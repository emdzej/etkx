# TabellenKonfiguration SQL Queries

Total queries: 3

## LOAD_KONFIG
**Description:** Retrieves data from w_user_tabellenkonfig filtered by usertk_firma_id, usertk_user_id, usertk_table_name, usertk_zusatz and ordered by usertk_column_index. Used in the TabellenKonfiguration module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: yes

```sql
SELECT usertk_column_index Spalte,  usertk_column_name Name FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ? ORDER BY usertk_column_index
```

## INSERT_SPALTE
**Description:** Inserts records in w_user_tabellenkonfig. Used in the TabellenKonfiguration module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_user_tabellenkonfig@etk_nutzer (usertk_firma_id, usertk_user_id, usertk_table_name, usertk_zusatz, usertk_column_name, usertk_column_index ) VALUES ( ?, ?, ?, ?, ?, ? )
```

## DELETE_KONFIG
**Description:** Deletes records from w_user_tabellenkonfig filtered by usertk_firma_id, usertk_user_id, usertk_table_name, usertk_zusatz. Used in the TabellenKonfiguration module to support ETK workflows for user settings.


- Type: DELETE
- Tables: w_user_tabellenkonfig
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
DELETE FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ?
```
