# Aspg SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_teil_aspg ↔ w_teil ↔ w_ben_gk

## LOAD_ASPG_TEILE

- Type: SELECT
- Tables: w_teil_aspg
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select teilaspg_sachnr_pg, teilaspg_vmenge from w_teil_aspg where teilaspg_sachnr = ? and teilaspg_kz_gruppe= ?
```

## LOAD_STECKER

- Type: SELECT
- Tables: w_teil_aspg, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr, teil_untergrup,teilaspg_sachnr_pg, ben_text, teilaspg_vmenge, teil_benennzus, teil_technik, teil_entfall_kez, teil_ist_diebstahlrelevant from w_teil_aspg, w_teil, w_ben_gk where teilaspg_sachnr = ? and teilaspg_kz_gruppe= ? and teilaspg_sachnr_pg = teil_sachnr and teil_textcode = ben_textcode and ben_iso = ? and ben_regiso = ?
```
