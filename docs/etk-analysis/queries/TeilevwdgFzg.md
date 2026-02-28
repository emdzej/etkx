# TeilevwdgFzg SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_btzeilen ↔ w_btzeilen_verbauung
- w_btzeilen_verbauung ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance
- w_btzeilen_verbauung ↔ w_teil ↔ w_btzeilen ↔ w_ben_gk ↔ w_tc_performance

## RETRIEVE_HGS

- Type: SELECT
- Tables: w_teileverwendungfzg_suche
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select distinct tvs_hg HG from w_teileverwendungfzg_suche where tvs_mospid IN (&MOSPIDS&) and tvs_hg >= '&HGAB&' &HGBIS_STMT& &DATSERIE_STMT& order by HG
```

## RETRIEVE_TEILE_OHNE_LENKUNG

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_teil, w_ben_gk, w_tc_performance
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct NeuesTeil.teil_hauptgr HG, NeuesTeil.teil_untergrup UG, NeuesTeil.teil_sachnr SACHNR, NeuesTeil.teil_alt SACHNRALT, NeuesTeil.teil_austausch_alt AT, AltesTeil.teil_hauptgr HGALT, AltesTeil.teil_untergrup UGALT, ben_text BENENNUNG, NeuesTeil.teil_benennzus ZUSATZ, NeuesTeil.teil_vorhanden_si SI, NeuesTeil.teil_lzb LZB, NeuesTeil.teil_kom_pi PI, NeuesTeil.teil_textcode_kom BENKOMMENTARID, NeuesTeil.teil_ist_reach REACH,  NeuesTeil.teil_ist_aspg ASPG,  NeuesTeil.teil_ist_stecker STECKER,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung inner join w_teil NeuesTeil on (btzeilenv_sachnr = NeuesTeil.teil_sachnr and NeuesTeil.teil_hauptgr = '&HG&') inner join w_ben_gk on (NeuesTeil.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr) left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = NeuesTeil.teil_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where btzeilenv_mospid IN (&MOSPIDS&) &DATSERIE_STMT& and btzeilenv_alter_kz is null order by HG, UG, SACHNR
```

## RETRIEVE_TEILE_MIT_LENKUNG

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_teil, w_btzeilen, w_ben_gk, w_tc_performance
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct NeuesTeil.teil_hauptgr HG, NeuesTeil.teil_untergrup UG, NeuesTeil.teil_sachnr SACHNR, NeuesTeil.teil_alt SACHNRALT, NeuesTeil.teil_austausch_alt AT, AltesTeil.teil_hauptgr HGALT, AltesTeil.teil_untergrup UGALT, ben_text BENENNUNG, NeuesTeil.teil_benennzus ZUSATZ, NeuesTeil.teil_vorhanden_si SI, NeuesTeil.teil_lzb LZB, NeuesTeil.teil_kom_pi PI, NeuesTeil.teil_textcode_kom BENKOMMENTARID, NeuesTeil.teil_ist_reach REACH,  NeuesTeil.teil_ist_aspg ASPG,  NeuesTeil.teil_ist_stecker STECKER,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung inner join w_teil NeuesTeil on (btzeilenv_sachnr = NeuesTeil.teil_sachnr and NeuesTeil.teil_hauptgr = '&HG&') inner join w_btzeilen on (btzeilenv_sachnr = btzeilen_sachnr and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos and (btzeilen_lenkg = '&LENKUNG&' OR btzeilen_lenkg IS NULL)) inner join w_ben_gk on (NeuesTeil.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr) left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = NeuesTeil.teil_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where btzeilenv_mospid IN (&MOSPIDS&) &DATSERIE_STMT& and btzeilenv_alter_kz is null order by HG, UG, SACHNR
```

## RETRIEVE_TEIL_ONLY_IN_OHNE_LENKUNG

- Type: SELECT
- Tables: w_btzeilen_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count (*) ANZ from w_btzeilen_verbauung where btzeilenv_sachnr = '&SACHNR&' and btzeilenv_mospid NOT IN (&MOSPIDS&)
```

## RETRIEVE_TEIL_ONLY_IN_MIT_LENKUNG

- Type: SELECT
- Tables: w_btzeilen, w_btzeilen_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select count (*) ANZ from w_btzeilen, w_btzeilen_verbauung where btzeilen_sachnr = '&SACHNR&' and (btzeilen_lenkg = '&LENKUNG&' OR btzeilen_lenkg IS NULL) and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_sachnr = btzeilenv_sachnr and btzeilenv_mospid NOT IN (&MOSPIDS&)
```
