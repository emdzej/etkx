# Fuellmengen SQL Queries

Total queries: 1

## RETRIEVE_FUELLMENGEN
**Description:** Retrieves data from w_fuellmengen filtered by fuellmengen_typ, CAST and ordered by Typ, Getriebe. Used in the Fuellmengen module to support ETK workflows for fuellmengen data.


- Type: SELECT
- Tables: w_fuellmengen
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: yes

```sql
select fuellmengen_typ Typ, fuellmengen_getriebeben Getriebe, fuellmengen_motor Motor, fuellmengen_fm_getriebe FMGetriebe, fuellmengen_fm_motor FMMotor, fuellmengen_fm_ha FMHinterachse, fuellmengen_fm_km_mitac FMKuehlmittelMitAC, fuellmengen_fm_km_ohneac FMKuehlmittelOhneAC, fuellmengen_fm_bremse FMBremse, fuellmengen_hw_&SPRACHE& Hinweis from w_fuellmengen where fuellmengen_typ in (&TYPEN&) and substr(to_char(fuellmengen_ab), 1, 6) CAST INTEGER <= &PRODUKTIONSDATUM_YYYYMM& and nvl(substr(to_char(fuellmengen_bis), 1, 6) CAST INTEGER, 999999) >= &PRODUKTIONSDATUM_YYYYMM& order by Typ, Getriebe
```
