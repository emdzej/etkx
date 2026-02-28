# Firmenkonfiguration SQL Queries

Total queries: 59

## Table Relationships (Co-occurrence)

- w_firma ↔ w_filiale
- w_firma ↔ w_filiale ↔ w_user
- w_publben ↔ w_ben_gk
- w_teileliste ↔ w_teileliste_sendeinfo
- w_teileliste ↔ w_teilelistepos
- w_user ↔ w_filiale
- w_user_funktionsrechte ↔ w_ben_gk ↔ w_publben
- w_zub_konfig ↔ w_user

## RETRIEVE_FIRMEN
**Description:** Retrieves data from w_firma and ordered by Bezeichnung. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_firma
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: yes

```sql
select firma_id Id, firma_bezeichnung Bezeichnung from w_firma@etk_nutzer order by Bezeichnung
```

## RETRIEVE_FILIALEN
**Description:** Retrieves data from w_filiale filtered by filiale_firma_id and ordered by Bezeichnung. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select filiale_id Id, filiale_bezeichnung Bezeichnung from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' order by Bezeichnung
```

## RETRIEVE_COUNT_FILIALEN_IN_FIRMA
**Description:** Retrieves data from w_filiale filtered by filiale_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select Count(*) countFiliale from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&'
```

## RETRIEVE_INFO_FILIALE
**Description:** Retrieves data from w_firma, w_filiale filtered by filiale_firma_id, filiale_id, firma_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_firma, w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select firma_id FirmaId,  firma_bezeichnung FirmaBezeichnung,  filiale_id FilialeId,  filiale_bezeichnung FilialeBezeichnung,  filiale_iso SpracheISO,  filiale_regiso SpracheRegISO from w_firma@etk_nutzer, w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' and filiale_id = '&FILIALE&' and firma_id = filiale_firma_id
```

## RETRIEVE_EXIST_USER
**Description:** Retrieves data from w_user filtered by user_firma_id, user_id, user_passwort. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(user_id) ANZ from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&KENNUNG&' and user_passwort = '&PASSWORT&'
```

## RETRIEVE_DMS_VERWENDEN
**Description:** Retrieves data from w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: SELECT
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select konfig_hs_verwenden DMSVerwenden from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'
```

## LOAD_KONFIGURATION
**Description:** Retrieves data from w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: SELECT
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select konfig_hd_firma Firma, konfig_hd_zusatz Zusatz, konfig_hd_strasse Strasse, konfig_hd_plz Plz, konfig_hd_ort Ort, konfig_hd_telefon Telefon, konfig_hdnr_pkw Pkw, konfig_hdnr_motorrad Motorrad, konfig_mwst_niedrig MwstNiedrig, konfig_mwst_hoch MwstHoch, konfig_mwst_altteile MwstAltteile, konfig_mwst_3 Mwst3, konfig_mwst_4 Mwst4, konfig_rechnungnr RechnungsNr, konfig_mailserver Mailserver, konfig_barverkaufnr BarverkaufsNr, konfig_auftragnr AuftragsNr, konfig_kundennr KundenNr, konfig_hs_verwenden Verwenden, konfig_abwicklung Abwicklung, konfig_bestandfiliale Bestandfiliale, konfig_datenabgleich Datenabgleich from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'
```

## DELETE_KONFIGURATION
**Description:** Deletes records from w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: DELETE
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'
```

## DELETE_ZUB_KONFIGURATION
**Description:** Deletes records from w_zub_konfig filtered by konfigz_firma_id, konfigz_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for zub konfig data.


- Type: DELETE
- Tables: w_zub_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_zub_konfig@etk_nutzer where konfigz_firma_id = '&FIRMA&' and konfigz_filiale_id = '&FILIALE&'
```

## INSERT_KONFIGURATION
**Description:** Inserts records in w_konfig. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: INSERT
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_konfig@etk_nutzer (konfig_firma_id, konfig_filiale_id, konfig_hd_firma, konfig_hd_zusatz, konfig_hd_strasse, konfig_hd_plz, konfig_hd_ort, konfig_hd_telefon, konfig_hdnr_pkw, konfig_hdnr_motorrad, konfig_mwst_niedrig, konfig_mwst_hoch, konfig_mwst_altteile, konfig_mwst_3, konfig_mwst_4, konfig_rechnungnr, konfig_mailserver, konfig_barverkaufnr, konfig_auftragnr, konfig_kundennr, konfig_hs_verwenden, konfig_abwicklung, konfig_bestandfiliale, konfig_datenabgleich) values ('&FIRMA&', '&FILIALE&', &FIRMENNAME&, &ZUSATZ&, &STRASSE&, &PLZ&, &ORT&, &TELEFON&, &PKW&, &MOTOTRRAD&, &MWST_NIEDRIG&, &MWST_HOCH&, &MWST_ALTTEILE&, &MWST_3&, &MWST_4&, &RECHNUNGNR&, &MAILSERVER&, &BARVERKAUFNR&, &AUFTRAGSNR&, &KUNDENNR&, '&HS_VERWENDEN&', &ABWICKLUNG&, &BESTAND_FILIALE&, &DATENABGLEICH&)
```

## UPDATE_KONFIGURATION
**Description:** Updates records in w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: UPDATE
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_konfig@etk_nutzer set konfig_hd_firma = &FIRMENNAME&, konfig_hd_zusatz = &ZUSATZ&, konfig_hd_strasse = &STRASSE&, konfig_hd_plz = &PLZ&, konfig_hd_ort = &ORT&, konfig_hd_telefon = &TELEFON&, konfig_hdnr_pkw = &PKW&, konfig_hdnr_motorrad = &MOTOTRRAD&, konfig_mwst_niedrig = &MWST_NIEDRIG&, konfig_mwst_hoch = &MWST_HOCH&, konfig_mwst_altteile = &MWST_ALTTEILE&, konfig_mwst_3 = &MWST_3&, konfig_mwst_4 = &MWST_4&, konfig_rechnungnr = &RECHNUNGNR&, konfig_mailserver = &MAILSERVER&, konfig_barverkaufnr = &BARVERKAUFNR&, konfig_auftragnr = &AUFTRAGSNR&, konfig_kundennr = &KUNDENNR&, konfig_hs_verwenden = '&HS_VERWENDEN&', konfig_abwicklung = &ABWICKLUNG&, konfig_bestandfiliale = &BESTAND_FILIALE&, konfig_datenabgleich = &DATENABGLEICH& where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'
```

## SELECT_RECHNUNGSNUMMER_FOR_UPDATE
**Description:** Retrieves data from w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: SELECT
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select konfig_rechnungnr RechnungsNr from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&' for update
```

## UPDATE_RECHNUNGSNUMMER
**Description:** Updates records in w_konfig filtered by konfig_firma_id, konfig_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for konfig data.


- Type: UPDATE
- Tables: w_konfig
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_konfig@etk_nutzer set konfig_rechnungnr = &RECHNUNGSNR& where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'
```

## UPDATE_FIRMENBEZEICHNUNG
**Description:** Updates records in w_firma filtered by firma_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: UPDATE
- Tables: w_firma
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_firma@etk_nutzer set firma_bezeichnung = '&BEZEICHNUNG&' where firma_id = '&ID&'
```

## INSERT_FILIALE
**Description:** Inserts records in w_filiale. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: INSERT
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_filiale@etk_nutzer (filiale_firma_id, filiale_id, filiale_bezeichnung, filiale_iso, filiale_regiso) values ('&FIRMAID&', '&FILIALID&', '&FILIALE&', '&ISO&', '&REGISO&')
```

## DELETE_FILIALE
**Description:** Deletes records from w_filiale filtered by filiale_firma_id, filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: DELETE
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMAID&' and filiale_id = '&FILIALID&'
```

## UPDATE_FILIALE
**Description:** Updates records in w_filiale filtered by filiale_firma_id, filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: UPDATE
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_filiale@etk_nutzer set filiale_bezeichnung = '&FILIALE&', filiale_iso = '&ISO&', filiale_regiso = '&REGISO&' where filiale_firma_id = '&FIRMAID&' and filiale_id = '&FILIALID&'
```

## RETRIEVE_FILIALEN_SPRACHEN
**Description:** Retrieves data from w_filiale filtered by filiale_firma_id and ordered by filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select filiale_id Id, filiale_bezeichnung Bezeichnung, filiale_iso Iso, filiale_regiso RegIso from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' order by filiale_id
```

## RETRIEVE_ANZAHL_NUTZER
**Description:** Retrieves data from w_user filtered by user_firma_id, NOT, user_id, user_name. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
SELECT count(user_id) NutzerAnzahl FROM   w_user@etk_nutzer WHERE  user_firma_id = '&FIRMA&'   AND  user_id NOT IN (&IGNORE_USER&)   AND ( user_id  LIKE INSENSITIVE '&KRIT&'    OR user_name  LIKE INSENSITIVE '&KRIT&' )
```

## RETRIEVE_MATCHING_NUTZER
**Description:** Retrieves data from w_user filtered by user_firma_id, NOT, user_id, user_name and ordered by user_name. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
SELECT user_id NutzerId,        user_name NutzerName FROM   w_user@etk_nutzer WHERE  user_firma_id = '&FIRMA&'   AND  user_id NOT IN (&IGNORE_USER&)   AND ( user_id  LIKE INSENSITIVE '&KRIT&'    OR user_name  LIKE INSENSITIVE '&KRIT&' ) ORDER BY user_name
```

## RETRIEVE_NUTZER
**Description:** Retrieves data from w_user filtered by user_firma_id and ordered by user_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select user_id NutzerId, user_name NutzerName, user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer from w_user@etk_nutzer where user_firma_id = '&FIRMA&' order by user_id
```

## RETRIEVE_SINGLE_NUTZER
**Description:** Retrieves data from w_user filtered by user_firma_id, user_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_name NutzerName,  user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer  from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&NUTZERID&'
```

## RETRIEVE_BERECHTIGUNEN
**Description:** Retrieves data from w_firma_berechtigungen filtered by firmab_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_firma_berechtigungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select firmab_art Art, firmab_wert Wert from w_firma_berechtigungen@etk_nutzer where firmab_firma_id = '&FIRMA&'
```

## RETRIEVE_SPRACHEN
**Description:** Retrieves data from w_publben, w_ben_gk filtered by publben_art, publben_textcode, ben_iso, ben_regiso. Used in the Firmenkonfiguration module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select publben_bezeichnung Code, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'S' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## RETRIEVE_FUNKTIONSRECHTE
**Description:** Retrieves data from w_publben, w_ben_gk filtered by publben_art, publben_textcode, ben_iso, ben_regiso and ordered by ben_text. Used in the Firmenkonfiguration module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select publben_bezeichnung Bezeichnung, ben_text Text from w_publben, w_ben_gk where publben_art = 'R' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text
```

## RETRIEVE_NUTZER_FUNKTIONSRECHTE
**Description:** Retrieves data from w_user_funktionsrechte, w_ben_gk, w_publben filtered by userf_firma_id, userf_id, publben_art, publben_bezeichnung, ben_textcode, ben_iso, ben_regiso and ordered by publben_bezeichnung. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_funktionsrechte, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select publben_bezeichnung Bezeichnung, ben_text Text from w_user_funktionsrechte@etk_nutzer, w_ben_gk, w_publben  where userf_firma_id = '&FIRMA_ID&' and userf_id = '&USER_ID&' and publben_art = 'R'  and publben_bezeichnung = userf_recht_id  and ben_textcode = publben_textcode  and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by publben_bezeichnung
```

## RETRIEVE_NUTZER_BERECHTIGUNGEN
**Description:** Retrieves data from w_user_berechtigungen filtered by userb_id, userb_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_berechtigungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select userb_art Art, userb_wert Wert from w_user_berechtigungen@etk_nutzer where userb_id = '&USER&' and userb_firma_id = '&FIRMA&'
```

## DELETE_NUTZER_BERECHTIGUNGEN
**Description:** Deletes records from w_user_berechtigungen filtered by userb_id, userb_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_berechtigungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_berechtigungen@etk_nutzer where userb_id = '&ID&' and userb_firma_id = '&FIRMA&'
```

## DELETE_NUTZER_FUNKTIONSRECHTE
**Description:** Deletes records from w_user_funktionsrechte filtered by userf_id, userf_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_funktionsrechte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_funktionsrechte@etk_nutzer where userf_id = '&ID&' and userf_firma_id = '&FIRMA&'
```

## DELETE_NUTZER_EINSTELLUNGEN
**Description:** Deletes records from w_user_einstellungen filtered by user_id, user_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen@etk_nutzer where user_id = '&ID&' and user_firma_id = '&FIRMA&'
```

## DELETE_ZUB_NUTZER
**Description:** Deletes records from w_zub_user filtered by userz_id, userz_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_zub_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_zub_user@etk_nutzer where userz_id = '&ID&' and userz_firma_id = '&FIRMA&'
```

## DELETE_NUTZER_EINSTELLUNGEN_REGION
**Description:** Deletes records from w_user_einstellungen_region filtered by user_id, user_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&' and user_firma_id = '&FIRMA&'
```

## DELETE_NUTZER
**Description:** Deletes records from w_user filtered by user_id, user_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user@etk_nutzer where user_id = '&ID&' and user_firma_id= '&FIRMA&'
```

## DELETE_NUTZER_TEILELISTEPOS
**Description:** Deletes records from w_teilelistepos filtered by teilelistepos_user_id, teilelistepos_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teilelistepos@etk_nutzer where teilelistepos_user_id = '&ID&' and teilelistepos_firma_id= '&FIRMA&'
```

## DELETE_NUTZER_TEILELISTE
**Description:** Deletes records from w_teileliste filtered by teileliste_user_id, teileliste_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste@etk_nutzer where teileliste_user_id = '&ID&' and teileliste_firma_id= '&FIRMA&'
```

## DELETE_NUTZER_TEILELISTE_SENDEINFO
**Description:** Deletes records from w_teileliste_sendeinfo filtered by teilelistesi_user_id, teilelistesi_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_user_id = '&ID&' and teilelistesi_firma_id= '&FIRMA&'
```

## DELETE_NUTZER_TEILEINFO
**Description:** Deletes records from w_teileinfo filtered by teileinfo_user_id, teileinfo_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileinfo@etk_nutzer where teileinfo_user_id = '&ID&' and teileinfo_firma_id= '&FIRMA&'
```

## RETRIEVE_EXIST_USER_ID
**Description:** Retrieves data from w_user filtered by user_firma_id, user_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(user_id) Cnt from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and LOWER(user_id) = '&KENNUNG&'
```

## STORE_NUTZER
**Description:** Inserts records in w_user. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user@etk_nutzer (user_firma_id, user_id, user_name, user_passwort, user_default_filiale_id, user_bearbeiternummer ) values('&FIRMAID&', '&USERID&', '&USERNAME&', '&PASSWORD&', '&FILIALE&', &BEARBEITERNUMMER&)
```

## STORE_NUTZER_BERECHTIGUNGEN
**Description:** Inserts records in w_user_berechtigungen. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_berechtigungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_berechtigungen@etk_nutzer (userb_firma_id, userb_id, userb_art, userb_wert ) values ('&FIRMAID&', '&USERID&', '&ART&', '&WERT&')
```

## STORE_NUTZER_FUNKTIONSRECHTE
**Description:** Inserts records in w_user_funktionsrechte. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_funktionsrechte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_funktionsrechte@etk_nutzer (userf_firma_id, userf_id, userf_recht_id) values ('&FIRMAID&', '&USERID&', '&RECHT&')
```

## UPDATE_NUTZER
**Description:** Updates records in w_user filtered by user_firma_id, user_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
UPDATE w_user@etk_nutzer SET    user_name = ?,        user_passwort = ?,        user_default_filiale_id = ?,        user_bearbeiternummer = ? WHERE  user_firma_id = ? AND    user_id = ?
```

## MOVE_TEILELISTEN
**Description:** Updates records in w_teileliste filtered by teileliste_firma_id, teileliste_filiale_id, teileliste_user_id, teileliste_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_filiale_id = '&FILIALID_NEU&' where teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&' and teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')
```

## MOVE_TEILELISTENPOS
**Description:** Updates records in w_teileliste, w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_user_id, teilelistepos_teileliste_id, teileliste_firma_id, teileliste_filiale_id, teileliste_user_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste, w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos@etk_nutzer set teilelistepos_filiale_id = '&FILIALID_NEU&' where teilelistepos_firma_id = '&FIRMAID&' and teilelistepos_filiale_id = '&FILIALID_ALT&' and teilelistepos_user_id = '&USERID&' and teilelistepos_teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')
```

## MOVE_TEILELISTENSI
**Description:** Updates records in w_teileliste, w_teileliste_sendeinfo filtered by teilelistesi_firma_id, teilelistesi_filiale_id, teilelistesi_user_id, teilelistesi_teileliste_id, teileliste_firma_id, teileliste_filiale_id, teileliste_user_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste, w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste_sendeinfo@etk_nutzer set teilelistesi_filiale_id = '&FILIALID_NEU&' where teilelistesi_firma_id = '&FIRMAID&' and teilelistesi_filiale_id = '&FILIALID_ALT&' and teilelistesi_user_id = '&USERID&' and teilelistesi_teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')
```

## RETRIEVE_USERINFO
**Description:** Retrieves data from w_user, w_filiale filtered by user_firma_id, user_id, user_default_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user, w_filiale
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select user_id UserId, user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer,  filiale_bezeichnung FilialBezeichnung, filiale_iso Iso,  filiale_regiso RegIso from w_user@etk_nutzer, w_filiale@etk_nutzer where user_firma_id = '&FIRMA&'  and user_id = '&USER&'  and user_default_filiale_id = filiale_id and user_firma_id = filiale_firma_id
```

## UPDATE_DEFAULT_FILIALE
**Description:** Updates records in w_user filtered by user_firma_id, user_id. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_user@etk_nutzer set user_default_filiale_id='&FILIALID&' where user_firma_id='&FIRMAID&' and user_id='&USERID&'
```

## RETRIEVE_EINEFIRMAFILIALENUTZER
**Description:** Retrieves data from w_firma, w_filiale, w_user filtered by filiale_firma_id. Used in the Firmenkonfiguration module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_firma, w_filiale, w_user
- Parameterized: no
- Hardcoded literals: no
- Joins: yes | Filters: no | Sorting: no

```sql
select firma_id FirmaId, filiale_id FilialId, user_id UserId from w_firma@etk_nutzer left join w_filiale@etk_nutzer on (filiale_firma_id = firma_id) left join w_user@etk_nutzer on (user_firma_id = firma_id)
```

## RETRIEVE_USER_BY_DEFAULT_FILIALE
**Description:** Retrieves data from w_user filtered by user_default_filiale_id, user_firma_id and ordered by user_name. Used in the Firmenkonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select user_id NutzerId, user_name NutzerName, user_passwort Passwort, user_bearbeiternummer BearbeiterNummer from w_user@etk_nutzer where user_default_filiale_id='&FILIALID&' and user_firma_id='&FIRMAID&' order by user_name
```

## RETRIEVE_BESTELLLISTE_POSITIONEN
**Description:** Retrieves data from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for orders.


- Type: SELECT
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select * from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id='&FIRMAID&' and bestelllistepos_filiale_id='&FILIALID&'
```

## RETRIEVE_TEILELISTE_POSITIONEN
**Description:** Retrieves data from w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select * from w_teilelistepos@etk_nutzer where teilelistepos_firma_id='&FIRMAID&' and teilelistepos_filiale_id='&FILIALID&'
```

## DELETE_ALL_AUFTRAEGE
**Description:** Deletes records from w_auftrag filtered by auftrag_firma_id, auftrag_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_auftrag
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_auftrag@etk_nutzer where auftrag_firma_id='&FIRMAID&' and auftrag_filiale_id='&FILIALID&'
```

## DELETE_ALL_TEILELISTEN
**Description:** Deletes records from w_teileliste filtered by teileliste_firma_id, teileliste_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste@etk_nutzer where teileliste_firma_id='&FIRMAID&' and teileliste_filiale_id='&FILIALID&'
```

## DELETE_ALL_TEILELISTEN_POSITIONEN
**Description:** Deletes records from w_teilelistepos filtered by teilelistepos_firma_id, teilelistepos_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id='&FIRMAID&' and teilelistepos_filiale_id='&FILIALID&'
```

## DELETE_ALL_TEILELISTEN_SI
**Description:** Deletes records from w_teileliste_sendeinfo filtered by teilelistesi_firma_id, teilelistesi_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_sendeinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_firma_id='&FIRMAID&' and teilelistesi_filiale_id='&FILIALID&'
```

## DELETE_ALL_BESTELLLISTEN
**Description:** Deletes records from w_bestellliste filtered by bestellliste_firma_id, bestellliste_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_bestellliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_bestellliste@etk_nutzer where bestellliste_firma_id='&FIRMAID&' and bestellliste_filiale_id='&FILIALID&'
```

## DELETE_ALL_BESTELLLISTEN_POSITIONEN
**Description:** Deletes records from w_bestelllistepos filtered by bestelllistepos_firma_id, bestelllistepos_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for orders.


- Type: DELETE
- Tables: w_bestelllistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id='&FIRMAID&' and bestelllistepos_filiale_id='&FILIALID&'
```

## EXISTIERT_FILIAL_ZUB
**Description:** Retrieves data from w_zub_konfig filtered by konfigz_firma_id, konfigz_filiale_id. Used in the Firmenkonfiguration module to support ETK workflows for zub konfig data.


- Type: SELECT
- Tables: w_zub_konfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select count(*) from w_zub_konfig@etk_nutzer where konfigz_firma_id = ?  and konfigz_filiale_id = ?  and konfigz_default_markt_id is not null
```

## UPDATE_MARKTID_NUTZER_VON_FILIALE
**Description:** Updates records in w_zub_konfig, w_user filtered by konfigz_filiale_id, konfigz_firma_id, user_id. Used in the Firmenkonfiguration module to support ETK workflows for zub konfig data.


- Type: UPDATE
- Tables: w_zub_konfig, w_user
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
update w_user@etk_nutzer set user_marktid = ( select konfigz_default_markt_id from w_zub_konfig@etk_nutzer  where konfigz_filiale_id = ? and konfigz_firma_id = user_firma_id)  where user_firma_id = ? and user_id = ?
```
