# TeilesucheFzg SQL Queries

Total queries: 26

## Table Relationships (Co-occurrence)

- w_ben_gk ↔ w_markt_etk ↔ w_bildtaf_suche ↔ w_bildtaf
- w_ben_gk ↔ w_markt_etk ↔ w_bildtaf_suche ↔ w_bildtaf ↔ w_komm_help ↔ w_komm
- w_bildtaf_suche ↔ w_markt_etk ↔ w_ben_gk ↔ w_bildtaf
- w_btzeilen_verbauung ↔ w_btzeilen ↔ w_bildtaf ↔ w_grafik
- w_btzeilen_verbauung ↔ w_btzeilen ↔ w_bildtaf ↔ w_grafik ↔ w_ben_gk ↔ w_markt_etk
- w_btzeilen_verbauung ↔ w_btzeilen ↔ w_bildtaf ↔ w_teil ↔ w_ben_gk
- w_btzeilen_verbauung ↔ w_markt_etk ↔ w_ben_gk ↔ w_btzeilen ↔ w_bildtaf
- w_hgfg ↔ w_ben_gk ↔ w_fg_thumbnail ↔ w_grafik ↔ w_bildtaf
- w_hgfg_mosp ↔ w_hgfg ↔ w_ben_gk
- w_hgfg_mosp ↔ w_hgfg ↔ w_ben_gk ↔ w_fg_thumbnail ↔ w_grafik ↔ w_bildtaf
- w_hgfg_mosp ↔ w_hgfg ↔ w_ben_gk ↔ w_hg_thumbnail ↔ w_grafik

## RETRIEVE_HGS

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgthb_produktart = hgfg_produktart and hgthb_bereich = hgfg_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where hgfgm_mospid = &MOSP& order by Hauptgruppe
```

## RETRIEVE_HGS_GRAF

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgthb_produktart = hgfg_produktart and hgthb_bereich = hgfg_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where hgfgm_mospid = &MOSP& order by Hauptgruppe
```

## RETRIEVE_FGS_GRAF

- Type: SELECT
- Tables: w_hgfg, w_ben_gk, w_fg_thumbnail, w_grafik, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_fg Funktionsgruppe,ben_text Benennung,fgthb_grafikid GrafikId,grafik_moddate ModStamp from w_hgfg  inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)  where hgfg_hg = '&HG&' AND hgfg_produktart = '&PRODART&' order by Funktionsgruppe
```

## RETRIEVE_FGS_GRAF_MOSP

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk, w_fg_thumbnail, w_grafik, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)  where hgfgm_mospid = &MOSP& AND hgfgm_hg = '&HG&' order by Funktionsgruppe
```

## RETRIEVE_FGS_GRAF_MIT_GRAFIKEN

- Type: SELECT
- Tables: w_hgfg, w_ben_gk, w_fg_thumbnail, w_grafik, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_fg Funktionsgruppe,ben_text Benennung,fgthb_grafikid GrafikId,grafik_moddate ModStamp,grafik_blob Grafik from w_hgfg  inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)  where hgfg_hg = '&HG&' AND hgfg_produktart = '&PRODART&' order by Funktionsgruppe
```

## RETRIEVE_FGS_GRAF_MOSP_MIT_GRAFIKEN

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk, w_fg_thumbnail, w_grafik, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)  where hgfgm_mospid = &MOSP& AND hgfgm_hg = '&HG&' order by Funktionsgruppe
```

## RETRIEVE_HGFGS

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = '&HG&' and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich  and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Hauptgruppe, Funktionsgruppe
```

## RETRIEVE_ALL_HGFGS

- Type: SELECT
- Tables: w_hgfg_mosp, w_hgfg, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union  select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2
```

## SEARCH_BT_BENENNUNG

- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and ben_text like INSENSITIVE '&SUCHSTRING&' and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_ASS& order by Pos
```

## SEARCH_BT_BENENNUNG_SONDERLOCKE

- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_text like INSENSITIVE '&SUCHSTRING2&') and ben_textcode = bildtaf_textc&BEACHTE_SICHER_FLAG_STMT& and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_SONDERLOCKE_ASS& order by Pos
```

## SEARCH_BT_BENENNUNG_TR

- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and upper(replace('i' by  '\u0130' in replace('\u0131' by 'I' in ben_text))) like '&SUCHSTRING&' and ben_textcode = bildtaf_textc&BEACHTE_SICHER_FLAG_STMT& and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_ASS& order by Pos
```

## SEARCH_BT_BEGRIFF

- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT& and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BEGRIFF_ASS& order by Pos
```

## SEARCH_BT_BEGRIFF_NEU

- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf, w_komm_help, w_komm
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT& and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BEGRIFF_ASS& union select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_bte.ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk ben_komm     , w_komm_help     , w_komm     , w_bildtaf_suche     , w_ben_gk ben_bte     , w_bildtaf  left join w_markt_etk    on (marktetk_lkz = bildtaf_lkz)  where ben_komm.ben_iso = '&ISO&'  and ben_komm.ben_regiso = '&REGISO&'  and &BEGRIFFE_BEN_KOMM_STMT&  and komm_textcode = ben_komm.ben_textcode  and kommh_id = komm_id  and kommh_mospid = &MOSP&  and kommh_btnr = bildtaf_btnr  and ben_bte.ben_iso = '&ISO&'  and ben_bte.ben_regiso = '&REGISO&'  and ben_bte.ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = kommh_mospid &LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& order by Pos
```

## SEARCH_SNR_BENENNUNG

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and ben_teil.ben_text like INSENSITIVE '&SUCHSTRING&'&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_BENENNUNG_SONDERLOCKE

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and (ben_teil.ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_teil.ben_text like INSENSITIVE '&SUCHSTRING2&')&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_SONDERLOCKE_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_BENENNUNG_TR

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like '&SUCHSTRING&'&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_BEGRIFF

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&BEACHTE_SICHER_FLAG_STMT&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and &BEGRIFFE_BEN_TEIL_STMT&union  select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      inner join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&BEACHTE_SICHER_FLAG_STMT&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and &BEGRIFFE_BEN_TEIL_KOMM_STMT&&UNION&&SEARCH_SNR_BEGRIFF_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_BT_SACHNUMMER_COMPL

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_markt_etk, w_ben_gk, w_btzeilen, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and btzeilen_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&'&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&UNION&&SEARCH_BT_SACHNUMMER_COMPL_ASS& order by Pos
```

## SEARCH_SNR_SACHNUMMER_INCOMPL

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr like '&SACHNUMMER&'&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&BEACHTE_SICHER_FLAG_STMT& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_FREMDNR

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr IN &FREMDNUMMERN&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&BEACHTE_SICHER_FLAG_STMT& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_BT_SACHNUMMERN

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_markt_etk, w_ben_gk, w_btzeilen, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and btzeilen_btnr = bildtaf_btnr&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&UNION&&SEARCH_BT_SACHNUMMERN_ASS& order by Pos
```

## SEARCH_BT_HGFG

- Type: SELECT
- Tables: w_bildtaf_suche, w_markt_etk, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_bildtaf_suche, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where bildtafs_hg = '&HG&' and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtafs_btnr = bildtaf_btnr&HG/HGFG_STMT&&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'order by Pos
```

## SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = '&ISO&' and BB.ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&'&BEACHTE_SICHER_FLAG_STMT& order by Pos
```

## SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = '&ISO&' and BB.ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&' and bildtaf_hg = '&FG&'&BEACHTE_SICHER_FLAG_STMT& order by Pos
```

## CHECK_BT_HG_GRAFISCH

- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_bildtaf, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select count( bildtaf_btnr) countBte from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&'&BEACHTE_SICHER_FLAG_STMT&
```

## SEARCH_KABELBAUM_CHANGEPOINTS

- Type: SELECT
- Tables: w_btzeilen_cp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct btzeilenc_pos Pos, btzeilenc_typschl Typ, btzeilenc_werk Werk, btzeilenc_art Art, btzeilenc_datum Datum, btzeilenc_vin Vin, btzeilenc_vin_proddatum VinProddatum, btzeilenc_vin_min VinMin, btzeilenc_vin_max VinMax, btzeilenc_nart ArtNummer, btzeilenc_nummer Nummer, btzeilenc_alter CPAlter from w_btzeilen_cp where btzeilenc_mospid = &MOSPID& and btzeilenc_typschl = '&TYPSCHLUESSEL&' and btzeilenc_werk = '&WERK&' and btzeilenc_pos = &POSITION& and btzeilenc_btnr = '&BTNUMMER&'
```
