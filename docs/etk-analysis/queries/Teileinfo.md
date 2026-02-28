# Teileinfo SQL Queries

Total queries: 8

## Table Relationships (Co-occurrence)

- w_komm ↔ w_ben_gk
- w_publben ↔ w_ben_gk
- w_tc_sachnummer ↔ w_tc_kampagne
- w_teil ↔ w_ben_gk ↔ w_teil_marken
- w_teil ↔ w_ben_gk ↔ w_teil_marken ↔ w_normnummer ↔ w_eu_reifen
- w_teil_reach ↔ w_teil

## RETRIEVE_TEILEINFO
**Description:** Retrieves data from w_teil, w_ben_gk, w_teil_marken, w_normnummer, w_eu_reifen filtered by teil_textcode, ben_iso, ben_regiso, teilm_marke_tps. Used in the Teileinfo module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil, w_ben_gk, w_teil_marken, w_normnummer, w_eu_reifen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil.teil_hauptgr Hg, teil.teil_untergrup Ug, teil.teil_sachnr SachNr, teil_marke.teilm_marke_tps Marke,  ben.ben_text Benennung, benkom.ben_text BenKom, teil.teil_benennzus BenZusatz, teil.teil_art Art, teiltausch.teil_hauptgr TauschHg, teiltausch.teil_untergrup TauschUg, teil.teil_tausch AustauschTNr, teilalt.teil_hauptgr AltHg, teilalt.teil_untergrup AltUg, teil.teil_alt TNrAlt, teil.teil_austausch_alt Austauschbar, NULL BenAustausch, teil.teil_technik TST, teil.teil_dispo TSD, teil.teil_mengeeinh Mengeneinheit, NULL BenMengeneinheit, teil.teil_produktkl Produktklasse, teil.teil_rundung RundungsKz, teil.teil_lkz LokalTeilKz, teil.teil_vorverpac VVM, teil.teil_lagerverp LVM, teil.teil_beh_verp BVM, teil.teil_teile_gew Gewicht, nn_art Normart, teil.teil_normnummer DIN, teil.teil_fertigungshinweis Fertigungshinweis, teil.teil_kom_pi ZusatzinfoKomId, teil.teil_recycling_kez RecyclKz, teil.teil_produktart Produktart, teil.teil_verbaubar Verbaubar,  teil.teil_ist_reach Reach,  teil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  w_eu_reifen.reifen_kraftstoff,  w_eu_reifen.reifen_nasshaftung,  w_eu_reifen.reifen_rollgeraeusch_stufe,  w_eu_reifen.reifen_rollgeraeusch_wert  from w_teil teil inner join w_ben_gk ben on (teil.teil_textcode = ben.ben_textcode and ben.ben_iso = '&ISO&' and  ben.ben_regiso = '&REGISO&')  inner join w_teil_marken teil_marke on (teil_marke.teilm_sachnr = teil.teil_sachnr and  teil_marke.teilm_marke_tps IN (&MARKEN&) ) left join w_ben_gk benkom on (teil.teil_textcode_kom = benkom.ben_textcode and benkom.ben_iso = '&ISO&' and  benkom.ben_regiso = '&REGISO&')  left join w_teil teilalt on (teil.teil_alt = teilalt.teil_sachnr) left join w_teil teiltausch on (teil.teil_tausch = teiltausch.teil_sachnr) left join w_normnummer on (teil.teil_normnummer = nn_nnid) left join w_eu_reifen on (teil.teil_sachnr = w_eu_reifen.reifen_sachnr) where teil.teil_sachnr = '&SACHNR&'  &HGUG_STMT&
```

## RETRIEVE_TEILEINFO_SERVICEINFO
**Description:** Retrieves data from w_si filtered by si_sachnr. Used in the Teileinfo module to support ETK workflows for service information.


- Type: SELECT
- Tables: w_si
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select si_dokart DokArt, si_doknr DokNr from w_si where si_sachnr = '&SACHNR&'
```

## RETRIEVE_TEILEINFO_PRODUKTINFO
**Description:** Retrieves data from w_komm, w_ben_gk filtered by komm_id, komm_textcode, ben_iso, ben_regiso and ordered by Pos. Used in the Teileinfo module to support ETK workflows for comments.


- Type: SELECT
- Tables: w_komm, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select ben_text Ben, komm_pos Pos from w_komm, w_ben_gk where komm_id = &KOMID& and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## RETRIEVE_TEILEINFO_REACHINFO
**Description:** Retrieves data from w_teil_reach, w_teil filtered by teilreach_sachnr and ordered by casNr. Used in the Teileinfo module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil_reach, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilreach_casnr casNr, teilreach_casname casName, teilreach_gewanteil Gewichtsanteil from w_teil_reach, w_teil where teilreach_sachnr = '&SACHNR&'   and teilreach_sachnr=teil_sachnr  order by casNr
```

## RETRIEVE_BEN_ZU_KUERZEL
**Description:** Retrieves data from w_publben, w_ben_gk filtered by publben_art, publben_bezeichnung, ben_textcode, ben_iso, ben_regiso. Used in the Teileinfo module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select ben_text Ben from w_publben, w_ben_gk where publben_art = '&ART&' and publben_bezeichnung = '&KUERZEL&' and  ben_textcode = publben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&'
```

## RETRIEVE_TEILEINFO_TEILECLEARING
**Description:** Retrieves data from w_tc_sachnummer, w_tc_kampagne filtered by tcs_sachnr, tcs_id, tck_marke_tps, tck_produktart, tck_vbereich, tck_datum_von, tck_datum_bis and ordered by Land, Marke, Produktart, VBereich, Id, Pos. Used in the Teileinfo module to support ETK workflows for technical campaigns.


- Type: SELECT
- Tables: w_tc_sachnummer, w_tc_kampagne
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct tck_id Id, tck_landkuerzel Land, tck_marke_tps Marke, tck_produktart Produktart, tck_vbereich VBereich, tck_baureihen Baureihen, tck_motoren Motoren, tck_baureihen_proddat_von DatumVon, tck_baureihen_proddat_bis DatumBis, tck_pos Pos from w_tc_sachnummer, w_tc_kampagne where tcs_sachnr = '&SACHNR&'   and tcs_id = tck_id    and tck_marke_tps in (&MARKEN&)   and tck_produktart in (&PRODUKTARTEN&)   and tck_vbereich in (&KATALOGUMFAENGE&)   and tck_datum_von <= &DATUM&   &TC_CHECK_LANDKUERZEL&   and (tck_datum_bis is null or tck_datum_bis >= &DATUM&) order by Land, Marke, Produktart DESC, VBereich DESC, Id, Pos
```

## RETRIEVE_HGUG
**Description:** Retrieves data from w_teil filtered by teil_sachnr. Used in the Teileinfo module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teil_hauptgr Hg, teil_untergrup Ug from w_teil where teil_sachnr = '&SACHNR&'
```

## SEARCH_SNR_FREMDNR
**Description:** Retrieves data from w_teil, w_ben_gk, w_teil_marken filtered by teil_textcode, ben_iso, ben_regiso and ordered by Benennung, Hauptgruppe, Untergruppe, Sachnummer. Used in the Teileinfo module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil, w_ben_gk, w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, teilm_marke_tps Marke,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk ben_teil on ( teil_textcode = ben_teil.ben_textcode and  ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left join w_ben_gk ben_komm on ( teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') inner join w_teil_marken teil_marke on ( teilm_sachnr = teil_sachnr) where teil_sachnr IN &SACHNUMMERN& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
```
