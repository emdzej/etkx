# Running Original BMW ETK on macOS

This guide describes how to run the original BMW ETK (Electronic Parts Catalog) Java application on macOS.

## Prerequisites

### 1. Java 8 (Temurin)

ETK requires Java 8 due to Tomcat 4.1.27 compatibility. Newer Java versions (9+) won't work.

```bash
# Download and install Temurin 8
cd /tmp
curl -L -o temurin8.tar.gz \
  'https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u402-b06/OpenJDK8U-jdk_x64_mac_hotspot_8u402b06.tar.gz'
tar xzf temurin8.tar.gz
mv jdk8u402-b06 ~/jdk8
```

### 2. ETK Files

ETK installation at `~/Documents/etk/javaclient/` with:
- `tomcat/` - Tomcat 4.1.27 server
- `classes/` - Client JAR files
- `libs/` - Dependencies

### 3. Transbase Database

Transbase server running at `192.168.101.150:2024` with `etk_publ` database.

## Configuration

### Server config (`tomcat/webapps/javaserver/WEB-INF/bmwetk.properties`)

```properties
database.connectionString = jdbc:transbase://192.168.101.150:2024/etk_publ
database.user = tbadmin
database.password = altabe
```

### Client config (`classes/javaclient.properties`)

```properties
server.URL = http://127.0.0.1:1033/javaserver/ClientService
```

### Tomcat config (`tomcat/conf/server.xml`)

Connector should bind to 127.0.0.1:

```xml
<Connector className="org.apache.coyote.tomcat4.CoyoteConnector" 
           port="1033" 
           address="127.0.0.1" 
           ... />
```

## Running ETK

### Using the script

```bash
# Start server + client
./scripts/start-etk-original.sh

# Start server only
./scripts/start-etk-original.sh server

# Start client only (if server running)
./scripts/start-etk-original.sh client

# Stop server
./scripts/start-etk-original.sh stop
```

### Manual start

```bash
export JAVA_HOME=~/jdk8/Contents/Home
cd ~/Documents/etk/javaclient

# Start Tomcat
./tomcat/bin/startup.sh

# Start client
CP="classes:classes/javaclientStarter.jar:classes/javaclient.jar"
for f in libs/*.jar tomcat/server/lib/*.jar tomcat/common/lib/*.jar tomcat/bin/*.jar; do
    CP="$CP:$f"
done
$JAVA_HOME/bin/java -cp "$CP" webetk.javaclient.Starter
```

## Known Issues

### 1. `javax.media.jai.util.ImagingException`

Java Advanced Imaging (JAI) issues on macOS. Some diagram views may not work. 
Native JAI libs for macOS would need to be compiled or found.

### 2. Menu items not working

Some features require external BMW services (SOWU, etc.) that are not available.
Core parts catalog functionality works.

### 3. Connection reset errors

If you see "Connection reset" errors:
- Ensure Tomcat is running: `lsof -i :1033`
- Check `classes/javaclient.properties` uses `127.0.0.1` (not `localhost`)
- Check `tomcat/conf/server.xml` has `address="127.0.0.1"` on Connector

## Logs

- Server: `~/Documents/etk/javaclient/etk_java_server.log`
- Tomcat: `~/Documents/etk/javaclient/tomcat/logs/catalina.out`
- Client: Start with `> /tmp/etk-client.log 2>&1` to capture

## Architecture

```
┌─────────────────┐     HTTP/1033     ┌─────────────────┐
│  ETK Client     │ ←───────────────→ │  Tomcat 4.1.27  │
│  (Swing GUI)    │                   │  (javaserver)   │
└─────────────────┘                   └────────┬────────┘
                                               │
                                          JDBC/2024
                                               │
                                      ┌────────▼────────┐
                                      │   Transbase DB  │
                                      │   (etk_publ)    │
                                      └─────────────────┘
```
