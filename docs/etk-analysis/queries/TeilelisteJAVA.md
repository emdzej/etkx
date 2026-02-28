# TeilelisteJAVA SQL Queries

Total queries: 56

## Table Relationships (Co-occurrence)

- w_teileliste ↔ w_auftrag
- w_teileliste ↔ w_auftrag ↔ w_user ↔ w_filiale
- w_teilelistepos ↔ w_preise
- w_teilelistepos ↔ w_teil ↔ w_ben_gk ↔ w_teil_marken
- w_teilelistepos ↔ w_teileinfo ↔ w_teil ↔ w_teil_marken

## RETRIEVE_LISTEN_IDS
**Description:** Retrieves data from w_teileliste filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_user_id, teileliste_marke, teileliste_gesperrt_von. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teileliste_marke Marke,  teileliste_filiale_id Filiale, teileliste_user_id Eigentuemer, teileliste_id ListeId, teileliste_rr_sap_status RrSapStatus from w_teileliste@etk_nutzer where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_user_id = '&NUTZER&' and teileliste_marke IN (&MARKEN&) and teileliste_gesperrt IS NULL and (teileliste_gesperrt_von IS NULL or teileliste_gesperrt_von = teileliste_user_id)
```

## RETRIEVE_BESTELLLISTEN_IDS
**Description:** Retrieves data from w_bestellliste filtered by bestellliste_firma_id, bestellliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select bestellliste_liste_id ListeId from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&'
```

## RETRIEVE_UEBERSICHT
**Description:** Retrieves data from w_teileliste, w_auftrag, w_user, w_filiale filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_auftragsnr, user_id, user_firma_id, filiale_firma_id, filiale_id and ordered by UserId, Gesperrt, Auftragsnr, Erzeugt, ListeId. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste, w_auftrag, w_user, w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teileliste_id ListeId, teileliste_user_id UserId, user_name UserName, teileliste_filiale_id Filiale, filiale_bezeichnung FilialeBen, teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, NVL(teileliste_gesperrt, 'N') Gesperrt, NVL(teileliste_privat, 'N') Privat, teileliste_rr_sap_status RrSapStatus, teileliste_auftragsnr Auftragsnr, auftrag_kundennr Kundennr from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr), w_user@etk_nutzer, w_filiale@etk_nutzer where teileliste_firma_id = '&FIRMA&' and &FILTER& and user_id = teileliste_user_id and user_firma_id = teileliste_firma_id and filiale_firma_id = teileliste_firma_id and filiale_id = teileliste_filiale_id order by UserId, Gesperrt, Auftragsnr, Erzeugt DESC, ListeId
```

## RETRIEVE_UEBERSICHT_BESTELLLISTEN
**Description:** Retrieves data from w_bestellliste filtered by bestellliste_firma_id, bestellliste_filiale_id and ordered by ListeId. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select bestellliste_liste_id ListeId, bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' order by ListeId
```

## RETRIEVE_LISTE_ALLG
**Description:** Retrieves data from w_teileliste, w_auftrag filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_auftragsnr, teileliste_id, teileliste_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste, w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, teileliste_geaendert Geaendert, teileliste_marke Marke, teileliste_auftragsnr Auftragsnr, teileliste_auftragsnr_lokal AuftragsnrLokal, teileliste_kundennr_lokal KundennrLokal, teileliste_privat Privat, teileliste_gesperrt_von GesperrtVon, teileliste_gesperrt_am GesperrtAm, teileliste_fzgdurchlauf DurchlaufId, teileliste_dringlichkeit Dringlichkeit, teileliste_vin Vin, teileliste_rr_sap_status RrSapStatus, auftrag_kundennr Kundennr, auftrag_kundenname Kundenname, auftrag_fgstnr FgStNr from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr) where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'
```

## RETRIEVE_BESTELLLISTE_ALLG
**Description:** Retrieves data from w_bestellliste filtered by bestellliste_firma_id, bestellliste_filiale_id, bestellliste_liste_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' and bestellliste_liste_id = '&ID&'
```

## RETRIEVE_LISTE_POS
**Description:** Retrieves data from w_teilelistepos, w_teileinfo, w_teil, w_teil_marken filtered by teilelistepos_sachnr, teilelistepos_user_id, teileinfo_allgemein, teileinfo_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_m, teilelistepos_lokalteil and ordered by 4, 5, 3. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teilelistepos, w_teileinfo, w_teil, w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilelistepos_sachnr SachNr, teilelistepos_hgug HgUg, teilelistepos_position Pos, teilelistepos_job_id JobId, teilelistepos_srp_id SrpId, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, teilelistepos_benennung Benennung, teilelistepos_zusatz Zusatz, teilelistepos_lagerbestand Lagerbestand, teilelistepos_minimalbestand Minimalbestand, teilelistepos_bedarfshinweis Bedarfshinweis, teilelistepos_lagerort Lagerort, teilelistepos_aume AuMe, teilelistepos_preis Preis, teilelistepos_rabatt Rabatt, teilelistepos_split Splitt, teilelistepos_transparenz Transparenz, teilelistepos_suffix Suffix, teilelistepos_dispocode Dispocode, teilelistepos_ruecksendepfl Ruecksendepflicht, teilelistepos_mwst MwSt, teilelistepos_altteil_steuer AltteilSteuer, teilelistepos_lokalteil Lokalteil, teilelistepos_fistring FiString, teilelistepos_status Status, teilelistepos_pruefen Pruefen, teilelistepos_lock lockflag, teilelistepos_typ AspgTyp, teilelistepos_ref AspgRef, teilelistepos_menge_org AspgMenge, teilm_marke_tps Marke, teil_art Teileart, teil_produktkl ProduktKlasse, teil_mam MAM, NVL(teil_fertigungshinweis, '') FH, teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM, NVL(teil_lagerverp, 0) LVM, NVL(teil_beh_verp, 0) BVM, teileinfo_sachnr SachnrMitNotiz from w_teilelistepos@etk_nutzer left join w_teileinfo@etk_nutzer on (teilelistepos_sachnr = teileinfo_sachnr and (teilelistepos_user_id = teileinfo_user_id OR teileinfo_allgemein = 'J') and teileinfo_firma_id = teilelistepos_firma_id) left join w_teil@etk_publ on (teilelistepos_sachnr = teil_sachnr) inner join w_teil_marken@etk_publ on (teil_sachnr = teilm_sachnr) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and NVL(teilelistepos_lokalteil, 'N') = 'N' union select teilelistepos_sachnr SachNr, teilelistepos_hgug HgUg, teilelistepos_position Pos, teilelistepos_job_id JobId, teilelistepos_srp_id SrpId, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, teilelistepos_benennung Benennung, teilelistepos_zusatz Zusatz, teilelistepos_lagerbestand Lagerbestand, teilelistepos_minimalbestand Minimalbestand, teilelistepos_bedarfshinweis Bedarfshinweis, teilelistepos_lagerort Lagerort, teilelistepos_aume AuMe, teilelistepos_preis Preis, teilelistepos_rabatt Rabatt, teilelistepos_split Splitt, teilelistepos_transparenz Transparenz, teilelistepos_suffix Suffix, teilelistepos_dispocode Dispocode, teilelistepos_ruecksendepfl Ruecksendepflicht, teilelistepos_mwst MwSt, teilelistepos_altteil_steuer AltteilSteuer, teilelistepos_lokalteil Lokalteil, teilelistepos_fistring FiString, teilelistepos_status Status, teilelistepos_pruefen Pruefen, teilelistepos_lock lockflag, teilelistepos_typ AspgTyp, teilelistepos_ref AspgRef, teilelistepos_menge_org AspgMenge, '' Marke, '' Teileart, null ProduktKlasse, 0  MAM, null FH, '' Mengeneinheit, 0 VVM, 0 LVM, 0 BVM, null SachnrMitNotiz from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_lokalteil = 'J' order by 4, 5, 3
```

## RETRIEVE_MAX_POS
**Description:** Retrieves data from w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select max (teilelistepos_position) Pos from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'
```

## RETRIEVE_BESTELLLISTE_POS
**Description:** Retrieves data from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id and ordered by Pos. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select bestelllistepos_sachnr SachNr, bestelllistepos_hgug HgUg, bestelllistepos_position Pos, bestelllistepos_menge Menge, bestelllistepos_bemerkung Bemerkung, bestelllistepos_benennung Benennung, bestelllistepos_zusatz Zusatz, bestelllistepos_lagerbestand Lagerbestand, bestelllistepos_minimalbestand Minimalbestand, bestelllistepos_bedarfshinweis Bedarfshinweis, bestelllistepos_lagerort Lagerort, bestelllistepos_aume AuMe, bestelllistepos_auftragsnr AuftragsNr, bestelllistepos_kundennr KundenNr, bestelllistepos_lokalteil Lokalteil from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' order by Pos
```

## RETRIEVE_TEIL_PREISE
**Description:** Retrieves data from w_preise filtered by preise_sachnr, preise_firma. Used in the TeilelisteJAVA module to support ETK workflows for pricing.


- Type: SELECT
- Tables: w_preise
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select NVL(preise_evpreis, 0) EVP, NVL(preise_nachbelastung, 0) Nachbelastung, preise_rabattschluessel RabattSchluessel, preise_preisaenderung Preisanederung, preise_preis_kz PreisKz, NVL(preise_sonderpreis, 0) SNP, preise_sonderpreis_kz SNPKz, preise_mwst MwSt, preise_mwst_code MwStCode, preise_zolltarifnr ZolltarifNr, NVL(preise_nettopreis, 0) NettoPreis from w_preise@etk_preise where preise_sachnr = '&SACHNR&' and preise_firma = '&FIRMA&'
```

## RETRIEVE_TEILELISTE_PREISE
**Description:** Retrieves data from w_teilelistepos, w_preise filtered by teilelistepos_sachnr, teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id and ordered by teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teilelistepos, w_preise
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilelistepos_sachnr Sachnummer_TL, teilelistepos_position Pos, preise_sachnr Sachnummer_Preis, NVL(preise_evpreis, 0) EVP, NVL(preise_nachbelastung, 0) Nachbelastung, preise_rabattschluessel RabattSchluessel, preise_preisaenderung Preisanederung, preise_preis_kz PreisKz, NVL(preise_sonderpreis, 0) SNP, preise_sonderpreis_kz SNPKz, preise_mwst MwSt, preise_mwst_code MwStCode, preise_zolltarifnr ZolltarifNr, NVL(preise_nettopreis, 0) NettoPreis from w_teilelistepos@etk_nutzer left join w_preise@etk_preise on (teilelistepos_sachnr = preise_sachnr and teilelistepos_firma_id = preise_firma) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' order by teilelistepos_position
```

## RETRIEVE_TEILELISTE_PUBLDATEN
**Description:** Retrieves data from w_teilelistepos, w_teil, w_ben_gk, w_teil_marken filtered by teilelistepos_sachnr, ben_iso, ben_regiso, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id and ordered by teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teilelistepos, w_teil, w_ben_gk, w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teil_hauptgr Hg, teil_untergrup Ug, teilelistepos_sachnr Sachnummer_TL, ben_text Benennung, teil_benennzus Zusatz, teilelistepos_position Pos, teil_sachnr Sachnummer_Publdaten, teilm_marke_tps Marke, teil_art Teileart, teil_produktkl ProduktKlasse, teil_mam MAM, teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM, NVL(teil_lagerverp, 0) LVM, NVL(teil_beh_verp, 0) BVM from w_teilelistepos@etk_nutzer left join w_teil@etk_publ on (teilelistepos_sachnr = teil_sachnr) left join w_ben_gk@etk_publ on (teil_textcode = ben_textcode and  ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken@etk_publ on (teil_sachnr = teilm_sachnr) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' order by teilelistepos_position
```

## DELETE
**Description:** Deletes records from w_teileliste filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_id, teileliste_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste@etk_nutzer where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'
```

## DELETE_JOBS
**Description:** Deletes records from w_teileliste_job filtered by teilelistejob_firma_id, teilelistejob_filiale_id, teilelistejob_teileliste_id, teilelistejob_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_job
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_job@etk_nutzer where teilelistejob_firma_id = '&FIRMA&' and teilelistejob_filiale_id = '&FILIALE&' and teilelistejob_teileliste_id = '&ID&' and teilelistejob_user_id = '&NUTZER&'
```

## DELETE_SRPS
**Description:** Deletes records from w_teileliste_srp filtered by teilelistesrp_firma_id, teilelistesrp_filiale_id, teilelistesrp_teileliste_id, teilelistesrp_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_srp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_srp@etk_nutzer where teilelistesrp_firma_id = '&FIRMA&' and teilelistesrp_filiale_id = '&FILIALE&' and teilelistesrp_teileliste_id = '&ID&' and teilelistesrp_user_id = '&NUTZER&'
```

## DELETE_POS
**Description:** Deletes records from w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'
```

## INSERT_BESTELLLISTE
**Description:** Inserts records in w_bestellliste. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: INSERT
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_bestellliste@etk_nutzer (bestellliste_firma_id, bestellliste_filiale_id, bestellliste_liste_id) values ('&FIRMAID&', '&FILIALID&', '&ID&')
```

## UPDATE_POS
**Description:** Updates records in w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos@etk_nutzer set teilelistepos_position = teilelistepos_position  - 1 where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_position > &POS&
```

## UPDATE_BESTELL_POS
**Description:** Updates records in w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id, bestelllistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: UPDATE
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_bestelllistepos@etk_nutzer set bestelllistepos_position = bestelllistepos_position  - 1 where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position > &POS&
```

## DELETE_BESTELLLISTE_POS
**Description:** Deletes records from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from  w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&'
```

## DELETE_SINGLE_POS
**Description:** Deletes records from w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_position = &POS&
```

## DELETE_BESTELLLISTE_SINGLE_POS
**Description:** Deletes records from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id, bestelllistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position = &POS&
```

## INSERT_LISTE_ALLG
**Description:** Inserts records in w_teileliste. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste@etk_nutzer (teileliste_firma_id, teileliste_filiale_id, teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf, teileliste_dringlichkeit, teileliste_vin, teileliste_rr_sap_status) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&BEMERKUNG&', &ERZEUGT&, &GEAENDERT&, '&MARKE&', &AUFTRAGSNR&, &AUFTRAGSNRLOKAL&, &KUNDENNRLOKAL&, &PRIVAT&, &GESPERRT&, &FZGDURCHLAUF&, &DRINGLICHKEIT&, &VIN&, &RRSAPSTATUS&)
```

## COPY_LISTE_ALLG
**Description:** Inserts records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
insert into w_teileliste@etk_nutzer (teileliste_firma_id, teileliste_filiale_id, teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf) (select teileliste_firma_id, '&NEWFILIALE&', '&NEWID&', '&NEWNUTZER&', teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf from w_teileliste@etk_nutzer where teileliste_id = '&OLDID&' and teileliste_user_id = '&OLDNUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&OLDFILIALE&')
```

## UPDATE_LISTE_ALLG
**Description:** Updates records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_bemerkung = '&BEMERKUNG&', teileliste_erzeugt = &ERZEUGT&, teileliste_geaendert = &GEAENDERT&, teileliste_marke = '&MARKE&', teileliste_auftragsnr = &AUFTRAGSNR&, teileliste_auftragsnr_lokal = &AUFTRAGSNRLOKAL&, teileliste_kundennr_lokal = &KUNDENNRLOKAL&, teileliste_privat = &PRIVAT&, teileliste_dringlichkeit = &DRINGLICHKEIT&, teileliste_vin = &VIN&, teileliste_rr_sap_status = &RRSAPSTATUS& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## UPDATE_LISTE_AENDER_DAT
**Description:** Updates records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_geaendert = &GEAENDERT& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## UPDATE_LISTE_SPERRE
**Description:** Updates records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_gesperrt = &GESPERRT& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## RETRIEVE_LISTE_SPERRE
**Description:** Retrieves data from w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teileliste_gesperrt_von GesperrtVon, teileliste_gesperrt_am GesperrtAm, teileliste_gesperrt Gesperrt from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&LISTE_NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## UPDATE_LISTE_SPERRE_NUTZER
**Description:** Updates records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_gesperrt_von = &NUTZER&, teileliste_gesperrt_am = &SPERRE_DAT& where teileliste_id = '&ID&' and teileliste_user_id = '&LISTE_NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## INSERT_SENDE_INFO
**Description:** Inserts records in w_teileliste_sendeinfo. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste_sendeinfo@etk_nutzer (teilelistesi_teileliste_id, teilelistesi_user_id, teilelistesi_firma_id, teilelistesi_filiale_id, teilelistesi_satzart, teilelistesi_auftragsnr, teilelistesi_kundennr, teilelistesi_mitarbeiternr, teilelistesi_greiferschein, teilelistesi_rechnung, teilelistesi_lieferschein, teilelistesi_freitext, teilelistesi_passwort, teilelistesi_sondersteuerung, teilelistesi_gegeben_bar, teilelistesi_gegeben_unbar) values ('&ID&', '&NUTZER&', '&FIRMA&', '&FILIALE&', '&SATZART&', &AUFTRAGSNR&, &KUNDENNR&, &MITARBEITERNR&, &GREIFERSCHEIN&, &RECHNUNG&, &LIEFERSCHEIN&, &FREITEXT&, &PASSWORT&, &SONDERSTEUERUNG&, &BAR&, &UNBAR&)
```

## DELETE_SENDE_INFO
**Description:** Deletes records from w_teileliste_sendeinfo filtered by teilelistesi_teileliste_id, teilelistesi_user_id, teilelistesi_firma_id, teilelistesi_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_teileliste_id = '&ID&' and teilelistesi_user_id = '&NUTZER&' and teilelistesi_firma_id = '&FIRMA&' and teilelistesi_filiale_id = '&FILIALE&'
```

## RETRIEVE_SENDE_INFO
**Description:** Retrieves data from w_teileliste_sendeinfo filtered by teilelistesi_teileliste_id, teilelistesi_user_id, teilelistesi_firma_id, teilelistesi_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teilelistesi_satzart Satzart, teilelistesi_auftragsnr AuftragsNr, teilelistesi_kundennr KundenNr, teilelistesi_mitarbeiternr MitarbeiterNr, teilelistesi_greiferschein Greiferschein, teilelistesi_rechnung Rechnung, teilelistesi_lieferschein Lieferschein, teilelistesi_freitext Freitext, teilelistesi_passwort Passwort, teilelistesi_sondersteuerung Sondersteuerung, teilelistesi_gegeben_bar Bar, teilelistesi_gegeben_unbar Unbar from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_teileliste_id = '&ID&' and teilelistesi_user_id = '&NUTZER&' and teilelistesi_firma_id = '&FIRMA&' and teilelistesi_filiale_id = '&FILIALE&'
```

## RETRIEVE_BESTELLLISTE_SPERRE_FOR_UPDATE
**Description:** Retrieves data from w_bestellliste filtered by bestellliste_firma_id, bestellliste_filiale_id, bestellliste_liste_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' and bestellliste_liste_id = '&ID&' for update
```

## UPDATE_BESTELLLISTE_SPERRE
**Description:** Updates records in w_bestellliste filtered by bestellliste_liste_id, bestellliste_firma_id, bestellliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: UPDATE
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_bestellliste@etk_nutzer set bestellliste_gesperrt_von = &NUTZER&, bestellliste_gesperrt_am = &SPERRE_DAT& where bestellliste_liste_id = '&ID&' and bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&'
```

## INSERT_LISTE_POS
**Description:** Inserts records in w_teilelistepos. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teilelistepos@etk_nutzer (teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_fistring, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock,  teilelistepos_typ, teilelistepos_ref, teilelistepos_menge_org) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', &POS&, '&HGUG&', '&SACHNR&', '&BENENNUNG&', '&ZUSATZ&', &MENGE&, &LAGERBESTAND&, &MINIMALBESTAND&, &BEDARFSHINWEIS&, '&LAGERORT&', &AUME&, &PREIS&, &RABATT&, '&SPLIT&', '&TRANSPARENZ&', '&SUFFIX&', '&DISPO&', '&RUECKSENDEPFLICHT&', &MWST&, &ATST&, &LOKALTEIL&, '&BEMERKUNG&', &FISTRING&, &JOBID&, &SRPID&, &STATUS&, &PRUEFEN&, &LOCK&, '&ASPGTYP&', '&ASPGREF&', &ASPGMENGE&)
```

## COPY_LISTE_POSITIONEN
**Description:** Inserts records in w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_user_id, teilelistepos_teileliste_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
insert into w_teilelistepos@etk_nutzer (teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock) (select teilelistepos_firma_id, '&NEWFILIALE&', '&NEWID&', '&NEWNUTZER&', teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&OLDFILIALE&' and teilelistepos_user_id = '&OLDNUTZER&' and teilelistepos_teileliste_id = '&OLDID&')
```

## INSERT_BESTELLLISTE_POS
**Description:** Inserts records in w_bestelllistepos. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: INSERT
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_bestelllistepos@etk_nutzer (bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id, bestelllistepos_position, bestelllistepos_hgug, bestelllistepos_sachnr, bestelllistepos_benennung, bestelllistepos_zusatz, bestelllistepos_menge, bestelllistepos_lagerbestand, bestelllistepos_minimalbestand, bestelllistepos_bedarfshinweis, bestelllistepos_lagerort, bestelllistepos_aume, bestelllistepos_auftragsnr, bestelllistepos_kundennr, bestelllistepos_lokalteil, bestelllistepos_bemerkung) values ('&FIRMA&', '&FILIALE&', '&ID&', &POS&, '&HGUG&', '&SACHNR&', '&BENENNUNG&', '&ZUSATZ&', &MENGE&, &LAGERBESTAND&, &MINIMALBESTAND&, &BEDARFSHINWEIS&, '&LAGERORT&', &AUME&, &AUFTRAGSNR&, &KUNDENNR&, &LOKALTEIL&, '&BEMERKUNG&')
```

## RETRIEVE_COUNT_BESTELLLISTEPOS
**Description:** Retrieves data from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(*) from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&'
```

## UPDATE_LISTE_POS
**Description:** Updates records in w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_user_id, teilelistepos_teileliste_id, teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos@etk_nutzer set teilelistepos_hgug = '&HGUG&', teilelistepos_sachnr = '&SACHNR&', teilelistepos_benennung = '&BENENNUNG&', teilelistepos_zusatz = '&ZUSATZ&', teilelistepos_menge = &MENGE&, teilelistepos_lagerbestand = &LAGERBESTAND&, teilelistepos_minimalbestand = &MINIMALBESTAND&, teilelistepos_bedarfshinweis = &BEDARFSHINWEIS&, teilelistepos_lagerort = '&LAGERORT&', teilelistepos_aume = &AUME&, teilelistepos_preis = &PREIS&, teilelistepos_rabatt = &RABATT&, teilelistepos_split = '&SPLIT&', teilelistepos_transparenz = '&TRANSPARENZ&', teilelistepos_suffix = '&SUFFIX&', teilelistepos_dispocode = '&DISPO&', teilelistepos_ruecksendepfl = '&RUECKSENDEPFLICHT&', teilelistepos_mwst = &MWST&, teilelistepos_altteil_steuer = &ATST&, teilelistepos_lokalteil = &LOKALTEIL&, teilelistepos_fistring = &FISTRING&, teilelistepos_bemerkung = '&BEMERKUNG&', teilelistepos_job_id = &JOBID&, teilelistepos_srp_id = &SRPID&, teilelistepos_status = &STATUS&, teilelistepos_pruefen = &PRUEFEN&, teilelistepos_lock = &LOCK& where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_position = &POS&
```

## UPDATE_SCORELISTE_POS_STATUS
**Description:** Updates records in w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_user_id, teilelistepos_teileliste_id, teilelistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos@etk_nutzer set teilelistepos_status = &STATUS& where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_position = &POS&
```

## UPDATE_BESTELLLISTE_POS
**Description:** Updates records in w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id, bestelllistepos_position. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: UPDATE
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_bestelllistepos@etk_nutzer set bestelllistepos_hgug = '&HGUG&', bestelllistepos_sachnr = '&SACHNR&', bestelllistepos_benennung = '&BENENNUNG&', bestelllistepos_zusatz = '&ZUSATZ&', bestelllistepos_menge = &MENGE&, bestelllistepos_lagerbestand = &LAGERBESTAND&, bestelllistepos_minimalbestand = &MINIMALBESTAND&, bestelllistepos_bedarfshinweis = &BEDARFSHINWEIS&, bestelllistepos_lagerort = '&LAGERORT&', bestelllistepos_aume = &AUME&, bestelllistepos_auftragsnr = &AUFTRAGSNR&, bestelllistepos_kundennr = &KUNDENNR&, bestelllistepos_lokalteil = &LOKALTEIL&, bestelllistepos_bemerkung = '&BEMERKUNG&' where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position = &POS&
```

## GET_TEILELISTE_ZU_AUFTRAG
**Description:** Retrieves data from w_teileliste, w_auftrag filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_auftragsnr, teileliste_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste, w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teileliste_id TeilelisteId, teileliste_user_id EigentuemerId, teileliste_gesperrt Gesperrt, auftrag_kundennr Kundennummer, auftrag_kundenname Kundenname, auftrag_fgstnr Fahrgestellnummer, auftrag_auftragsnr Auftragsnummer from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and  teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr) where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&AUFTRAGSNUMMER&'
```

## INSERT_AUFTRAG
**Description:** Inserts records in w_auftrag. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: INSERT
- Tables: w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_auftrag@etk_nutzer (auftrag_firma_id, auftrag_filiale_id, auftrag_auftragsnr, auftrag_kundennr, auftrag_kundenname, auftrag_fgstnr) values ('&FIRMA&', '&FILIALE&', '&AUFTRAGSNUMMER&', &KUNDENNUMMER&, &KUNDENNAME&, &FGSTNR&)
```

## UPDATE_AUFTRAG
**Description:** Updates records in w_auftrag filtered by auftrag_firma_id, auftrag_filiale_id, auftrag_auftragsnr. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: UPDATE
- Tables: w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_auftrag@etk_nutzer  set auftrag_kundennr = &KUNDENNUMMER&, auftrag_kundenname = &KUNDENNAME&, auftrag_fgstnr = &FGSTNR& where auftrag_firma_id = '&FIRMA&' and auftrag_filiale_id = '&FILIALE&' and auftrag_auftragsnr = '&AUFTRAGSNUMMER&'
```

## DELETE_AUFTRAG
**Description:** Deletes records from w_auftrag filtered by auftrag_firma_id, auftrag_filiale_id, auftrag_auftragsnr. Used in the TeilelisteJAVA module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_auftrag@etk_nutzer where auftrag_firma_id = '&FIRMA&' and   auftrag_filiale_id = '&FILIALE&' and   auftrag_auftragsnr = '&AUFTRAGSNUMMER&'
```

## UPDATE_EIGENTUEMER
**Description:** Updates records in w_teileliste filtered by teileliste_id, teileliste_user_id, teileliste_firma_id, teileliste_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_user_id = '&NEWNUTZER&' where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'
```

## UPDATE_EIGENTUEMER_POS
**Description:** Updates records in w_teilelistepos filtered by teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_firma_id, teilelistepos_filiale_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos@etk_nutzer set teilelistepos_user_id = '&NEWNUTZER&' where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&'
```

## INSERT_JOB
**Description:** Inserts records in w_teileliste_job. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste_job
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste_job@etk_nutzer (teilelistejob_firma_id, teilelistejob_filiale_id, teilelistejob_teileliste_id, teilelistejob_user_id, teilelistejob_job_id, teilelistejob_job_ben, teilelistejob_lock) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&JOBID&', '&JOBBEN&', '&LOCK&')
```

## INSERT_SRP
**Description:** Inserts records in w_teileliste_srp. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste_srp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste_srp@etk_nutzer (teilelistesrp_firma_id, teilelistesrp_filiale_id, teilelistesrp_teileliste_id, teilelistesrp_user_id, teilelistesrp_srp_id, teilelistesrp_job_id, teilelistesrp_srp_ben, teilelistesrp_lock, teilelistesrp_quelle) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&SRPID&', '&JOBID&', '&SRPBEN&', '&LOCK&', &QUELLE&)
```

## RETRIEVE_JOBS
**Description:** Retrieves data from w_teileliste_job filtered by teilelistejob_firma_id, teilelistejob_filiale_id, teilelistejob_teileliste_id, teilelistejob_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste_job
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teilelistejob_job_id id, teilelistejob_job_ben ben, teilelistejob_lock lockflag from w_teileliste_job@etk_nutzer WHERE teilelistejob_firma_id = '&FIRMA&' and teilelistejob_filiale_id = '&FILIALE&' and teilelistejob_teileliste_id = '&ID&' and teilelistejob_user_id = '&NUTZER&'
```

## RETRIEVE_SRPS
**Description:** Retrieves data from w_teileliste_srp filtered by teilelistesrp_firma_id, teilelistesrp_filiale_id, teilelistesrp_teileliste_id, teilelistesrp_user_id. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste_srp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teilelistesrp_srp_id id, teilelistesrp_srp_ben ben, teilelistesrp_job_id jobid, teilelistesrp_lock lockflag, teilelistesrp_quelle quelle from w_teileliste_srp@etk_nutzer WHERE teilelistesrp_firma_id = '&FIRMA&' and teilelistesrp_filiale_id = '&FILIALE&' and teilelistesrp_teileliste_id = '&ID&' and teilelistesrp_user_id = '&NUTZER&'
```

## GET_NEXT_ID_SEQ_VAL
**Description:** Retrieves data from teileliste_id_seq. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: teileliste_id_seq
- Tables not in schema: teileliste_id_seq
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select teileliste_id_seq.nextval from teileliste_id_seq
```

## GET_NEXT_SCOREID_SEQ_VAL
**Description:** Retrieves data from teileliste_score_id_seq. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: teileliste_score_id_seq
- Tables not in schema: teileliste_score_id_seq
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select teileliste_score_id_seq.nextval from teileliste_score_id_seq
```

## GET_NEXT_RRSAPID_SEQ_VAL
**Description:** Retrieves data from teileliste_rrsap_id_seq. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: teileliste_rrsap_id_seq
- Tables not in schema: teileliste_rrsap_id_seq
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select teileliste_rrsap_id_seq.nextval from teileliste_rrsap_id_seq
```

## GET_RELEASED_SCORE_LISTS
**Description:** Retrieves data from w_teileliste filtered by teileliste_firma_id, teileliste_user_id and ordered by fzgDlfID, geaendert, listeId. Used in the TeilelisteJAVA module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teileliste_fzgdurchlauf fzgDlfID, teileliste_firma_id firmaId, teileliste_filiale_id filialeId, teileliste_id listeId, teileliste_geaendert geaendert, teileliste_auftragsnr auftragsnr from w_teileliste where teileliste_firma_id = '&FIRMA&' and teileliste_fzgdurchlauf is not null and teileliste_user_id = 'score' ORDER BY fzgDlfID, geaendert, listeId
```

## LOAD_TC_INFO
**Description:** Retrieves data from w_tc_performance filtered by tcp_mospid, tcp_sachnr, tcp_datum_von, tcp_datum_bis. Used in the TeilelisteJAVA module to support ETK workflows for technical campaigns.


- Type: SELECT
- Tables: w_tc_performance
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct tcp_proddat_rel Teil_TC_ProdDatRelevant from w_tc_performance where tcp_mospid = &MOSP&   and tcp_sachnr = '&SACHNR&'   &TC_CHECK_LANDKUERZEL&    and tcp_datum_von <= &DATUM&    and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)
```
