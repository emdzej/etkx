# TabellenKonfiguration SQL Queries

Total queries: 3

## LOAD_KONFIG

- Type: SELECT
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: yes

```sql
SELECT usertk_column_index Spalte,  usertk_column_name Name FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ? ORDER BY usertk_column_index
```

## INSERT_SPALTE

- Type: INSERT
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_user_tabellenkonfig@etk_nutzer (usertk_firma_id, usertk_user_id, usertk_table_name, usertk_zusatz, usertk_column_name, usertk_column_index ) VALUES ( ?, ?, ?, ?, ?, ? )
```

## DELETE_KONFIG

- Type: DELETE
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
DELETE FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ?
```
