# Teileliste SQL Queries

Total queries: 1

## Table Relationships (Co-occurrence)

- w_teil ↔ w_teil_marken ↔ w_ben_gk

## RETRIEVE_TEIL

- Type: SELECT
- Tables: w_teil, w_teil_marken, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr Hg,  teil_untergrup Ug,  ben_text Benennung,  teil_benennzus Zusatz, teilm_marke_tps Marke,  teil_art Teileart,  teil_produktkl ProduktKlasse, teil_mam MAM,  teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM,  NVL(teil_lagerverp, 0) LVM,  NVL(teil_beh_verp, 0) BVM, teil_fertigungshinweis FH,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk  where teil_sachnr = '&SACHNR&' and teil_textcode = ben_textcode  and ben_regiso = '&REGISO&'  and ben_iso = '&ISO&' and teil_sachnr = teilm_sachnr
```
