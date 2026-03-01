#!/bin/bash
# Start original BMW ETK (Java Swing client + Tomcat server)
# Requires: Java 8 (Temurin) in ~/jdk8, Transbase server at 192.168.101.150:2024

set -e

ETK_HOME="${ETK_HOME:-$HOME/Documents/etk/javaclient}"
JAVA_HOME="${JAVA_HOME:-$HOME/jdk8/Contents/Home}"

export JAVA_HOME

cd "$ETK_HOME"

# Check if Java 8 exists
if [ ! -f "$JAVA_HOME/bin/java" ]; then
    echo "ERROR: Java 8 not found at $JAVA_HOME"
    echo "Install Temurin 8: https://adoptium.net/temurin/releases/?version=8"
    exit 1
fi

# Check Java version
JAVA_VER=$("$JAVA_HOME/bin/java" -version 2>&1 | head -1)
echo "Using Java: $JAVA_VER"

start_server() {
    echo "Starting Tomcat server..."
    ./tomcat/bin/startup.sh
    sleep 3
    
    # Check if server is running
    if lsof -i :1033 > /dev/null 2>&1; then
        echo "✓ Tomcat running on port 1033"
    else
        echo "✗ Tomcat failed to start. Check logs:"
        echo "  tail -50 $ETK_HOME/tomcat/logs/catalina.out"
        exit 1
    fi
}

stop_server() {
    echo "Stopping Tomcat server..."
    ./tomcat/bin/shutdown.sh 2>/dev/null || true
}

start_client() {
    echo "Starting ETK client..."
    
    # Build classpath
    CP="classes:classes/javaclientStarter.jar:classes/javaclient.jar"
    for f in libs/*.jar tomcat/server/lib/*.jar tomcat/common/lib/*.jar tomcat/bin/*.jar; do
        CP="$CP:$f"
    done
    
    # Start GUI
    # Disable native medialib to avoid JAI ImagingException on macOS
    "$JAVA_HOME/bin/java" -Dcom.sun.media.jai.disableMediaLib=true -cp "$CP" webetk.javaclient.Starter &
    
    echo "✓ ETK client started"
}

case "${1:-all}" in
    server)
        start_server
        ;;
    client)
        start_client
        ;;
    stop)
        stop_server
        ;;
    all)
        start_server
        start_client
        ;;
    *)
        echo "Usage: $0 [server|client|stop|all]"
        echo "  server - Start Tomcat only"
        echo "  client - Start GUI client only"
        echo "  stop   - Stop Tomcat server"
        echo "  all    - Start server + client (default)"
        exit 1
        ;;
esac
