#!/bin/bash
# Optimize ETKx SQLite database (indexes + views)
# Executes all .sql files from scripts/sql/ directory in alphabetical order
# Usage: ./optimize-db.sh [path-to-db]

set -euo pipefail

DB_FILE="${1:-./data/etk.sqlite}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_DIR="$SCRIPT_DIR/sql"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log() { echo -e "${GREEN}[INFO]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# Check sqlite3
command -v sqlite3 &>/dev/null || error "sqlite3 not found. Install SQLite CLI."

# Check database exists
[[ -f "$DB_FILE" ]] || error "Database not found: $DB_FILE"

# Check SQL directory
[[ -d "$SQL_DIR" ]] || error "SQL directory not found: $SQL_DIR"

DB_SIZE=$(du -h "$DB_FILE" | cut -f1)
log "Database: $DB_FILE ($DB_SIZE)"
log "SQL directory: $SQL_DIR"
echo

# Find and execute all .sql files in alphabetical order
sql_files=$(find "$SQL_DIR" -maxdepth 1 -name "*.sql" -type f | sort)

if [[ -z "$sql_files" ]]; then
    warn "No .sql files found in $SQL_DIR"
    exit 0
fi

file_count=$(echo "$sql_files" | wc -l | tr -d ' ')
log "Found $file_count SQL file(s) to execute"
echo

echo "$sql_files" | while read -r sql_file; do
    filename=$(basename "$sql_file")
    log "Executing: $filename"
    
    if ! sqlite3 "$DB_FILE" < "$sql_file"; then
        error "Failed to execute: $filename"
    fi
done

echo
log "Running ANALYZE for query planner optimization..."
sqlite3 "$DB_FILE" "ANALYZE;"

# Show results
echo
log "Indexes created:"
sqlite3 "$DB_FILE" "SELECT '  - ' || name FROM sqlite_master WHERE type='index' AND name LIKE 'idx_%' ORDER BY name;"

echo
log "Views created:"
sqlite3 "$DB_FILE" "SELECT '  - ' || name FROM sqlite_master WHERE type='view' AND name LIKE 'v_%' ORDER BY name;"

echo
NEW_SIZE=$(du -h "$DB_FILE" | cut -f1)
log "Done! Database size: $DB_SIZE -> $NEW_SIZE"
