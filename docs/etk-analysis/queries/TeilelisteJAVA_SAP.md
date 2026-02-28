# TeilelisteJAVA_SAP SQL Queries

Total queries: 9

## Table Relationships (Co-occurrence)

- w_ben_gk ↔ w_publben ↔ w_teileliste_hist

## LOAD_TEILELISTE_HIST
**Description:** Retrieves data from w_ben_gk, w_publben, w_teileliste_hist filtered by teilelistehist_firma_id, teilelistehist_id, teilelistehist_user_id, publben_bezeichnung, ben_textcode, ben_iso, ben_regiso, publben_art and ordered by teilelistehist_datum. Used in the TeilelisteJAVA_SAP module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_publben, w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilelistehist_datum Datum, ben_text Funktion, teilelistehist_abfrage_id AbfragId from w_ben_gk@etk_publ, w_publben@etk_publ, w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&' and publben_bezeichnung = teilelistehist_funktion and ben_textcode = publben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and publben_art = 'H' ORDER BY teilelistehist_datum DESC
```

## DELETE_TEILELISTE_HIST
**Description:** Deletes records from w_teileliste_hist filtered by teilelistehist_firma_id, teilelistehist_id, teilelistehist_user_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&'
```

## INSERT_TEILELISTE_HIST
**Description:** Inserts records in w_teileliste_hist. Used in the TeilelisteJAVA_SAP module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste_hist ( teilelistehist_firma_id, teilelistehist_id, teilelistehist_user_id, teilelistehist_datum, teilelistehist_abfrage_id, teilelistehist_funktion) values( '&FIRMA&', '&ID&', '&NUTZER&', SYSDATE, &ABFRAGEID&, '&FUNKTION&')
```

## LOAD_NUTZERDATEN
**Description:** Retrieves data from w_user_rr filtered by user_rr_firma_id, user_rr_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_rr_name Name, user_rr_telefon Telefon, user_rr_email Email, user_rr_haendlernr Haendlernr from w_user_rr where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'
```

## INSERT_NUTZERDATEN
**Description:** Inserts records in w_user_rr. Used in the TeilelisteJAVA_SAP module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_rr ( user_rr_firma_id, user_rr_id, user_rr_name, user_rr_telefon, user_rr_email, user_rr_haendlernr) values( '&FIRMA&', '&ID&', &NAME&, &TELEFON&, &EMAIL&, '&HAENDLERNR&')
```

## UPDATE_NUTZERDATEN
**Description:** Updates records in w_user_rr filtered by user_rr_firma_id, user_rr_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_user_rr set user_rr_name = &NAME&, user_rr_telefon = &TELEFON&, user_rr_email = &EMAIL&, user_rr_haendlernr = '&HAENDLERNR&' where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'
```

## UPDATE_ID_IN_LISTEPOS
**Description:** Updates records in w_teilelistepos filtered by teilelistepos_teileliste_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos set teilelistepos_teileliste_id = '&NEWID&' where teilelistepos_teileliste_id = '&OLDID&'\tand teilelistepos_firma_id = '&FIRMA&'\tand teilelistepos_filiale_id = '&FILIALE&'\tand teilelistepos_user_id = '&USER&'
```

## UPDATE_ID_IN_LISTEHIST
**Description:** Updates records in w_teileliste_hist filtered by teilelistehist_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste_hist set teilelistehist_id = '&NEWID&' where teilelistehist_id = '&OLDID&'\tand teilelistehist_firma_id = '&FIRMA&'\tand teilelistehist_user_id = '&USER&'
```

## UPDATE_ID_IN_LISTE
**Description:** Updates records in w_teileliste filtered by teileliste_id. Used in the TeilelisteJAVA_SAP module to support ETK workflows for part data.


- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste set teileliste_id = '&NEWID&' where teileliste_id = '&OLDID&'\tand teileliste_firma_id = '&FIRMA&'\tand teileliste_filiale_id = '&FILIALE&'\tand teileliste_user_id = '&USER&'
```
