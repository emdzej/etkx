# Infotool SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_tipp ↔ w_user_tipps

## INSERT_USER_TIPP
**Description:** Inserts records in w_user_tipps. Used in the Infotool module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_tipps
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_tipps@etk_nutzer (usert_firma_id, usert_id, usert_tipp_id) table &TABELLE&
```

## DELETE_USER_TIPP
**Description:** Deletes records from w_user_tipps filtered by usert_firma_id, usert_id. Used in the Infotool module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_tipps
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_tipps@etk_nutzer where usert_firma_id = ?   and usert_id = ?
```

## LOAD_TIPPS_TRICKS
**Description:** Retrieves data from w_tipp, w_user_tipps filtered by usert_firma_id, usert_id, usert_tipp_id, tipp_wichtig and ordered by tipp_pos. Used in the Infotool module to support ETK workflows for tips.


- Type: SELECT
- Tables: w_tipp, w_user_tipps
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select tipp_id Id, tipp_filename Filename, tipp_art Art, DECODE(usert_tipp_id, usert_tipp_id, 'J', 'N') Gelesen  from w_tipp@etk_nutzer     left join w_user_tipps@etk_nutzer on (usert_firma_id = '&FIRMAID&'  and usert_id ='&USERID&' and usert_tipp_id = tipp_id) where tipp_id > 0 and tipp_wichtig = '&WICHTIG&' order by tipp_pos
```

## COUNT_TIPPS_TRICKS
**Description:** Retrieves data from w_tipp filtered by tipp_id. Used in the Infotool module to support ETK workflows for tips.


- Type: SELECT
- Tables: w_tipp
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id > 0
```

## COUNT_TICKER
**Description:** Retrieves data from w_tipp filtered by tipp_id. Used in the Infotool module to support ETK workflows for tips.


- Type: SELECT
- Tables: w_tipp
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id < 0
```
