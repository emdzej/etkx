# SatzEinzelteile SQL Queries

Total queries: 4

## Table Relationships (Co-occurrence)

- w_kompl_einzelteil ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance_allg
- w_kompl_satz ↔ w_ben_gk ↔ w_hgfg
- w_kompl_satz ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance_allg

## LOAD_HG
**Description:** Retrieves data from w_kompl_satz, w_ben_gk, w_hgfg filtered by ks_marke_tps, ks_produktart, hgfg_produktart, ks_hg, hgfg_fg, hgfg_textcode, ben_iso, ben_regiso and ordered by HG. Used in the SatzEinzelteile module to support ETK workflows for kompl satz data.


- Type: SELECT
- Tables: w_kompl_satz, w_ben_gk, w_hgfg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg HG, ben_text Benennung from w_kompl_satz, w_ben_gk, w_hgfg where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and hgfg_produktart = '&PRODUKTART&' and ks_hg = hgfg_hg and hgfg_fg = '00' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by HG
```

## LOAD_SATZ
**Description:** Retrieves data from w_kompl_satz, w_teil, w_ben_gk, w_tc_performance_allg filtered by ks_sachnr_satz, ben_iso, ben_regiso, tcp_marke_tps, tcp_produktart, tcp_vbereich, tcp_datum_von, tcp_datum_bis, ks_produktart, ks_hg and ordered by HG, UG, Sachnummer. Used in the SatzEinzelteile module to support ETK workflows for kompl satz data.


- Type: SELECT
- Tables: w_kompl_satz, w_teil, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId, teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC from w_kompl_satz inner join w_teil on (ks_sachnr_satz = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and ks_hg = '&HG&' order by HG, UG, Sachnummer
```

## COUNT_EINZELTEILE
**Description:** Retrieves data from w_kompl_einzelteil filtered by ke_sachnr_satz. Used in the SatzEinzelteile module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_kompl_einzelteil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(ke_sachnr_einzelteil) Anzahl  from w_kompl_einzelteil where ke_sachnr_satz = '&SACHNUMMER&'
```

## LOAD_EINZELTEIL
**Description:** Retrieves data from w_kompl_einzelteil, w_teil, w_ben_gk, w_tc_performance_allg filtered by ke_sachnr_einzelteil, ben_iso, ben_regiso, tcp_marke_tps, tcp_produktart, tcp_vbereich, tcp_datum_von, tcp_datum_bis and ordered by Pos. Used in the SatzEinzelteile module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_kompl_einzelteil, w_teil, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, ke_menge Menge, ke_beziehbar istBeziehbar, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId,  teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC, teil_ist_eba istEBA, ke_pos Pos from w_kompl_einzelteil inner join w_teil on (ke_sachnr_einzelteil = teil_sachnr)   inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')   left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                   and tcp_marke_tps = '&MARKE&'                                    and tcp_produktart = '&PRODUKTART&'                                   and tcp_vbereich in (&KATALOGUMFAENGE&)                                   &TC_CHECK_LANDKUERZEL&                                   and tcp_datum_von <= &DATUM&                                   and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ke_sachnr_satz = '&SACHNUMMER&' order by Pos
```
