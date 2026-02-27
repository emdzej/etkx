#!/bin/bash
# Test connection to ETK Transbase

HOST="${1:-192.168.101.150}"
PORT="${2:-2024}"

echo "Testing TCP connection to $HOST:$PORT..."
nc -zv $HOST $PORT 2>&1

echo ""
echo "To test JDBC, run:"
echo "  cd scripts && java -cp ../lib/tbjdbc.jar:. TestConnection"
