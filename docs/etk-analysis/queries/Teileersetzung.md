# Teileersetzung SQL Queries

Total queries: 3

## Table Relationships (Co-occurrence)

- w_teileersetzung ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance

## RETRIEVE_HGS
**Description:** Retrieves data from w_teileersetzung_suche filtered by tss_mospid, tss_hg and ordered by HG. Used in the Teileersetzung module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileersetzung_suche
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select distinct tss_hg HG from w_teileersetzung_suche where tss_mospid IN (&MOSPIDS&) and tss_hg >= '&HGAB&' &HGBIS_STMT& &LENKUNG_STMT& &DATSERIE_STMT& order by HG
```

## FIND_HG
**Description:** Retrieves data from w_teil filtered by teil_hauptgr. Used in the Teileersetzung module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr HG from w_teil where teil_hauptgr = '&HG&'
```

## RETRIEVE_TEILE
**Description:** Retrieves data from w_teileersetzung, w_teil, w_ben_gk, w_tc_performance filtered by ts_sachnr, ben_iso, ben_regiso, tcp_sachnr, tcp_datum_von, tcp_datum_bis, ts_mospid and ordered by HGALT, UGALT, SACHNRALT, HG, UG, SACHNR. Used in the Teileersetzung module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileersetzung, w_teil, w_ben_gk, w_tc_performance
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct NEU.teil_hauptgr HG,  NEU.teil_untergrup UG,  ts_sachnr SACHNR, NEU.teil_alt SACHNRALT, ALT.teil_hauptgr HGALT, ALT.teil_untergrup UGALT, NEU.teil_austausch_alt AT, ben_text BENENNUNG, NEU.teil_benennzus ZUSATZ, NEU.teil_vorhanden_si SI, NEU.teil_lzb LZB, NEU.teil_kom_pi PI, NEU.teil_textcode_kom BENKOMMENTARID, NEU.teil_ist_reach REACH,  NEU.teil_ist_aspg ASPG,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NEU.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teileersetzung inner join w_teil NEU on (ts_sachnr = NEU.teil_sachnr)      inner join w_teil ALT on (NEU.teil_alt = ALT.teil_sachnr)      inner join w_ben_gk on (NEU.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')      left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = ts_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where ts_hg = '&HG&' and ts_mospid IN (&MOSPIDS&) &LENKUNG_STMT& &DATSERIE_STMT& order by HGALT, UGALT, SACHNRALT, HG, UG, SACHNR
```
