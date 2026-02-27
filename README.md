# etkx

BMW ETK (Electronic Parts Catalog) data extraction and analysis tools.

## Deployment

### Docker Compose

```bash
# build + run
mkdir -p data

docker compose up --build
```

Backend is available at `http://localhost:8080` and frontend at `http://localhost:3000`.

### Kubernetes (production)

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/pvc.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/oauth2-proxy.yaml
```

Notes:
- Replace placeholders in `k8s/secret.yaml` before applying.
- Ingress is configured for `etkx.bimmerz.app` with cert-manager TLS.

## Database Access

### Credentials
- **User:** `tbadmin`
- **Password:** `altabe`
- **Port:** 2024
- **Databases:** `etk_publ`, `etk_nutzer`, `etk_preise`

### Remote Connection
ETK Transbase on Windows XP must be running. Default IP: `192.168.101.150`

## Export Tools

### Prerequisites
- Java 11+ (OpenJDK)
- Transbase JDBC driver (`lib/tbjdbc.jar`)

### Quick Start

```bash
cd /tmp/etk-export

# Compile
/usr/local/opt/openjdk/bin/javac -cp tbjdbc.jar EtkExport.java

# Run export (foreground)
/usr/local/opt/openjdk/bin/java -Xmx1g -cp .:tbjdbc.jar EtkExport ./etk_data

# Run export (background)
nohup /usr/local/opt/openjdk/bin/java -Xmx1g -cp .:tbjdbc.jar EtkExport ./etk_data > export.log 2>&1 &

# Monitor progress
tail -f export.log
```

### Output Structure
```
etk_data/
├── csv/                    # All tables as CSV
│   ├── etk_publ_w_teil.csv
│   ├── etk_publ_w_bildtaf.csv
│   └── ...
└── blobs/                  # Binary data (images from w_grafik)
    ├── w_grafik_GRAFIK_0.bin
    └── ...
```

### Configuration

Edit `scripts/export.java` to change:
```java
static String URL = "jdbc:transbase://192.168.101.150:2024/";  // ETK host
static String USER = "tbadmin";
static String PASS = "altabe";
```

## Documentation

- `docs/SCHEMA.md` - Database schema (116 tables)
- `src/decompiled/` - Decompiled Java sources with SQL queries

## Key Tables

| Table | Description | ~Rows |
|-------|-------------|-------|
| `w_teil` | Parts master | 553K |
| `w_bildtaf` | Diagrams | 48K |
| `w_btzeilen` | Parts in diagrams | 1.7M |
| `w_grafik` | Images (BLOBs) | ~48K |
| `w_publben` | Multilingual names | ~2M |
| `w_fztyp` | Vehicle types | ~10K |

## License

Private repository - BMW ETK data is proprietary.
