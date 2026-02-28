# TeilelisteJAVA_SAP SQL Queries

Total queries: 9

## Table Relationships (Co-occurrence)

- w_ben_gk ↔ w_publben ↔ w_teileliste_hist

## LOAD_TEILELISTE_HIST

- Type: SELECT
- Tables: w_ben_gk, w_publben, w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilelistehist_datum Datum, ben_text Funktion, teilelistehist_abfrage_id AbfragId from w_ben_gk@etk_publ, w_publben@etk_publ, w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&' and publben_bezeichnung = teilelistehist_funktion and ben_textcode = publben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and publben_art = 'H' ORDER BY teilelistehist_datum DESC
```

## DELETE_TEILELISTE_HIST

- Type: DELETE
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&'
```

## INSERT_TEILELISTE_HIST

- Type: INSERT
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste_hist ( teilelistehist_firma_id, teilelistehist_id, teilelistehist_user_id, teilelistehist_datum, teilelistehist_abfrage_id, teilelistehist_funktion) values( '&FIRMA&', '&ID&', '&NUTZER&', SYSDATE, &ABFRAGEID&, '&FUNKTION&')
```

## LOAD_NUTZERDATEN

- Type: SELECT
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select user_rr_name Name, user_rr_telefon Telefon, user_rr_email Email, user_rr_haendlernr Haendlernr from w_user_rr where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'
```

## INSERT_NUTZERDATEN

- Type: INSERT
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_rr ( user_rr_firma_id, user_rr_id, user_rr_name, user_rr_telefon, user_rr_email, user_rr_haendlernr) values( '&FIRMA&', '&ID&', &NAME&, &TELEFON&, &EMAIL&, '&HAENDLERNR&')
```

## UPDATE_NUTZERDATEN

- Type: UPDATE
- Tables: w_user_rr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_user_rr set user_rr_name = &NAME&, user_rr_telefon = &TELEFON&, user_rr_email = &EMAIL&, user_rr_haendlernr = '&HAENDLERNR&' where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'
```

## UPDATE_ID_IN_LISTEPOS

- Type: UPDATE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teilelistepos set teilelistepos_teileliste_id = '&NEWID&' where teilelistepos_teileliste_id = '&OLDID&'\tand teilelistepos_firma_id = '&FIRMA&'\tand teilelistepos_filiale_id = '&FILIALE&'\tand teilelistepos_user_id = '&USER&'
```

## UPDATE_ID_IN_LISTEHIST

- Type: UPDATE
- Tables: w_teileliste_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste_hist set teilelistehist_id = '&NEWID&' where teilelistehist_id = '&OLDID&'\tand teilelistehist_firma_id = '&FIRMA&'\tand teilelistehist_user_id = '&USER&'
```

## UPDATE_ID_IN_LISTE

- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste set teileliste_id = '&NEWID&' where teileliste_id = '&OLDID&'\tand teileliste_firma_id = '&FIRMA&'\tand teileliste_filiale_id = '&FILIALE&'\tand teileliste_user_id = '&USER&'
```
