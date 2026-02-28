# Erstbevorratung SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_erstbevorratung ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance

## RETRIEVE_HGS
**Description:** Retrieves data from w_erstbevorratung_suche filtered by ebs_mospid, ebs_hg and ordered by HG. Used in the Erstbevorratung module to support ETK workflows for erstbevorratung suche data.


- Type: SELECT
- Tables: w_erstbevorratung_suche
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select distinct ebs_hg HG from w_erstbevorratung_suche where ebs_mospid IN (&MOSPIDS&) and ebs_hg >= '&HGAB&' &HGBIS_STMT& &LENKUNG_STMT& order by HG
```

## RETRIEVE_TEILE
**Description:** Retrieves data from w_erstbevorratung, w_teil, w_ben_gk, w_tc_performance filtered by eb_sachnr, ben_iso, ben_regiso, tcp_sachnr, tcp_datum_von, tcp_datum_bis, eb_mospid and ordered by HG, UG, SACHNR. Used in the Erstbevorratung module to support ETK workflows for erstbevorratung data.


- Type: SELECT
- Tables: w_erstbevorratung, w_teil, w_ben_gk, w_tc_performance
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, ben_text BENENNUNG, teil_benennzus ZUSATZ, teil_vorhanden_si SI, teil_lzb LZB, teil_kom_pi PI, teil_textcode_kom BENKOMMENTARID,  teil_ist_reach REACH,  teil_ist_aspg ASPG,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_erstbevorratung inner join w_teil on (eb_sachnr = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = eb_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where eb_hg = '&HG&' and eb_mospid IN (&MOSPIDS&) &LENKUNG_STMT& order by HG, UG, SACHNR
```
