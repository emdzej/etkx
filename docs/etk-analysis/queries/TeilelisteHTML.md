# TeilelisteHTML SQL Queries

Total queries: 9

## Table Relationships (Co-occurrence)

- w_teilelistepos ↔ w_ben_gk ↔ w_teil

## RETRIEVE_LISTEN_IDS

- Type: SELECT
- Tables: —
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
select teileliste_marke Marke,  teileliste_filiale_id Filiale, teileliste_user_id Eigentuemer, teileliste_id ListeId  teileliste_user_id = '&NUTZER&' and teileliste_marke IN (&MARKEN&)
```

## RETRIEVE_LISTE_ALLG

- Type: SELECT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, teileliste_geaendert Geaendert, teileliste_marke Marke from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'
```

## RETRIEVE_LISTE_POS

- Type: SELECT
- Tables: w_teilelistepos, w_ben_gk, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teilelistepos_sachnr SachNr, teil_hauptgr Hg, teil_untergrup Ug, teilelistepos_position Pos, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, ben_text Benennung, teil_benennzus Zusatz from w_teilelistepos@etk_nutzer, w_ben_gk, w_teil where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_sachnr = teil_sachnr and teil_textcode = ben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&' order by Pos
```

## DELETE

- Type: DELETE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'
```

## DELETE_POS

- Type: DELETE
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teilelistepos@etk_nutzer where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'
```

## INSERT_LISTE_ALLG

- Type: INSERT
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileliste@etk_nutzer (teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke) values ('&ID&', '&NUTZER&', '&BEMERKUNG&', &ERZEUGT&, &GEAENDERT&, '&MARKE&')
```

## UPDATE_LISTE_ALLG

- Type: UPDATE
- Tables: w_teileliste
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_teileliste@etk_nutzer set teileliste_bemerkung = '&BEMERKUNG&', teileliste_erzeugt = &ERZEUGT&, teileliste_geaendert = &GEAENDERT&, teileliste_marke = '&MARKE&' where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'
```

## INSERT_LISTE_POS

- Type: INSERT
- Tables: w_teilelistepos
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teilelistepos@etk_nutzer (teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_sachnr, teilelistepos_position, teilelistepos_menge, teilelistepos_bemerkung) values ('&ID&', '&NUTZER&', '&SACHNR&', &POS&, '&MENGE&', '&BEMERKUNG&')
```

## RETRIEVE_MAILADRESSEN

- Type: SELECT
- Tables: w_mailadressen
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select mailadr_mailadresse AnMailAdresse from w_mailadressen where mailadr_marke_tps IN (&MARKEN&)
```
