#!/bin/bash
# Upload SQLite database to etkx PVC
# Usage: ./upload-db.sh [path-to-db]

set -euo pipefail

NAMESPACE="etkx"
PVC_NAME="etkx-data"
DB_FILE="${1:-$HOME/Documents/etk/etk.sqlite}"
DEST_PATH="/data/etk.sqlite"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log() { echo -e "${GREEN}[INFO]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# Check if file exists
[[ -f "$DB_FILE" ]] || error "Database file not found: $DB_FILE"

DB_SIZE=$(du -h "$DB_FILE" | cut -f1)
log "Database: $DB_FILE ($DB_SIZE)"

# Check if namespace exists
kubectl get namespace "$NAMESPACE" &>/dev/null || error "Namespace $NAMESPACE not found"

# Get running pod name
POD=$(kubectl get pods -n "$NAMESPACE" -l app=etkx -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || true)

if [[ -n "$POD" && "$POD" != "null" ]]; then
    # Pod exists - use kubectl cp
    log "Found pod: $POD"
    log "Uploading via kubectl cp..."
    
    kubectl cp "$DB_FILE" "$NAMESPACE/$POD:$DEST_PATH" -c backend
    
    log "Upload complete. Restarting deployment..."
    kubectl rollout restart deployment/etkx -n "$NAMESPACE"
    
else
    # No pod - create temporary uploader pod
    warn "No running pod found. Creating temporary uploader..."
    
    cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Pod
metadata:
  name: db-uploader
  namespace: $NAMESPACE
spec:
  containers:
  - name: uploader
    image: busybox
    command: ["sleep", "3600"]
    volumeMounts:
    - name: data
      mountPath: /data
  volumes:
  - name: data
    persistentVolumeClaim:
      claimName: $PVC_NAME
  restartPolicy: Never
EOF

    log "Waiting for uploader pod..."
    kubectl wait --for=condition=Ready pod/db-uploader -n "$NAMESPACE" --timeout=60s
    
    log "Uploading database..."
    kubectl cp "$DB_FILE" "$NAMESPACE/db-uploader:$DEST_PATH"
    
    log "Cleaning up uploader pod..."
    kubectl delete pod db-uploader -n "$NAMESPACE"
fi

log "Done! Database uploaded to PVC $PVC_NAME"
