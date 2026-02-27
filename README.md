# BMW ETK (Electronic Parts Catalogue) Analysis

## Overview

**Version:** ETK 02/2013, Client v2.0.95
**Database:** Transbase (proprietary SQL database by Transaction Software GmbH)
**Architecture:** Java client + embedded Tomcat + Transbase DB

## Directory Structure

```
etk/
├── javaclient/           # Java application
│   ├── ETK.exe           # JVM launcher (C wrapper)
│   ├── ETK.exe.c         # Ghidra decompilation
│   ├── classes/
│   │   ├── javaclient.jar     # Main client app (5MB)
│   │   ├── zubclient.jar      # Accessories module (3MB)
│   │   └── javaclient.properties
│   ├── libs/
│   │   └── tbjdbc.jar         # Transbase JDBC driver
│   └── tomcat/           # Embedded servlet container
├── transbase/            # Database engine + config
│   ├── etk_nutzer/       # User/config database
│   ├── etk_preise/       # Pricing database
│   ├── etk_publ/         # Publication data (main catalog)
│   ├── tbker32.dll       # Transbase kernel
│   ├── tbjdbc.jar        # JDBC driver
│   └── webretknutzer_tb.sql  # User DB schema
├── roms/                 # Main data files
│   ├── rfile000.000      # Cluster 0 (~2GB)
│   ├── rfile001.000      # Cluster 1 (~900MB)
│   └── *.sql             # Update scripts
├── admintool/            # Admin application
└── migration/            # DMS migration tools
```

## Databases

### 1. etk_publ (Publication Data - Main Catalog)
- **Location:** `transbase/etk_publ/` + `roms/rfile*.000`
- **Size:** ~3GB total
- **Content:** Part numbers, descriptions, images, vehicle mappings
- **Volume ID:** `TB_ETK_0213`

Configuration (`etk_publ/dbconf.ini`):
- Page size: 4096 bytes
- ROM size: 1.5GB
- Disk size: 100MB
- Codepage: UTF-8
- 2 clusters (CD_1: rfile000, CD_2: rfile001)

### 2. etk_nutzer (User Data)
- **Tables:** w_firma, w_filiale, w_user, w_konfig, w_teileliste, etc.
- **Purpose:** User settings, shopping carts, orders

### 3. etk_preise (Pricing)
- **Table:** w_preise
- **Fields:** sachnr (7-char part#), evpreis, rabattschluessel, mwst, etc.

## Key Entities

### Part Number (Sachnummer/Teilenummer)
- 7-digit format (compact) or 11-digit format (full)
- Examples: `1234567` or `12 34 5 678 901`

### Brand (Marke)
- BMW, MINI, Rolls-Royce

### Vehicle Identification
- VIN (Fahrgestellnummer): 7-20 characters
- Used for exact part matching

## Java Application Structure

Main package: `webetk.*`

```
webetk/
├── app/              # UI modules
│   ├── fzgsuche/     # Vehicle search
│   ├── teileinfo/    # Part info
│   ├── teileersetzung/  # Part replacement
│   ├── bteanzeige/   # Assembly display
│   ├── visualisierungteil/  # Part visualization
│   └── zub/          # Accessories
├── db/               # Database access
│   ├── dbaccess.class    # Main DB access class
│   ├── teilesuchefzg/    # Vehicle part search
│   └── teilevwdgfzg/     # Vehicle part usage
├── communication/    # Client-server comm
├── javaclient/       # Client bootstrap
└── interfaces/       # External interfaces
```

Main class: `webetk.javaclient.Starter`

## Configuration

**java_config.ini:**
```ini
MainClass=webetk.javaclient.Starter
JavaMaxMemory=576
JDBCURL=jdbc:transbase://localhost/etk_nutzer
JavaHome=..\\java\\jre
```

**javaclient.properties:**
```properties
server.URL=http://localhost:1033/javaserver/ClientService
standalone=1
tomcat.port=1033
language=en
```

## Database Connection

- **JDBC URL:** `jdbc:transbase://localhost/<database>`
- **Port:** 2024-2025 (Transbase service)
- **Driver:** `transbase.jdbc.Driver` (tbjdbc.jar)

## User Tables Schema (etk_nutzer)

```sql
-- Company
CREATE TABLE w_firma (
    firma_id VARCHAR(10) NOT NULL KEY,
    firma_bezeichnung VARCHAR(40) NOT NULL,
    firma_verzeichnis VARCHAR(256)
);

-- User
CREATE TABLE w_user (
    user_firma_id VARCHAR(10) NOT NULL,
    user_id VARCHAR(10) NOT NULL,
    user_name VARCHAR(20) NOT NULL,
    user_passwort VARCHAR(20) NOT NULL,
    user_default_filiale_id VARCHAR(4) NOT NULL,
    KEY IS user_firma_id, user_id
);

-- Shopping Cart (Teileliste)
CREATE TABLE w_teileliste (
    teileliste_firma_id VARCHAR(10) NOT NULL,
    teileliste_filiale_id VARCHAR(4) NOT NULL,
    teileliste_user_id VARCHAR(10) NOT NULL,
    teileliste_id VARCHAR(20) NOT NULL,
    teileliste_marke VARCHAR(11) NOT NULL,
    teileliste_vin CHAR(7),
    -- ... more fields
    KEY IS teileliste_firma_id, teileliste_filiale_id, 
           teileliste_user_id, teileliste_id
);

-- Cart Items (Teilelistepos)
CREATE TABLE w_teilelistepos (
    teilelistepos_firma_id VARCHAR(10) NOT NULL,
    teilelistepos_filiale_id VARCHAR(4) NOT NULL,
    teilelistepos_user_id VARCHAR(10) NOT NULL,
    teilelistepos_teileliste_id VARCHAR(20) NOT NULL,
    teilelistepos_position INTEGER NOT NULL,
    teilelistepos_sachnr CHAR(7) NOT NULL,
    teilelistepos_benennung VARCHAR(40),
    teilelistepos_menge NUMERIC(7,2),
    teilelistepos_preis NUMERIC(11,2),
    -- ... more fields
);

-- Pricing
CREATE TABLE w_preise (
    preise_firma VARCHAR(10) NOT NULL,
    preise_sachnr CHAR(7) NOT NULL,
    preise_evpreis NUMERIC(11,2),
    preise_rabattschluessel VARCHAR(3),
    preise_mwst NUMERIC(4,2),
    KEY IS preise_firma, preise_sachnr
);
```

## Discovered Tables (from Java bytecode analysis)

### Publication Tables (etk_publ)
| Table | Description |
|-------|-------------|
| `w_fztyp` | Vehicle types (Fahrzeugtyp) |
| `w_markt_produkt_br` | Market/Product/Baureihe mapping |
| `w_btzeilen_verbauung` | Assembly lines with vehicle usage |
| `w_btzeilenzub_verbauung` | Accessory assembly lines with vehicle usage |
| `w_btzeilenzubugb` | Accessory HG/UG assembly lines |
| `w_bildtaf*` | Image tables (Bildtafel) |
| `w_dealer_type` | Dealer types per market |

### Column naming convention (from SQL analysis)
| Prefix | Meaning | Example |
|--------|---------|---------|
| `fztyp_` | Vehicle type | `fztyp_mospid` |
| `btzeilenzv_` | Assembly line ZUB verbauung | `btzeilenzv_btnr`, `btzeilenzv_pos`, `btzeilenzv_mospid` |
| `btzeilenuz_` | Assembly line ZUB UGB | `btzeilenuz_btnr`, `btzeilenuz_pos`, `btzeilenuz_elementart` |
| `marktprod_` | Market product | `marktprod_btnr`, `marktprod_systemid`, `marktprod_marktid` |
| `bildtaf_` | Image table | `bildtaf_btnr` |

### Key IDs discovered
- `btnr` - Bildtafel number (image/diagram ID)
- `mospid` - Model/Series/Production ID
- `marktid` - Market ID
- `systemid` - System ID (=1 for BMW)
- `elementart` - Element type (e.g., 'HP_PAKET' for accessory packages)

## Next Steps for Data Extraction

1. **Start Transbase server** on Windows (needs Windows Node)
   - Run `tbkern32.exe` or `transbase.exe`
   - Connect via JDBC

2. **Query etk_publ schema:**
   ```sql
   SELECT * FROM @@systable;  -- List all tables
   SELECT * FROM @@syscolumn; -- List all columns
   ```

3. **Key publication tables to find:**
   - Parts master (Teilestamm)
   - Vehicle models (Fahrzeugmodelle)
   - Assemblies (Baugruppen)
   - Part-vehicle mappings
   - Images/graphics

4. **Alternative: Direct ROM file analysis**
   - Transbase ROM files have documented structure
   - Could reverse-engineer without running server

## Tools Needed

- **Java Runtime** - to decompile and run ETK
- **Transbase client** - to query databases
- **Windows environment** - ETK is Windows-only

## Notes

- Transbase is still maintained: https://www.transaction.de/
- ETK has been replaced by ETK Online (web-based)
- Data structure likely similar to ISTA/Rheingold
