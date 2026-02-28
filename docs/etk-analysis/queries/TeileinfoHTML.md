# TeileinfoHTML SQL Queries

Total queries: 3

## RETRIEVE_TEILEINFO_NOTIZ
**Description:** Retrieves data from w_teileinfo filtered by teileinfo_sachnr, teileinfo_user_id. Used in the TeileinfoHTML module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select teileinfo_notiz Notiz, teileinfo_gueltig_bis_monat BisMonat, teileinfo_gueltig_bis_jahr BisJahr from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&'
```

## DELETE_TEILEINFO_NOTIZ
**Description:** Deletes records from w_teileinfo filtered by teileinfo_sachnr, teileinfo_user_id. Used in the TeileinfoHTML module to support ETK workflows for part data.


- Type: DELETE
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&'
```

## INSERT_TEILEINFO_NOTIZ
**Description:** Inserts records in w_teileinfo. Used in the TeileinfoHTML module to support ETK workflows for part data.


- Type: INSERT
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileinfo@etk_nutzer (teileinfo_sachnr, teileinfo_user_id, teileinfo_notiz, teileinfo_gueltig_bis_monat, teileinfo_gueltig_bis_jahr) values ('&SACHNR&', '&ID&', '&NOTIZTEXT&', &BISMONAT&, &BISJAHR&)
```
