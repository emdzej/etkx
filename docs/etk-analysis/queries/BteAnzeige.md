# BteAnzeige SQL Queries

Total queries: 14

## Table Relationships (Co-occurrence)

- w_bildtaf_verweis ↔ w_ben_gk ↔ w_komm ↔ w_bildtaf ↔ w_bildtaf_suche
- w_bildtaf_verweis ↔ w_ben_gk ↔ w_komm ↔ w_bildtaf ↔ w_btzeilenugb_verbauung
- w_bte_bedelem ↔ w_bte_bedog ↔ w_bte_bedgesamt
- w_bte_bedkurz ↔ w_bildtaf ↔ w_bte_bedelem ↔ w_bte_bedog ↔ w_bte_bedgesamt
- w_bte_bedkurz ↔ w_bte_bedueber
- w_btzeilen_verbauung ↔ w_btzeilen ↔ w_teil ↔ w_ben_gk ↔ w_kompl_satz ↔ w_tc_performance ↔ w_grp_information ↔ w_si ↔ w_bildtaf_bnbben
- w_btzeilenugb_verbauung ↔ w_btzeilenugb ↔ w_teil ↔ w_ben_gk ↔ w_kompl_satz ↔ w_tc_performance_allg ↔ w_si ↔ w_bildtaf_bnbben
- w_komm_help ↔ w_ben_gk ↔ w_komm
- w_kommugb_help ↔ w_ben_gk ↔ w_komm
- w_publben ↔ w_ben_gk
- w_tc_sachnummer ↔ w_tc_kampagne_proddatum ↔ w_tc_kampagne

## LOAD_HOTSPOTS
**Description:** Retrieves data from w_grafik_hs filtered by grafikhs_grafikid, grafikhs_art. Used in the BteAnzeige module to support ETK workflows for graphics.


- Type: SELECT
- Tables: w_grafik_hs
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select grafikhs_bildposnr Bildnummer, grafikhs_topleft_x TopLeft_x, grafikhs_topleft_y TopLeft_y, grafikhs_bottomright_x BottomRight_x, grafikhs_bottomright_y BottomRight_y from w_grafik_hs where grafikhs_grafikid = &GRAFIKID& and grafikhs_art = '&ART&'
```

## LOAD_BTZEILEN_FZG
**Description:** Retrieves data from w_btzeilen_verbauung, w_btzeilen, w_teil, w_ben_gk, w_kompl_satz, w_tc_performance, w_grp_information, w_si, w_bildtaf_bnbben filtered by btzeilenv_btnr, btzeilenv_pos, ben_iso, ben_regiso, ks_marke_tps, tcp_sachnr, tcp_datum_von, tcp_datum_bis, grpinfo_sachnr, grpinfo_typ, bildtafb_bildposnr and ordered by Pos, GRP_PA, GRP_HG, GRP_UG, GRP_lfdNr, SI_DokArt. Used in the BteAnzeige module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_teil, w_ben_gk, w_kompl_satz, w_tc_performance, w_grp_information, w_si, w_bildtaf_bnbben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btzeilen_bildposnr Bildnummer, teil_hauptgr Teil_HG, teil_untergrup Teil_UG, teil_sachnr Teil_Sachnummer, tben.ben_text Teil_Benennung, teil_benennzus Teil_Zusatz, teil_entfall_kez Teil_Entfall, teil_textcode_kom Teil_Kommentar_Id, tkben.ben_text Teil_Kommentar, teil_kom_pi Teil_Komm_PI, teil_vorhanden_si Teil_SI, teil_ist_reach Teil_Reach,  teil_ist_aspg Teil_Aspg,  teil_ist_stecker Teil_Stecker,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  si_dokart SI_DokArt, decode(tcp_sachnr, tcp_sachnr, 'C', NULL) Teil_TC, NVL(tcp_proddat_rel, 'N') Teil_TC_ProdDatRelevant, grpinfo_leitaw_pa GRP_PA, grpinfo_leitaw_hg GRP_HG, grpinfo_leitaw_ug GRP_UG, grpinfo_leitaw_nr GRP_lfdNr, btzeilenv_vmenge Menge, btzeilen_kat Kat_KZ, btzeilen_automatik Getriebe_KZ, btzeilen_lenkg Lenkung_KZ, btzeilen_eins Einsatz, btzeilen_auslf Auslauf, btzeilen_bedkez || nvl(to_char(btzeilen_regelnr), '') Bedingung_KZ, btzeilen_kommbt KommBT, btzeilen_kommvor KommVor, btzeilen_kommnach KommNach, ks_sachnr_satz Satz_Sachnummer,  btzeilen_gruppeid GruppeId,  btzeilen_blocknr BlockNr,  bnbben.ben_text BnbBenText, btzeilen_pos Pos, btzeilenv_alter_kz BtZAlter, btzeilen_bedkez_pg Teil_BedkezPG, btzeilenv_bed_art BedingungArt, btzeilenv_bed_alter BedingungAlter from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_teil on (btzeilen_sachnr = teil_sachnr) inner join w_ben_gk tben on (teil_textcode = tben.ben_textcode and tben.ben_iso = '&ISO&' and tben.ben_regiso = '&REGISO&') left join w_kompl_satz on (btzeilen_sachnr = ks_sachnr_satz and ks_marke_tps = '&MARKE&') left join w_tc_performance on (tcp_mospid = &MOSP& and tcp_sachnr = btzeilen_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) left join w_grp_information on (btzeilenv_mospid = grpinfo_mospid and grpinfo_sachnr = btzeilen_sachnr and grpinfo_typ = '&TYP&') left join w_ben_gk tkben on (teil_textcode_kom = tkben.ben_textcode and tkben.ben_iso = '&ISO&' and tkben.ben_regiso = '&REGISO&') left join w_si on (si_sachnr = teil_sachnr) left join w_bildtaf_bnbben on (bildtafb_btnr = btzeilenv_btnr and bildtafb_bildposnr = btzeilen_bildposnr) left join w_ben_gk bnbben on (bildtafb_textcode = bnbben.ben_textcode and bnbben.ben_iso = '&ISO&' and bnbben.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_btnr = '&BTNR&' order by Pos, GRP_PA, GRP_HG, GRP_UG, GRP_lfdNr, SI_DokArt
```

## LOAD_BTZEILEN_CP_FZG
**Description:** Retrieves data from w_btzeilen_cp filtered by btzeilenc_mospid, btzeilenc_btnr and ordered by Pos. Used in the BteAnzeige module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_cp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select distinct btzeilenc_pos Pos, btzeilenc_typschl Typ, btzeilenc_werk Werk, btzeilenc_art Art, btzeilenc_datum Datum, btzeilenc_vin Vin, btzeilenc_vin_proddatum VinProddatum, btzeilenc_vin_min VinMin, btzeilenc_vin_max VinMax, btzeilenc_nart ArtNummer, btzeilenc_nummer Nummer, btzeilenc_alter CPAlter from w_btzeilen_cp where btzeilenc_mospid = &MOSP& and btzeilenc_btnr = '&BTNR&' &CP_FZG_TYP_WERK& order by Pos
```

## LOAD_BTZEILEN_UGB
**Description:** Retrieves data from w_btzeilenugb_verbauung, w_btzeilenugb, w_teil, w_ben_gk, w_kompl_satz, w_tc_performance_allg, w_si, w_bildtaf_bnbben filtered by btzeilenuv_btnr, btzeilenuv_pos, ben_iso, ben_regiso, ks_marke_tps, tcp_marke_tps, tcp_produktart, tcp_vbereich, tcp_datum_von, tcp_datum_bis, bildtafb_bildposnr and ordered by Pos. Used in the BteAnzeige module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb_verbauung, w_btzeilenugb, w_teil, w_ben_gk, w_kompl_satz, w_tc_performance_allg, w_si, w_bildtaf_bnbben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btzeilenu_bildposnr Bildnummer, teil_hauptgr Teil_HG, teil_untergrup Teil_UG, teil_sachnr Teil_Sachnummer, tben.ben_text Teil_Benennung, teil_benennzus Teil_Zusatz, teil_entfall_kez Teil_Entfall, teil_textcode_kom Teil_Kommentar_Id, tkben.ben_text Teil_Kommentar, teil_kom_pi Teil_Komm_PI, teil_vorhanden_si Teil_SI, teil_ist_reach Teil_Reach,  teil_ist_aspg Teil_Aspg,  teil_ist_stecker Teil_Stecker,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  si_dokart SI_DokArt, decode(teil_sachnr, tcp_sachnr, 'C', NULL) Teil_TC, 'N' Teil_TC_ProdDatRelevant, btzeilenu_mmg MMG, btzeilenu_emg EMG, btzeilenu_eins Einsatz, btzeilenu_ausl Auslauf, btzeilenu_kommbt KommBT, btzeilenu_kommvor KommVor, btzeilenu_kommnach KommNach, ks_sachnr_satz Satz_Sachnummer,  0 GruppeId,  0 BlockNr,  bnbben.ben_text BnbBenText, btzeilenu_pos Pos from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_teil on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk tben on (teil_textcode = tben.ben_textcode and tben.ben_iso = '&ISO&' and tben.ben_regiso = '&REGISO&') left join w_kompl_satz on (btzeilenu_sachnr = ks_sachnr_satz and ks_marke_tps = '&MARKE&') left join w_tc_performance_allg on (btzeilenu_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich = '&KATALOGUMFANG&'                                     &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) left join w_ben_gk tkben on (teil_textcode_kom = tkben.ben_textcode and tkben.ben_iso = '&ISO&' and tkben.ben_regiso = '&REGISO&') left join w_si on (si_sachnr = teil_sachnr) left join w_bildtaf_bnbben on (bildtafb_btnr = btzeilenu_btnr and bildtafb_bildposnr = btzeilenu_bildposnr) left join w_ben_gk bnbben on (bildtafb_textcode = bnbben.ben_textcode and bnbben.ben_iso = '&ISO&' and bnbben.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = '&BTNR&' order by Pos
```

## LOAD_KOMMENTARE_FZG
**Description:** Retrieves data from w_komm_help, w_ben_gk, w_komm filtered by kommh_mospid, kommh_btnr, kommh_id, komm_textcode, ben_iso, ben_regiso and ordered by KommId, Pos. Used in the BteAnzeige module to support ETK workflows for comments.


- Type: SELECT
- Tables: w_komm_help, w_ben_gk, w_komm
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct komm_id KommId, ben_text Text, komm_code Code, komm_vz VZ, komm_darstellung Darstellung, komm_tiefe Tiefe, komm_pos Pos from w_komm_help, w_ben_gk, w_komm where kommh_mospid = &MOSP& and kommh_btnr = '&BTNR&' and kommh_id = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos
```

## LOAD_KOMMENTARE_UGB
**Description:** Retrieves data from w_kommugb_help, w_ben_gk, w_komm filtered by kommuh_marke_tps, kommuh_btnr, kommuh_id, komm_textcode, ben_iso, ben_regiso and ordered by KommId, Pos. Used in the BteAnzeige module to support ETK workflows for comments.


- Type: SELECT
- Tables: w_kommugb_help, w_ben_gk, w_komm
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct komm_id KommId, ben_text Text, komm_pos Pos from w_kommugb_help, w_ben_gk, w_komm where kommuh_marke_tps = '&MARKE&' and kommuh_btnr = '&BTNR&' and kommuh_id = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos
```

## LOAD_BEDINGUNGEN_FZG
**Description:** Retrieves data from w_bte_bedkurz, w_bildtaf, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt filtered by btebk_btnr, btebk_mospid, btebk_kez, btebg_btnr, btebg_kez, btebo_btnr, btebo_kez, btebo_ogid, bildtaf_btnr, bildtaf_bedkez and ordered by Kuerzel, OG, Pos. Used in the BteAnzeige module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedkurz, w_bildtaf, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebg_kez Kuerzel, btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bte_bedkurz, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebg_btnr and btebk_kez = btebg_kez  and btebg_btnr = btebo_btnr and btebg_kez = btebo_kez and btebo_btnr = btebe_btnr and btebo_kez = btebe_kez and btebo_ogid = btebe_ogid union select distinct btebg_kez Kuerzel, btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bildtaf, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebg_btnr  and bildtaf_bedkez = btebg_kez and btebg_btnr = btebo_btnr and btebg_kez = btebo_kez and btebo_btnr = btebe_btnr and btebo_kez = btebe_kez and btebo_ogid = btebe_ogid order by Kuerzel, OG, Pos
```

## LOAD_BTE_BEDINGUNGEN_FZG
**Description:** Retrieves data from w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt filtered by btebg_btnr, btebg_kez, btebo_btnr, btebo_kez, btebe_btnr, btebe_kez, btebe_ogid and ordered by OG, Pos. Used in the BteAnzeige module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where btebg_btnr = '&BTNR&' and btebg_kez = '&BEDKEZ&' and btebo_btnr = btebg_btnr and btebo_kez = btebg_kez  and btebe_btnr = btebg_btnr and btebe_kez = btebg_kez and btebe_ogid = btebo_ogid order by OG, Pos
```

## LOAD_UEBERBEDINGUNGEN_FZG
**Description:** Retrieves data from w_bte_bedkurz, w_bte_bedueber filtered by btebk_btnr, btebk_mospid, btebu_btnr, btebu_kez, btebu_kezueber and ordered by Kuerzel, KuerzelUeber. Used in the BteAnzeige module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedkurz, w_bte_bedueber
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebu_kez Kuerzel, btebu_kezueber  KuerzelUeber from w_bte_bedkurz, w_bte_bedueber  where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebu_btnr = btebk_btnr and btebu_kez =  btebk_kez union select distinct btebu_kez Kuerzel, btebu_kezueber  KuerzelUeber from w_bte_bedkurz, w_bte_bedueber  where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebu_btnr = btebk_btnr and btebu_kezueber = btebk_kez order by Kuerzel, KuerzelUeber
```

## LOAD_BTVERWEISE_FZG
**Description:** Retrieves data from w_bildtaf_verweis, w_ben_gk, w_komm, w_bildtaf, w_bildtaf_suche filtered by bv_btnr_von, bildtafs_hg, bildtafs_mospid, bildtafs_btnr, bv_kommid, komm_textcode, ben_iso, ben_regiso, bv_btnr_nach, bildtaf_textc and ordered by Bildtafelnummer, Pos. Used in the BteAnzeige module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf_verweis, w_ben_gk, w_komm, w_bildtaf, w_bildtaf_suche
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bv_btnr_nach Bildtafelnummer, bt.ben_text Ueberschrift, bv.ben_text Text, komm_pos Pos from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_bildtaf_suche where bv_btnr_von = '&BTNR&' and bildtafs_hg = substr(bv_btnr_nach, 1, 2) and bildtafs_mospid = &MOSP& and bildtafs_btnr = bv_btnr_nach and bv_kommid = komm_id and komm_textcode = bv.ben_textcode and bv.ben_iso = '&ISO&' and bv.ben_regiso = '&REGISO&' and bv_btnr_nach = bildtaf_btnr and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' order by Bildtafelnummer, Pos
```

## LOAD_BTVERWEISE_UGB
**Description:** Retrieves data from w_bildtaf_verweis, w_ben_gk, w_komm, w_bildtaf, w_btzeilenugb_verbauung filtered by bv_btnr_von, bv_btnr_nach, btzeilenuv_marke_tps, bv_kommid, komm_textcode, ben_iso, ben_regiso, bildtaf_produktart, bildtaf_textc and ordered by Bildtafelnummer, Pos. Used in the BteAnzeige module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf_verweis, w_ben_gk, w_komm, w_bildtaf, w_btzeilenugb_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bv_btnr_nach Bildtafelnummer, bt.ben_text Ueberschrift, bv.ben_text Text, komm_pos Pos from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_btzeilenugb_verbauung where bv_btnr_von = '&BTNR&' and bv_btnr_nach = btzeilenuv_btnr and btzeilenuv_marke_tps = '&MARKE&' and bv_kommid = komm_id and komm_textcode = bv.ben_textcode and bv.ben_iso = '&ISO&' and bv.ben_regiso = '&REGISO&' and bv_btnr_nach = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' order by Bildtafelnummer, Pos
```

## LOAD_JA_NEIN_TEXT
**Description:** Retrieves data from w_publben, w_ben_gk filtered by publben_art, publben_textcode, ben_iso, ben_regiso and ordered by Bezeichnung. Used in the BteAnzeige module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct publben_bezeichnung Bezeichnung, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'V' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Bezeichnung
```

## LOAD_AZEICHEN
**Description:** Retrieves data from w_teil_atb filtered by teilatb_sachnr_alt, teilatb_sachnr_neu. Used in the BteAnzeige module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil_atb
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct teilatb_kennz ATB,              teilatb_bap BAP from w_teil_atb where teilatb_sachnr_alt = '&SNR1&' and teilatb_sachnr_neu = '&SNR2&'
```

## LOAD_ANZAHL_REL_KAMPAGNEN
**Description:** Retrieves data from w_tc_sachnummer, w_tc_kampagne_proddatum, w_tc_kampagne filtered by tckp_mospid, tckp_proddatum_von, 99999999, tcs_id, tcs_sachnr, tck_id. Used in the BteAnzeige module to support ETK workflows for technical campaigns.


- Type: SELECT
- Tables: w_tc_sachnummer, w_tc_kampagne_proddatum, w_tc_kampagne
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select Count(*) Anzahl from w_tc_sachnummer, w_tc_kampagne_proddatum, w_tc_kampagne where tckp_mospid = &MOSPID& and tckp_proddatum_von <= &PRODDATUM_MAX& and nvl(tckp_proddatum_bis, 99999999) >= &PRODDATUM_MIN& and tcs_id = tckp_id and tcs_sachnr = '&SACHNUMMER&' and tck_id = tcs_id &TC_CHECK_LANDKUERZEL_KAMPAGNE&
```
