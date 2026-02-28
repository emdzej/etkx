# TeilesucheAss SQL Queries

Total queries: 20

## Table Relationships (Co-occurrence)

- w_btzeilenugb_verbauung ↔ w_bildtaf ↔ w_hgfg ↔ w_ben_gk ↔ w_hg_thumbnail ↔ w_grafik
- w_btzeilenugb_verbauung ↔ w_btzeilenugb ↔ w_bildtaf ↔ w_grafik
- w_btzeilenugb_verbauung ↔ w_btzeilenugb ↔ w_bildtaf ↔ w_grafik ↔ w_ben_gk ↔ w_markt_etk
- w_btzeilenugb_verbauung ↔ w_btzeilenugb ↔ w_bildtaf ↔ w_teil ↔ w_ben_gk
- w_btzeilenugb_verbauung ↔ w_hgfg ↔ w_ben_gk ↔ w_bildtaf
- w_btzeilenugb_verbauung ↔ w_markt_etk ↔ w_ben_gk ↔ w_bildtaf
- w_btzeilenugb_verbauung ↔ w_markt_etk ↔ w_ben_gk ↔ w_btzeilenugb ↔ w_bildtaf

## RETRIEVE_HGS
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_bildtaf, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik filtered by btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, hgfg_fg, bildtaf_bereich, ben_iso, ben_regiso, hgthb_marke_tps, hgfg_produktart, hgfg_bereich, grafik_art and ordered by Hauptgruppe. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_bildtaf, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp from w_btzeilenugb_verbauung inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')) inner join w_hgfg on (bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgfg_produktart = hgthb_produktart and hgfg_bereich = hgthb_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' order by Hauptgruppe
```

## RETRIEVE_HGS_GRAF
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_bildtaf, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik filtered by btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, hgfg_fg, bildtaf_bereich, ben_iso, ben_regiso, hgthb_marke_tps, hgfg_produktart, hgfg_bereich, grafik_art and ordered by Hauptgruppe. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_bildtaf, w_hgfg, w_ben_gk, w_hg_thumbnail, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_btzeilenugb_verbauung inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')) inner join w_hgfg on (bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgfg_produktart = hgthb_produktart and hgfg_bereich = hgthb_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' order by Hauptgruppe
```

## RETRIEVE_HGFGS
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf filtered by btzeilenuv_marke_tps, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_hg, bildtaf_fg, bildtaf_bereich, hgfg_textcode, ben_iso, ben_regiso and ordered by Hauptgruppe, Funktionsgruppe. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr  and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Hauptgruppe, Funktionsgruppe
```

## RETRIEVE_ALL_HGFGS
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf filtered by btzeilenuv_marke_tps, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_hg, hgfg_fg, bildtaf_bereich, hgfg_textcode, ben_iso, ben_regiso, bildtaf_fg and ordered by 1, 2. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union  select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2
```

## SEARCH_BT_BENENNUNG
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso, ben_text. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and ben_text like INSENSITIVE '&SUCHSTRING&'&ORDER_BY_POS&
```

## SEARCH_BT_BENENNUNG_SONDERLOCKE
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso, ben_text. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_text like INSENSITIVE '&SUCHSTRING2&')&ORDER_BY_POS&
```

## SEARCH_BT_BENENNUNG_TR
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso, SUCHSTR. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_text))) like '&SUCHSTRING&'&ORDER_BY_POS&
```

## SEARCH_BT_BEGRIFF
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT&&ORDER_BY_POS&
```

## SEARCH_SNR_BENENNUNG
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, ben_text. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and ben_teil.ben_text like INSENSITIVE '&SUCHSTRING&'&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&
```

## SEARCH_SNR_BENENNUNG_SONDERLOCKE
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, ben_text. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and (ben_teil.ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_teil.ben_text like INSENSITIVE '&SUCHSTRING2&')&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&
```

## SEARCH_SNR_BENENNUNG_TR
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, SUCHSTR. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like '&SUCHSTRING&'&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&
```

## SEARCH_SNR_BEGRIFF
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, dist. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung      inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)      inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr)       inner join w_teil         on (btzeilenu_sachnr = teil_sachnr)       inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and &BEGRIFFE_BEN_TEIL_STMT&union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung      inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)      inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr)       inner join w_teil         on (btzeilenu_sachnr = teil_sachnr)       inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and &BEGRIFFE_BEN_TEIL_KOMM_STMT&&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&
```

## SEARCH_BT_SACHNUMMER_COMPL
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, btzeilenuv_pos, btzeilenu_sachnr, btzeilenu_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_sachnr = '&SACHNUMMER&' and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' &ORDER_BY_POS&
```

## SEARCH_SNR_SACHNUMMER_INCOMPL
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, btzeilenu_sachnr, bildtaf_produktart, bildtaf_vbereich and ordered by Benennung, Hauptgruppe, Untergruppe, Sachnummer. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenu_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenu_sachnr like '&SACHNUMMER&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_FREMDNR
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, btzeilenu_sachnr, bildtaf_produktart, bildtaf_vbereich and ordered by Benennung, Hauptgruppe, Untergruppe, Sachnummer. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenu_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenu_sachnr IN &FREMDNUMMERN& and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_BT_SACHNUMMERN
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, btzeilenuv_pos, btzeilenu_sachnr, btzeilenu_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_sachnr IN (&SACHNUMMERN&) and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&ORDER_BY_POS&
```

## SEARCH_BT_HGFG
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf filtered by marktetk_lkz, btzeilenuv_btnr, btzeilenuv_pos, btzeilenu_btnr, bildtaf_produktart, bildtaf_vbereich, bildtaf_textc, ben_iso, ben_regiso and ordered by Pos. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_markt_etk, w_ben_gk, w_btzeilenugb, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')&HG/HGFG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk filtered by btzeilenuv_btnr, btzeilenuv_pos, grafik_art, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, bildtaf_hg and ordered by Pos. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' order by Pos
```

## SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk filtered by btzeilenuv_btnr, btzeilenuv_pos, grafik_art, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_vbereich, bildtaf_hg, bildtaf_fg and ordered by Pos. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik, w_ben_gk, w_markt_etk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, grafik_blob Grafik, bildtaf_bedkez BedingungKZ, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' and bildtaf_fg = '&FG&' order by Pos
```

## CHECK_BT_HG_GRAFISCH
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik filtered by btzeilenuv_btnr, btzeilenuv_pos, grafik_art, bildtaf_produktart, bildtaf_vbereich, bildtaf_hg. Used in the TeilesucheAss module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_bildtaf, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select count(bildtaf_btnr) countBte from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&'
```
