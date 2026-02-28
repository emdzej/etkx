# TeilesucheAllgemein SQL Queries

Total queries: 1

## Table Relationships (Co-occurrence)

- w_bildtaf ↔ w_markt_etk ↔ w_ben_gk

## LOAD_MARKT_BENENNUNG
**Description:** Retrieves data from w_bildtaf, w_markt_etk, w_ben_gk filtered by marktetk_lkz, ben_iso, ben_regiso, bildtaf_produktart. Used in the TeilesucheAllgemein module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf, w_markt_etk, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select lj11.ben_text MarktBen  from   w_bildtaf t1  left join w_markt_etk lj1  on (lj1.marktetk_lkz = t1.bildtaf_lkz)  left join w_ben_gk lj11 on (lj1.marktetk_textcode = lj11.ben_textcode and                              lj11.ben_iso = '&ISO&' and                              lj11.ben_regiso = '&REGISO&')  where t1.bildtaf_btnr = '&BTNR&'  and   t1.bildtaf_produktart = '&PRODUKTART&'
```
