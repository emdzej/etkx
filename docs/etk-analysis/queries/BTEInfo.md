# BTEInfo SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_bildtaf ↔ w_ben_gk
- w_bildtaf ↔ w_ben_gk ↔ w_komm

## RETRIEVE_BTEINFO
**Description:** Retrieves data from w_bildtaf, w_ben_gk filtered by bildtaf_btnr, bildtaf_produktart, bildtaf_textc, ben_iso, ben_regiso. Used in the BTEInfo module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select ben.ben_text Ueberschrift from w_bildtaf, w_ben_gk ben where bildtaf_btnr = '&BTENR&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_textc = ben.ben_textcode and ben.ben_iso = '&ISO&' and  ben.ben_regiso = '&REGISO&'
```

## RETRIEVE_BTEKOMMENTAR
**Description:** Retrieves data from w_bildtaf, w_ben_gk, w_komm filtered by bildtaf_btnr, bildtaf_kommbt, komm_textcode, ben_iso, ben_regiso and ordered by KommId, Pos. Used in the BTEInfo module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf, w_ben_gk, w_komm
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct komm_id KommId, ben_text Text, komm_code Code, komm_vz VZ,  komm_darstellung Darstellung, komm_tiefe Tiefe,  komm_pos Pos from w_bildtaf, w_ben_gk, w_komm where bildtaf_btnr = '&BTNR&' and bildtaf_kommbt = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos
```
