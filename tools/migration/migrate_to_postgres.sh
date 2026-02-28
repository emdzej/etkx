#!/bin/bash
# Transbase to PostgreSQL Migration Script
#
# Prerequisites:
#   1. PostgreSQL database must exist (createdb etk)
#   2. Network access to Transbase server (192.168.101.150:2024)
#
# Usage:
#   ./migrate_to_postgres.sh [pg_host:port] [pg_database] [pg_user] [pg_password] [schema]
#
# Example:
#   ./migrate_to_postgres.sh localhost:5432 etk postgres mypassword etk

PG_HOST=${1:-localhost:5432}
PG_DB=${2:-etk}
PG_USER=${3:-postgres}
PG_PASS=${4:-postgres}
PG_SCHEMA=${5:-public}

echo ""
echo "Transbase to PostgreSQL Migration"
echo "=================================="
echo ""
echo "Target: jdbc:postgresql://$PG_HOST/$PG_DB"
echo "Schema: $PG_SCHEMA"
echo "User:   $PG_USER"
echo ""

# Check for PostgreSQL driver
if [ ! -f postgresql.jar ]; then
    echo "ERROR: postgresql.jar not found!"
    echo ""
    echo "Download from: https://jdbc.postgresql.org/download/"
    echo "Place postgresql-XX.X.X.jar as postgresql.jar in this directory."
    exit 1
fi

# Compile if needed
if [ ! -f TransbaseToPostgres.class ]; then
    echo "Compiling TransbaseToPostgres.java..."
    javac -cp "tbjdbc.jar:postgresql.jar" TransbaseToPostgres.java
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
fi

echo "Starting migration..."
echo ""

java -Xmx4g -cp "tbjdbc.jar:postgresql.jar:." TransbaseToPostgres "$PG_HOST" "$PG_DB" "$PG_USER" "$PG_PASS" "$PG_SCHEMA"

echo ""
echo "Migration finished."
