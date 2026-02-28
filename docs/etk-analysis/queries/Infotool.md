# Infotool SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_tipp ↔ w_user_tipps

## INSERT_USER_TIPP

- Type: INSERT
- Tables: w_user_tipps
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_user_tipps@etk_nutzer (usert_firma_id, usert_id, usert_tipp_id) table &TABELLE&
```

## DELETE_USER_TIPP

- Type: DELETE
- Tables: w_user_tipps
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_user_tipps@etk_nutzer where usert_firma_id = ?   and usert_id = ?
```

## LOAD_TIPPS_TRICKS

- Type: SELECT
- Tables: w_tipp, w_user_tipps
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select tipp_id Id, tipp_filename Filename, tipp_art Art, DECODE(usert_tipp_id, usert_tipp_id, 'J', 'N') Gelesen  from w_tipp@etk_nutzer     left join w_user_tipps@etk_nutzer on (usert_firma_id = '&FIRMAID&'  and usert_id ='&USERID&' and usert_tipp_id = tipp_id) where tipp_id > 0 and tipp_wichtig = '&WICHTIG&' order by tipp_pos
```

## COUNT_TIPPS_TRICKS

- Type: SELECT
- Tables: w_tipp
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id > 0
```

## COUNT_TICKER

- Type: SELECT
- Tables: w_tipp
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id < 0
```
