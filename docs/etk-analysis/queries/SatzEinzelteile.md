# SatzEinzelteile SQL Queries

Total queries: 4

## Table Relationships (Co-occurrence)

- w_kompl_einzelteil ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance_allg
- w_kompl_satz ↔ w_ben_gk ↔ w_hgfg
- w_kompl_satz ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance_allg

## LOAD_HG

- Type: SELECT
- Tables: w_kompl_satz, w_ben_gk, w_hgfg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg HG, ben_text Benennung from w_kompl_satz, w_ben_gk, w_hgfg where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and hgfg_produktart = '&PRODUKTART&' and ks_hg = hgfg_hg and hgfg_fg = '00' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by HG
```

## LOAD_SATZ

- Type: SELECT
- Tables: w_kompl_satz, w_teil, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId, teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC from w_kompl_satz inner join w_teil on (ks_sachnr_satz = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and ks_hg = '&HG&' order by HG, UG, Sachnummer
```

## COUNT_EINZELTEILE

- Type: SELECT
- Tables: w_kompl_einzelteil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(ke_sachnr_einzelteil) Anzahl  from w_kompl_einzelteil where ke_sachnr_satz = '&SACHNUMMER&'
```

## LOAD_EINZELTEIL

- Type: SELECT
- Tables: w_kompl_einzelteil, w_teil, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, ke_menge Menge, ke_beziehbar istBeziehbar, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId,  teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC, teil_ist_eba istEBA, ke_pos Pos from w_kompl_einzelteil inner join w_teil on (ke_sachnr_einzelteil = teil_sachnr)   inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')   left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                   and tcp_marke_tps = '&MARKE&'                                    and tcp_produktart = '&PRODUKTART&'                                   and tcp_vbereich in (&KATALOGUMFAENGE&)                                   &TC_CHECK_LANDKUERZEL&                                   and tcp_datum_von <= &DATUM&                                   and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ke_sachnr_satz = '&SACHNUMMER&' order by Pos
```
