# Lagerzeit SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_teil ↔ w_teil_marken
- w_teil ↔ w_teil_marken ↔ w_ben_gk ↔ w_tc_performance_allg

## RETRIEVE_HGS

- Type: SELECT
- Tables: w_teil, w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG from w_teil, w_teil_marken  where teil_lzb = 'J' and teil_hauptgr >= '&HGAB&' &HGBIS_STMT& and teil_produktart IN ('&PRODUKTART&', 'B') and teil_sachnr = teilm_sachnr  and teilm_marke_tps = '&MARKE&'  order by HG
```

## RETRIEVE_TEILE

- Type: SELECT
- Tables: w_teil, w_teil_marken, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, ben_text BENENNUNG, teil_benennzus ZUSATZ, teil_vorhanden_si_ohne_lzb SI, teil_kom_pi PI, teil_textcode_kom BENKOMMENTARID,  teil_ist_reach REACH,  teil_ist_aspg ASPG,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where teil_lzb = 'J' and teil_hauptgr = '&HG&' and teil_produktart IN ('&PRODUKTART&', 'B') order by HG, UG, SACHNR
```
