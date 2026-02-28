# ETKTexte SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_bed_etktext ↔ w_komm ↔ w_ben_gk

## RETRIEVE_ETKTEXTE

- Type: SELECT
- Tables: w_bed_etktext
- Parameterized: no
- Hardcoded literals: no
- Joins: yes | Filters: no | Sorting: yes

```sql
select bedetkt_elemid ElemId,      bedetkt_hg HG,      bedetkt_fg FG,      bedetkt_produktart Produktart,      bedetkt_kommid KommId  from w_bed_etktext  order by Produktart, HG, FG
```

## RETRIEVE_ETKTEXTE_KOMMENTARE

- Type: SELECT
- Tables: w_bed_etktext, w_komm, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select DISTINCT bedetkt_kommid KommId,               ben_text Text,              komm_pos KommPos from w_bed_etktext, w_komm, w_ben_gk  where komm_id = bedetkt_kommid    and ben_textcode = komm_textcode    and ben_iso = '&ISO&'    and ben_regiso = '&REGISO&'  order by KommId, KommPos
```
