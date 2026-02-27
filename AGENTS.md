# AGENTS.md

## Project: etkx

BMW ETK (Electronic Parts Catalogue) reverse engineering and TypeScript implementation.

## Goals

1. Understand ETK database structure (Transbase)
2. Extract data from ROM files
3. Build TypeScript library for ETK data access
4. Create web viewer (like tisx/wdsx pattern)

## Key Files

- `docs/SCHEMA.md` — Database schema (116 tables)
- `src/decompiled/SQLStatementsTransbase.java` — Original SQL queries (decompiled)
- `docs/etk-tables.txt` — Table list

## ETK Source Location

- Mac: `/Users/emdzej/Documents/etk/`
- Windows shared: `S:\etk\`

## Database

- **Engine:** Transbase 6.1.2
- **License:** Linux only (in tblic.ini)
- **ROM files:** `roms/rfile000.000` (~2GB), `rfile001.000` (~900MB)

## Architecture

```
ETK.exe (JVM launcher)
    └── webetk.javaclient.Starter
        └── Embedded Tomcat (port 1033)
            └── JDBC → Transbase
                └── etk_publ (catalog)
                └── etk_nutzer (users)
                └── etk_preise (prices)
```

## Conventions

- Code, comments, commits in English
- TypeScript for implementation
- Follow tisx/wdsx patterns for web viewer
