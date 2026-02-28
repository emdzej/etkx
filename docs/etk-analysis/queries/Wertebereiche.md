# Wertebereiche SQL Queries

Total queries: 1

## Table Relationships (Co-occurrence)

- w_publben ↔ w_ben_gk

## RETRIEVE_WERTEBEREICH
**Description:** Retrieves data from w_publben, w_ben_gk filtered by publben_art, publben_textcode, ben_iso, ben_regiso. Used in the Wertebereiche module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select publben_bezeichnung Value, ben_text Name  from w_publben, w_ben_gk where publben_art = '&ART&' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&'
```
