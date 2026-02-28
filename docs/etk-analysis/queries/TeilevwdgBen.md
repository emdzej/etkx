# TeilevwdgBen SQL Queries

Total queries: 10

## Table Relationships (Co-occurrence)

- w_baureihe ↔ w_ben_gk ↔ w_bildtaf ↔ w_bildtaf_marke ↔ w_bildtaf_suche ↔ w_fztyp
- w_baureihe ↔ w_fztyp ↔ w_ben_gk ↔ w_btzeilen_verbauung
- w_baureihe ↔ w_fztyp ↔ w_bildtaf_suche ↔ w_bildtaf ↔ w_ben_gk
- w_ben_gk ↔ w_markt_etk ↔ w_bildtaf ↔ w_btzeilen ↔ w_btzeilen_verbauung ↔ w_fztyp ↔ w_baureihe
- w_ben_gk ↔ w_markt_etk ↔ w_bildtaf_marke ↔ w_bildtaf_suche ↔ w_fztyp ↔ w_baureihe ↔ w_publben ↔ w_bildtaf
- w_ben_gk ↔ w_markt_etk ↔ w_bildtaf_suche ↔ w_bildtaf_marke ↔ w_fztyp ↔ w_baureihe ↔ w_publben ↔ w_bildtaf
- w_bildtaf_marke ↔ w_markt_etk ↔ w_bildtaf_suche ↔ w_fztyp ↔ w_baureihe ↔ w_ben_gk ↔ w_publben ↔ w_btzeilen_verbauung ↔ w_bildtaf
- w_btzeilen_verbauung ↔ w_btzeilenugb ↔ w_ben_gk ↔ w_teil_marken ↔ w_teil

## RETRIEVE_BAUREIHEN
**Description:** Retrieves data from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk filtered by SUCHSTR, ben_iso, ben_regiso, bildtaf_textc, bildtafs_btnr, bildtafs_hg, fztyp_mospid, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, baureihe_baureihe, baureihe_marke_tps, baureihe_produktart, ben_textcode and ordered by POS. Used in the TeilevwdgBen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr where &SUCHSTRING& and H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and bildtaf_textc = H.ben_textcode and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' order by POS
```

## RETRIEVE_BAUREIHEN_SONDERLOCKE
**Description:** Retrieves data from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk filtered by SUCHSTR, ben_iso, ben_regiso, bildtaf_textc, bildtafs_btnr, bildtafs_hg, fztyp_mospid, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, baureihe_baureihe, baureihe_marke_tps, baureihe_produktart, ben_textcode and ordered by POS. Used in the TeilevwdgBen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr where (&SUCHSTRING1& or &SUCHSTRING2&) and H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and bildtaf_textc = H.ben_textcode and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' order by POS
```

## RETRIEVE_BAUREIHEN_TR
**Description:** Retrieves data from w_baureihe, w_ben_gk, w_bildtaf, w_bildtaf_marke, w_bildtaf_suche, w_fztyp filtered by ben_textcode, ben_iso, ben_regiso, bildtaf_vbereich, bildtaf_produktart, bildtafm_marke_tps, bildtafs_btnr, SUCHSTR and ordered by POS. Used in the TeilevwdgBen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_ben_gk, w_bildtaf, w_bildtaf_marke, w_bildtaf_suche, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe inner join w_ben_gk on (ben_textcode = baureihe_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')\twhere baureihe_baureihe IN (select distinct fztyp_baureihe from w_ben_gk H\tinner join w_bildtaf on (bildtaf_textc = ben_textcode and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&')\tinner join w_bildtaf_marke on (bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&') inner join w_bildtaf_suche on (bildtafs_hg = bildtaf_hg and bildtafs_btnr = bildtaf_btnr) inner join w_fztyp on (fztyp_mospid = bildtafs_mospid\tand fztyp_vbereich = '&KATALOGUMFANG&'\tand fztyp_sichtschutz = 'N'\tand fztyp_ktlgausf IN (&REGIONEN&)) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (&SUCHSTRING&) order by POS
```

## RETRIEVE_MODELLSPALTEN
**Description:** Retrieves data from w_ben_gk, w_markt_etk, w_bildtaf_marke, w_bildtaf_suche, w_fztyp, w_baureihe, w_publben, w_bildtaf filtered by marktetk_lkz, ben_regiso, SUCHSTR, ben_textcode, bildtaf_vbereich, bildtaf_produktart, bildtafm_btnr, bildtafm_marke_tps, bildtaf_hg, bildtaf_btnr, bildtafs_mospid, fztyp_baureihe, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_karosserie, publben_art, publben_textcode, ben_iso, bildtaf_textc and ordered by MODELL, BTNR, KAROSSERIE, REGION. Used in the TeilevwdgBen module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_marke, w_bildtaf_suche, w_fztyp, w_baureihe, w_publben, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk H, w_bildtaf_marke, w_bildtaf_suche,      w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and &SUCHSTRING& and H.ben_textcode = bildtaf_textc and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' &MODELLSPALTEN_STMT&  order by MODELL, BTNR, KAROSSERIE, REGION
```

## RETRIEVE_MODELLSPALTEN_SONDERLOCKE
**Description:** Retrieves data from w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf_marke, w_fztyp, w_baureihe, w_publben, w_bildtaf filtered by marktetk_lkz, ben_regiso, SUCHSTR, ben_textcode, bildtaf_vbereich, bildtaf_produktart, bildtafm_btnr, bildtafm_marke_tps, bildtaf_hg, bildtaf_btnr, bildtafs_mospid, fztyp_baureihe, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_karosserie, publben_art, publben_textcode, ben_iso, bildtaf_textc and ordered by MODELL, BTNR, KAROSSERIE, REGION. Used in the TeilevwdgBen module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf_suche, w_bildtaf_marke, w_fztyp, w_baureihe, w_publben, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk H, w_bildtaf_suche, w_bildtaf_marke,      w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&) and H.ben_textcode = bildtaf_textc and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' &MODELLSPALTEN_STMT&  order by MODELL, BTNR, KAROSSERIE, REGION
```

## RETRIEVE_BAUREIHEN_TNR
**Description:** Retrieves data from w_baureihe, w_fztyp, w_ben_gk, w_btzeilen_verbauung filtered by btzeilenv_sachnr, fztyp_mospid, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, baureihe_baureihe, baureihe_marke_tps, baureihe_produktart, ben_textcode, ben_iso, ben_regiso and ordered by POS. Used in the TeilevwdgBen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp, w_ben_gk, w_btzeilen_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_ben_gk benbr, w_btzeilen_verbauung where btzeilenv_sachnr IN (&SACHNUMMERN&) and fztyp_mospid = btzeilenv_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by POS
```

## RETRIEVE_MODELLSPALTEN_TNR
**Description:** Retrieves data from w_bildtaf_marke, w_markt_etk, w_bildtaf_suche, w_fztyp, w_baureihe, w_ben_gk, w_publben, w_btzeilen_verbauung, w_bildtaf filtered by marktetk_lkz, btzeilenv_btnr, bildtaf_vbereich, bildtaf_produktart, bildtafm_btnr, bildtafm_marke_tps, bildtaf_hg, bildtaf_btnr, bildtafs_mospid, fztyp_baureihe, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_karosserie, publben_art, publben_textcode, ben_iso, ben_regiso, bildtaf_textc and ordered by MODELL, BTNR, KAROSSERIE, REGION. Used in the TeilevwdgBen module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf_marke, w_markt_etk, w_bildtaf_suche, w_fztyp, w_baureihe, w_ben_gk, w_publben, w_btzeilen_verbauung, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btzeilenv_vmenge MENGE, bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_bildtaf_marke, w_bildtaf_suche, w_fztyp,       w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_btzeilen_verbauung, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = bildtaf_btnr and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and bildtaf_btnr = btzeilenv_btnr and bildtafs_mospid = btzeilenv_mospid and btzeilenv_alter_kz is null order by MODELL, BTNR, KAROSSERIE, REGION
```

## SEARCH_SNR_TVBENENNUNG
**Description:** Retrieves data from w_btzeilen_verbauung, w_btzeilenugb, w_ben_gk, w_teil_marken, w_teil filtered by ben_textcode, ben_iso, ben_regiso, SUCHSTR, teilm_marke_tps, teil_verbaubar, teil_produktart, btzeilenv_sachnr, dist, btzeilenu_sachnr and ordered by Benennung, Hauptgruppe, Untergruppe, Sachnummer. Used in the TeilevwdgBen module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilenugb, w_ben_gk, w_teil_marken, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung, w_teil_marken, w_teil  inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and &SUCHSTRING&) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom  and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenv_sachnr = teil_sachnr and btzeilenv_alter_kz is null union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb, w_teil_marken, w_teil inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and &SUCHSTRING&) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenu_sachnr = teil_sachnr order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_SNR_TVBENENNUNG_SONDERLOCKE
**Description:** Retrieves data from w_btzeilen_verbauung, w_btzeilenugb, w_ben_gk, w_teil_marken, w_teil filtered by ben_textcode, ben_iso, ben_regiso, SUCHSTR, teilm_marke_tps, teil_verbaubar, teil_produktart, btzeilenv_sachnr, dist, btzeilenu_sachnr and ordered by Benennung, Hauptgruppe, Untergruppe, Sachnummer. Used in the TeilevwdgBen module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilenugb, w_ben_gk, w_teil_marken, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz,  ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung, w_teil_marken, w_teil  inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&)) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom  and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenv_sachnr = teil_sachnr and btzeilenv_alter_kz is null  union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb, w_teil_marken, w_teil inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&)) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenu_sachnr = teil_sachnr order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```

## SEARCH_BT_SACHNUMMERN_TVBEN
**Description:** Retrieves data from w_ben_gk, w_markt_etk, w_bildtaf, w_btzeilen, w_btzeilen_verbauung, w_fztyp, w_baureihe filtered by marktetk_lkz, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_baureihe, baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, btzeilenv_sachnr, btzeilenv_btnr, btzeilenv_pos, btzeilen_btnr, bildtaf_textc, ben_iso, ben_regiso and ordered by Pos. Used in the TeilevwdgBen module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_markt_etk, w_bildtaf, w_btzeilen, w_btzeilen_verbauung, w_fztyp, w_baureihe
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from  w_ben_gk, w_bildtaf, w_btzeilen, w_btzeilen_verbauung, w_fztyp, w_baureihe left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid =  fztyp_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos and btzeilen_btnr = bildtaf_btnr and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' &UNION& &SEARCH_BT_SACHNUMMERN_ASS& order by Pos
```
