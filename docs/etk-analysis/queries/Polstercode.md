# Polstercode SQL Queries

Total queries: 1

## Table Relationships (Co-occurrence)

- w_bed_aflpc ↔ w_ben_gk

## LOAD_POLSTERCODE
**Description:** Retrieves data from w_bed_aflpc, w_ben_gk filtered by ben_textcode, ben_iso, ben_regiso and ordered by Pos. Used in the Polstercode module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bed_aflpc, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select bedaflpc_art Art, bedaflpc_code Code, ben_text Benennung, bedaflpc_pcode PCode, bedaflpc_gilt_v GueltigVon, bedaflpc_gilt_b GueltigBis, bedaflpc_pos Pos from w_bed_aflpc, w_ben_gk where ben_textcode = bedaflpc_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'  order by Pos
```
