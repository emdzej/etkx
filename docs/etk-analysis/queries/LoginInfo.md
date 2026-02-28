# LoginInfo SQL Queries

Total queries: 6

## Table Relationships (Co-occurrence)

- w_user_log ↔ w_user

## LOAD_SINGLE_USER_LOGIN_INFO
**Description:** Retrieves data from —. Used in the LoginInfo module to support ETK workflows for — data.


- Type: SELECT
- Tables: —
- Parameterized: yes
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
SELECT\tuserlog_eingeloggt Eingeloggt, \t\t\t\tuserlog_lastlogin LastLogin, \t\t\t\tuserlog_anzahl_logins AnzahlLogins FROM \tw_user_log@etk_nutzer WHERE\tuserlog_firma_id = ? AND\t\tuserlog_user_id = ?
```

## INSERT_NEW_LOGIN_INFO
**Description:** Inserts records in w_user_log. Used in the LoginInfo module to support ETK workflows for user settings.


- Type: INSERT
- Tables: w_user_log
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_user_log@etk_nutzer ( \t\t\t\tuserlog_firma_id, \t\t\t\tuserlog_user_id, \t\t\t\tuserlog_eingeloggt, \t\t\t\tuserlog_lastlogin, \t\t\t\tuserlog_anzahl_logins ) VALUES ( ?, ?, 'J', ?, 1 )
```

## UPDATE_LOGIN_INFO
**Description:** Updates records in w_user_log filtered by userlog_user_id. Used in the LoginInfo module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user_log
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
UPDATE w_user_log@etk_nutzer SET    userlog_eingeloggt = 'J', \t\t\t\tuserlog_lastlogin = ?, \t\t\t\tuserlog_anzahl_logins = userlog_anzahl_logins + 1 WHERE\tuserlog_firma_id = ? AND    userlog_user_id = ?
```

## UPDATE_LOGIN_INFO_LOGOUT
**Description:** Updates records in w_user_log filtered by userlog_user_id. Used in the LoginInfo module to support ETK workflows for user settings.


- Type: UPDATE
- Tables: w_user_log
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
UPDATE w_user_log@etk_nutzer SET    userlog_eingeloggt = 'N' WHERE\tuserlog_firma_id = ? AND    userlog_user_id = ?
```

## GET_LOGGEDIN_USERS
**Description:** Retrieves data from w_user_log, w_user filtered by user_firma_id, user_id, NOT, userlog_firma_id, tuserlog_eingeloggt and ordered by UserId. Used in the LoginInfo module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user_log, w_user
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
SELECT userlog_user_id UserId,        user_name UserName, \t      userlog_lastlogin LastLogin, \t      userlog_anzahl_logins AnzahlLogins,       user_default_filiale_id DefaultFiliale FROM   w_user_log@etk_nutzer, w_user@etk_nutzer WHERE  user_firma_id = userlog_firma_id   AND  user_id = userlog_user_id   AND  user_id NOT IN (&IGNORE_USER&)   AND  userlog_firma_id = ?   AND \tuserlog_eingeloggt = 'J' ORDER BY UserId
```

## GET_INACTIVE_USERS
**Description:** Retrieves data from w_user filtered by user_firma_id, NOT, userlog_lastlog, userlog_lastlogin and ordered by UserId. Used in the LoginInfo module to support ETK workflows for user settings.


- Type: SELECT
- Tables: w_user
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: yes

```sql
SELECT user_id UserId,        user_name UserName, \t\t\t\tuserlog_lastlogin LastLogin, \t\t\t\tuserlog_anzahl_logins AnzahlLogins,        user_default_filiale_id DefaultFiliale FROM \tw_user_log@etk_nutzer \t\t\t\tRIGHT OUTER JOIN w_user@etk_nutzer \t\t\t\tON (user_firma_id = userlog_firma_id \t\t\t\t\tAND user_id = userlog_user_id) WHERE  user_firma_id = ?   AND  user_id NOT IN (&IGNORE_USER&)   AND  (userlog_lastlogin IS NULL    OR   userlog_lastlogin < ?) ORDER BY UserId
```
