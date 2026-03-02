# ETKx

BMW ETK (Electronic Parts Catalog) web viewer — modern SvelteKit frontend with Spring Boot backend.

## Quick Start (Local Development)

### Prerequisites

- Java 21+ (backend)
- Node.js 20+ with pnpm (frontend)
- SQLite database file (`etk.sqlite`)

### 1. Start Backend

```bash
cd service

# Set database path
export SPRING_DATASOURCE_URL=jdbc:sqlite:/path/to/etk.sqlite

# Run
./gradlew bootRun
```

Backend API: http://localhost:8080

### 2. Start Frontend

```bash
cd frontend
pnpm install
pnpm dev
```

Frontend: http://localhost:5173 (proxies API to :8080)

---

## Database Migration (Transbase → SQLite)

The original ETK uses Transbase. This section describes how to migrate to SQLite.

### Prerequisites

- Original ETK Transbase running (IP: `192.168.101.150`, port: `2024`)
- Java 11+ on host machine
- JDBC drivers:
  - `tbjdbc.jar` (Transbase) — from ETK installation
  - `sqlite-jdbc-3.x.jar` — [download](https://github.com/xerial/sqlite-jdbc/releases)

### Migration Script

```bash
cd scripts

# Download SQLite JDBC if not present
curl -LO https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar

# Compile migration tool
javac -cp tbjdbc.jar:sqlite-jdbc-3.45.1.0.jar TransbaseToSqlite.java

# Run migration (takes ~30-60 minutes for full DB)
java -Xmx2g -cp .:tbjdbc.jar:sqlite-jdbc-3.45.1.0.jar TransbaseToSqlite etk.sqlite
```

### Configuration

Edit `TransbaseToSqlite.java` to change connection settings:

```java
static final String TB_URL = "jdbc:transbase://192.168.101.150:2024/etk_publ";
static final String TB_USER = "tbadmin";
static final String TB_PASS = "altabe";
```

### Output

- `etk.sqlite` — ~5GB SQLite database with all ETK data
- Tables: 116 tables including parts, diagrams, images, translations

---

## Docker

### Build and Run

```bash
# Place etk.sqlite in ./data/
docker-compose up --build
```

- Frontend: http://localhost:3000
- Backend: http://localhost:8080

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PUBLIC_API_BASE_URL` | Backend URL (frontend build-time) | `http://localhost:8080` |
| `SPRING_DATASOURCE_URL` | SQLite path | `jdbc:sqlite:./etk.sqlite` |

---

## Project Structure

```
etkx/
├── frontend/          # SvelteKit + Tailwind CSS
│   ├── src/
│   │   ├── lib/       # Components, API client, stores
│   │   └── routes/    # Pages (/{brand}/{productType}/{scope}/...)
│   └── package.json
├── service/           # Spring Boot backend
│   ├── src/main/java/pl/emdzej/etkx/
│   │   ├── api/       # REST controllers
│   │   └── dal/       # Data access layer
│   └── build.gradle
├── scripts/           # Migration and utility scripts
│   ├── TransbaseToSqlite.java
│   └── start-etk-original.sh
├── k8s/               # Kubernetes manifests
└── docs/              # Documentation
```

---

## URL Structure

```
/{brand}/{productType}/{catalogScope}/vehicles/{mospId}/groups/{hg}/subgroups/{fg}/diagrams/{btnr}
```

| Param | Values | Description |
|-------|--------|-------------|
| `brand` | `bmw`, `mini`, `rolls-royce` | Vehicle brand |
| `productType` | `car`, `motorcycle` | Product type |
| `catalogScope` | `current`, `classic` | Catalog scope (VT/ST) |
| `mospId` | e.g. `47669` | Vehicle model identifier |
| `datum` | `YYYY-MM-DD` (query param) | Production date for typ resolution |


## Original ETK (Java Swing)

To run the original BMW ETK application on macOS:

```bash
# Requires Java 8 and running Transbase
./scripts/start-etk-original.sh
```

See `docs/original-etk-setup.md` for full setup instructions.

---

## License

Licensed under PolyForm Noncommercial 1.0.0 — free for personal and educational use, no commercial use permitted.
