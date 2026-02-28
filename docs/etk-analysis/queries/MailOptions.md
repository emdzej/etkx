# MailOptions SQL Queries

Total queries: 5

## LOAD_ABSENDER_UND_EMPFAENGER

- Type: SELECT
- Tables: w_user_mailoptions
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
SELECT usermo_krit_art Kriterium, usermo_krit_wert Wert FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND (usermo_krit_art = 'E-Mail-Adresse Empfaenger' OR usermo_krit_art = 'E-Mail-Adresse Absender' OR usermo_krit_art = 'Name Absender')
```

## INSERT_ABSENDER

- Type: INSERT
- Tables: w_user_mailoptions
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_user_mailoptions@etk_nutzer (usermo_firma_id, usermo_user_id, usermo_krit_art, usermo_krit_wert) TABLE ( ( ?, ?, 'E-Mail-Adresse Absender', ? ), ( ?, ?, 'Name Absender', ? ) )
```

## DELETE_ABSENDER

- Type: DELETE
- Tables: w_user_mailoptions
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
DELETE FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND (usermo_krit_art = 'Name Absender' OR usermo_krit_art = 'E-Mail-Adresse Absender')
```

## INSERT_EMPFAENGER

- Type: INSERT
- Tables: w_user_mailoptions
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_user_mailoptions@etk_nutzer (usermo_firma_id, usermo_user_id, usermo_krit_art, usermo_krit_wert) VALUES ( ?, ?, 'E-Mail-Adresse Empfaenger', ? )
```

## DELETE_EMPAENGER

- Type: DELETE
- Tables: w_user_mailoptions
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
DELETE FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND  usermo_krit_art = 'E-Mail-Adresse Empfaenger'
```
