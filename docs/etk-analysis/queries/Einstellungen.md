# Einstellungen SQL Queries

Total queries: 27

## Table Relationships (Co-occurrence)

- w_baureihe ↔ w_fztyp
- w_ben_gk ↔ w_publben
- w_markt_etk ↔ w_ben_gk
- w_user ↔ w_markt_ipac
- w_user_einstellungen_wmaerkte ↔ w_markt_etk ↔ w_ben_gk

## RETRIEVE_EINSTELLUNGEN
**Description:** Retrieves data from w_user_einstellungen filtered by user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_marke,  user_produktart, user_lenkung,  user_katalogumfang, user_iso,  user_regiso,  user_expand_bnb,  user_short_searchpath, user_request_saz, user_show_proddate,  user_dft_verbaumenge  from w_user_einstellungen  where user_id = '&ID&'
```

## RETRIEVE_EINSTELLUNGEN_JAVA
**Description:** Retrieves data from w_user_einstellungen filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath, user_request_saz, user_show_proddate, user_suchraum, user_show_preise, user_show_tipps, user_primaermarkt_id, user_tablestretch, user_fontsize,  user_dft_verbaumenge,  user_aufbewahrung  from w_user_einstellungen@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'
```

## RETRIEVE_EINSTELLUNGEN_MARKTID_JAVA
**Description:** Retrieves data from w_user, w_markt_ipac filtered by user_id, user_firma_id, marktipac_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user, w_markt_ipac
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select user_marktid Id, marktipac_kuerzel Kuerzel, marktipac_lkz Lkz, marktipac_relevanz_pa Produktart from w_user@etk_nutzer, w_markt_ipac@etk_publ where user_id = '&ID&' and user_firma_id = '&FIRMAID&' and marktipac_id = user_marktid
```

## RETRIEVE_IPAC_MARKT
**Description:** Retrieves data from w_markt_ipac filtered by marktipac_id. Used in the Einstellungen module to support ETK workflows for brand/market data.


- Type: SELECT
- Tables: w_markt_ipac
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select marktipac_kuerzel Kuerzel, marktipac_lkz Lkz from w_markt_ipac@etk_publ where marktipac_id = &ID&
```

## UPDATE_MARKTID
**Description:** Updates records in w_user filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
update w_user  set user_marktid = ? where user_firma_id = coalesce(?,user_firma_id) and user_id = coalesce(?,user_id)
```

## RETRIEVE_EINSTELLUNGEN_REGIONEN
**Description:** Retrieves data from w_user_einstellungen_region filtered by user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_region from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&'
```

## RETRIEVE_EINSTELLUNGEN_REGIONEN_JAVA
**Description:** Retrieves data from w_user_einstellungen_region filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_region from w_user_einstellungen_region@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'
```

## DELETE_EINSTELLUNGEN
**Description:** Deletes records from w_user_einstellungen filtered by user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen@etk_nutzer where user_id = '&ID&'
```

## DELETE_EINSTELLUNGEN_JAVA
**Description:** Deletes records from w_user_einstellungen filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'
```

## INSERT_EINSTELLUNGEN
**Description:** Inserts records in w_user_einstellungen. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_einstellungen@etk_nutzer (user_id, user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath,  user_request_saz, user_show_proddate, user_dft_verbaumenge ) values ('&ID&', '&MARKE&', '&PRODUKTART&', '&LENKUNG&', '&KATALOGUMFANG&', '&ISO&', '&REGISO&', '&EXPAND_BNB&', '&SHORT_SEARCHPATH&', '&REQUEST_SAZ&', &SHOW_PRODDATE&, '&VERBAUMENGE&' )
```

## INSERT_EINSTELLUNGEN_JAVA
**Description:** Inserts records in w_user_einstellungen. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_einstellungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_einstellungen@etk_nutzer (user_firma_id, user_id, user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath, user_request_saz, user_show_proddate, user_suchraum,  user_show_preise, user_show_tipps, user_primaermarkt_id, user_tablestretch, user_fontsize,user_dft_verbaumenge,  user_aufbewahrung)  values ('&FIRMAID&', '&ID&', '&MARKE&', '&PRODUKTART&', '&LENKUNG&', '&KATALOGUMFANG&', '&ISO&', '&REGISO&', '&EXPAND_BNB&', '&SHORT_SEARCHPATH&', '&REQUEST_SAZ&', &SHOW_PRODDATE&, '&SUCHRAUM&', &SHOW_PREISE&, &SHOW_TIPPS&, &PRIMAER_MARKT_ID&, '&TABLESTRETCH&', '&FONTSIZE&', '&VERBAUMENGE&', '&AUFBEWAHRUNG&')
```

## RETRIEVE_SPRACHEN
**Description:** Retrieves data from w_ben_gk, w_publben filtered by publben_art, ben_textcode, ben_iso, ben_regiso and ordered by ben_iso, ben_regiso. Used in the Einstellungen module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_publben
- Parameterized: no
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select ben_iso ISO, ben_regiso RegISO, ben_text Benennung from w_ben_gk, w_publben where publben_art = 'S' and ben_textcode = publben_textcode and ben_iso = substr(publben_bezeichnung, 1, 2) and ben_regiso = substr(publben_bezeichnung, 3, 2) order by ben_iso, ben_regiso
```

## DELETE_EINSTELLUNGEN_REGIONEN
**Description:** Deletes records from w_user_einstellungen_region filtered by user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&'
```

## DELETE_EINSTELLUNGEN_REGIONEN_JAVA
**Description:** Deletes records from w_user_einstellungen_region filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen_region@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'
```

## INSERT_EINSTELLUNGEN_REGIONEN
**Description:** Inserts records in w_user_einstellungen_region. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_einstellungen_region@etk_nutzer (user_id, user_region) values ('&ID&', '&REGION&')
```

## INSERT_EINSTELLUNGEN_REGIONEN_JAVA
**Description:** Inserts records in w_user_einstellungen_region. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_einstellungen_region
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_einstellungen_region@etk_nutzer (user_firma_id, user_id, user_region) values ('&FIRMAID&', '&ID&', '&REGION&')
```

## RETRIEVE_REGIONEN
**Description:** Retrieves data from w_fztyp. Used in the Einstellungen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_fztyp
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select distinct fztyp_ktlgausf REGIONEN from  w_fztyp
```

## RETRIEVE_COUNT_MODSPALTEN
**Description:** Retrieves data from w_baureihe, w_fztyp filtered by baureihe_marke_tps, baureihe_produktart, baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf. Used in the Einstellungen module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select count (distinct fztyp_mospid) from w_baureihe, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_baureihe = fztyp_baureihe and &LENKUNG_STMT& fztyp_vbereich = '&KATALOGUMFANG&' and  fztyp_ktlgausf in (&REGIONEN&)
```

## RETRIEVE_RECHTE_JAVA
**Description:** Retrieves data from w_user_funktionsrechte filtered by userf_id, userf_firma_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_funktionsrechte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select userf_recht_id RechtId from w_user_funktionsrechte@etk_nutzer where userf_id = '&ID&' and userf_firma_id = '&FIRMA&'
```

## RETRIEVE_RECHTE
**Description:** Retrieves data from w_user_funktionsrechte filtered by userf_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_funktionsrechte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select userf_recht_id RechtId from w_user_funktionsrechte where userf_id = '&ID&'
```

## RETRIEVE_BERECHTIGUNGEN
**Description:** Retrieves data from w_user_berechtigungen filtered by userb_firma_id, userb_id and ordered by Art. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_berechtigungen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select userb_art Art, userb_wert Wert from w_user_berechtigungen@etk_nutzer where userb_firma_id = '&FIRMAID&' and  userb_id = '&ID&' order by Art
```

## DELETE_TEILENOTIZEN_ABGELAUFEN
**Description:** Deletes records from w_teileinfo filtered by teileinfo_firma_id, teileinfo_user_id, teileinfo_gueltig_bis_jahr, teileinfo_gueltig_bis_monat, teile. Used in the Einstellungen module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileinfo@etk_nutzer where teileinfo_firma_id = '&FIRMAID&' and  teileinfo_user_id = '&ID&' and (teileinfo_gueltig_bis_jahr <= &JAHR& and  teileinfo_gueltig_bis_monat < &MONAT& and  teileinfo_gueltig_bis_monat is not null) or  (teileinfo_gueltig_bis_jahr < &JAHR& and  teileinfo_gueltig_bis_monat is null)
```

## RETRIEVE_MAERKTE_ETK_LOKALE_PRODUKTE
**Description:** Retrieves data from w_markt_etk, w_ben_gk filtered by marktetk_anzlokbt, ben_textcode, ben_iso, ben_regiso and ordered by ben_text. Used in the Einstellungen module to support ETK workflows for brand/market data.


- Type: SELECT
- Tables: w_markt_etk, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select marktetk_id id, ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_markt_etk, w_ben_gk where marktetk_anzlokbt > 0 and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text
```

## RETRIEVE_ETK_MARKT
**Description:** Retrieves data from w_markt_etk, w_ben_gk filtered by marktetk_id, ben_textcode, ben_iso, ben_regiso. Used in the Einstellungen module to support ETK workflows for brand/market data.


- Type: SELECT
- Tables: w_markt_etk, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_markt_etk, w_ben_gk where marktetk_id = &ID& and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## RETRIEVE_WEITERE_MAERKTE
**Description:** Retrieves data from w_user_einstellungen_wmaerkte, w_markt_etk, w_ben_gk filtered by user_firma_id, user_id, marktetk_id, ben_textcode, ben_iso, ben_regiso and ordered by ben_text. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_einstellungen_wmaerkte, w_markt_etk, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select user_markt_id, ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_user_einstellungen_wmaerkte@etk_nutzer, w_markt_etk, w_ben_gk where user_firma_id = '&FIRMA&' and user_id = '&ID&' and marktetk_id = user_markt_id and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text
```

## DELETE_WEITERE_MAERKTE
**Description:** Deletes records from w_user_einstellungen_wmaerkte filtered by user_firma_id, user_id. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_einstellungen_wmaerkte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_einstellungen_wmaerkte@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&USERID&'
```

## INSERT_WEITERE_MAERKTE
**Description:** Inserts records in w_user_einstellungen_wmaerkte. Used in the Einstellungen module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_einstellungen_wmaerkte
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_einstellungen_wmaerkte@etk_nutzer (user_firma_id, user_id, user_markt_id) VALUES ('&FIRMA&', '&USERID&', &MARKTID&)
```
