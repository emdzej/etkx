# ETKTexte SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_bed_etktext ↔ w_komm ↔ w_ben_gk

## RETRIEVE_ETKTEXTE
**Description:** Retrieves data from w_bed_etktext and ordered by Produktart, HG, FG. Used in the ETKTexte module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bed_etktext
- Parameterized: no
- Hardcoded literals: no
- Joins: yes | Filters: no | Sorting: yes

```sql
select bedetkt_elemid ElemId,      bedetkt_hg HG,      bedetkt_fg FG,      bedetkt_produktart Produktart,      bedetkt_kommid KommId  from w_bed_etktext  order by Produktart, HG, FG
```

## RETRIEVE_ETKTEXTE_KOMMENTARE
**Description:** Retrieves data from w_bed_etktext, w_komm, w_ben_gk filtered by komm_id, ben_textcode, ben_iso, ben_regiso and ordered by KommId, KommPos. Used in the ETKTexte module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bed_etktext, w_komm, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select DISTINCT bedetkt_kommid KommId,               ben_text Text,              komm_pos KommPos from w_bed_etktext, w_komm, w_ben_gk  where komm_id = bedetkt_kommid    and ben_textcode = komm_textcode    and ben_iso = '&ISO&'    and ben_regiso = '&REGISO&'  order by KommId, KommPos
```
