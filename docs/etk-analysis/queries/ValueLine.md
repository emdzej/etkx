# ValueLine SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_hgfg ↔ w_bildtaf ↔ w_ben_gk ↔ w_bildtaf_suche ↔ w_baureihe ↔ w_fztyp
- w_hgfg ↔ w_bildtaf ↔ w_ben_gk ↔ w_publben ↔ w_bildtaf_suche ↔ w_baureihe ↔ w_fztyp
- w_kompl_satz ↔ w_ben_gk ↔ w_hgfg
- w_kompl_satz ↔ w_teil ↔ w_ben_gk ↔ w_tc_performance_allg
- w_teil ↔ w_teil_marken ↔ w_ben_gk

## LOAD_HGS

- Type: SELECT
- Tables: w_kompl_satz, w_ben_gk, w_hgfg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg HG, ben_text Benennung from w_kompl_satz, w_ben_gk, w_hgfg where ks_marke_tps = '&MARKE&' and ks_ist_valueline = 'J' and ks_produktart IN ('B', '&PRODUKTART&') and hgfg_produktart = '&PRODUKTART&' and ks_hg = hgfg_hg and hgfg_fg = '00' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by HG
```

## LOAD_SAETZE

- Type: SELECT
- Tables: w_kompl_satz, w_teil, w_ben_gk, w_tc_performance_allg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId, teil_ist_reach Reach,  teil_ist_aspg Aspg,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_kompl_satz inner join w_teil on (ks_sachnr_satz = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and ks_hg = '&HG&' and ks_ist_valueline = 'J' order by HG, UG, Sachnummer
```

## LOAD_BTE_BAUREIHEN

- Type: SELECT
- Tables: w_hgfg, w_bildtaf, w_ben_gk, w_bildtaf_suche, w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from  w_hgfg, w_bildtaf, w_ben_gk, w_bildtaf_suche, w_baureihe, w_fztyp where hgfg_bereich = 'KONZERN' and hgfg_produktart = '&PRODUKTART&' and hgfg_ist_valueline = 'J' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and ben_textcode = baureihe_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by POS
```

## LOAD_BILDTAFELN

- Type: SELECT
- Tables: w_hgfg, w_bildtaf, w_ben_gk, w_publben, w_bildtaf_suche, w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, B.ben_text Benennung, fztyp_erwvbez Modell, K.ben_text Karosserie, fztyp_karosserie KarosserieId, baureihe_bauart Bauart, fztyp_ktlgausf Region, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos from  w_hgfg, w_bildtaf, w_ben_gk B, w_publben, w_ben_gk K, w_bildtaf_suche, w_baureihe, w_fztyp where hgfg_ist_valueline = 'J' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = '&BAUREIHE&' and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and B.ben_textcode = bildtaf_textc and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and publben_bezeichnung = fztyp_karosserie and publben_art = 'K' and K.ben_textcode = publben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' order by Pos
```

## LOAD_TEILENUMMERN

- Type: SELECT
- Tables: w_teil, w_teil_marken, w_ben_gk
- Parameterized: no
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text Kommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = 'BMW') inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = 'de' and ben_teil.ben_regiso = '  ') left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '  ' and ben_komm.ben_regiso = '  ') where teil_ist_valueline = 'J' and teil_sachnr = teilm_sachnr and teilm_marke_tps = 'BMW' and teil_verbaubar = 'J' and teil_produktart in ('P', 'B') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```
