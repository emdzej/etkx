# ETK Export Tools

Tools for exporting BMW ETK (Electronic Parts Catalog) data from Transbase to other databases.

## Prerequisites

- Java 11+ (JDK)
- Network access to Transbase server (192.168.101.150:2024)
- JDBC drivers in this directory:
  - `tbjdbc.jar` - Transbase JDBC driver (included)
  - `postgresql.jar` - PostgreSQL JDBC driver (download separately)
  - `sqlite-jdbc.jar` - SQLite JDBC driver (included)

## Migration Options

### 1. Transbase → SQLite

Best for: Single-file deployment, embedded applications, development.

```bash
# Windows
java -Xmx4g -cp "tbjdbc.jar;sqlite-jdbc.jar;." TransbaseToSqlite etk.sqlite

# Linux/Mac
java -Xmx4g -cp "tbjdbc.jar:sqlite-jdbc.jar:." TransbaseToSqlite etk.sqlite
```

Output: ~4.8GB SQLite file with all tables and BLOBs.

### 2. Transbase → PostgreSQL

Best for: Production deployments, multi-user access, advanced queries.

```bash
# Windows
migrate_to_postgres.bat localhost:5432 etk postgres mypassword public

# Linux/Mac
chmod +x migrate_to_postgres.sh
./migrate_to_postgres.sh localhost:5432 etk postgres mypassword public
```

**Before running:**
1. Create PostgreSQL database: `createdb etk`
2. Download PostgreSQL JDBC driver from https://jdbc.postgresql.org/download/
3. Rename/copy to `postgresql.jar` in this directory

**Arguments:**
| Arg | Default | Description |
|-----|---------|-------------|
| 1 | localhost:5432 | PostgreSQL host:port |
| 2 | etk | Database name |
| 3 | postgres | Username |
| 4 | postgres | Password |
| 5 | public | Schema name |

## Database Schema

### Key Tables

| Table | Description | Rows (approx) |
|-------|-------------|---------------|
| w_teil | Parts master | 553K |
| w_bildtaf | Diagrams | 48K |
| w_btzeilen | Parts in diagrams | 1.7M |
| w_grafik | Images (BLOBs) | 66K |
| w_publben | Multilingual names | ~10M |
| w_teileersetzung | Part supersessions | 5.6M |
| w_fztyp | Vehicle types | 4K |
| w_baureihe | Series | 174 |

### Total
- **129 tables**
- **~56 million rows**
- **~570K BLOBs** (images)

## Type Mappings

| Transbase | SQLite | PostgreSQL |
|-----------|--------|------------|
| TINYINT | INTEGER | SMALLINT |
| SMALLINT | INTEGER | SMALLINT |
| INTEGER | INTEGER | INTEGER |
| BIGINT | INTEGER | BIGINT |
| FLOAT | REAL | REAL |
| DOUBLE | REAL | DOUBLE PRECISION |
| CHAR(n) | TEXT | VARCHAR(n) |
| VARCHAR(n) | TEXT | VARCHAR(n) |
| CLOB | TEXT | TEXT |
| BLOB | BLOB | BYTEA |
| DATE | TEXT | DATE |
| TIMESTAMP | TEXT | TIMESTAMP |

## Files

| File | Description |
|------|-------------|
| TransbaseToSqlite.java | Direct TB→SQLite migration |
| TransbaseToPostgres.java | Direct TB→PostgreSQL migration |
| migrate_to_postgres.bat | Windows script for PostgreSQL |
| migrate_to_postgres.sh | Linux/Mac script for PostgreSQL |
| EtkExport.java | Export to CSV files |
| ImportSqlite.java | Import CSVs to SQLite |
| SchemaTranslated.java | Generate translated schema |

## Performance Notes

- Migration takes 15-30 minutes depending on network/disk speed
- Uses 4GB heap (-Xmx4g) for batch processing
- Batch size: 5000 rows (configurable in source)
- Indexes created after data load for speed

## Transbase Connection

```
Host: 192.168.101.150
Port: 2024
Database: etk_publ
User: tbadmin
Password: altabe
```

## Troubleshooting

### "No suitable driver found"
- Ensure JDBC driver JARs are in classpath
- Check JAR file names match those in -cp argument

### "Connection refused"
- Verify network access to Transbase server
- Check firewall rules for port 2024

### "Out of memory"
- Increase heap: `-Xmx8g`
- Reduce BATCH_SIZE in source code

### PostgreSQL "database does not exist"
- Create database first: `createdb etk`
