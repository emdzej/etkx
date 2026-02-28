# TeileinfoJAVA SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_teileinfo ↔ w_publben ↔ w_ben_gk

## RETRIEVE_TEILEINFO_NOTIZ

- Type: SELECT
- Tables: w_teileinfo, w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teileinfo_notiz Notiz, teileinfo_gueltig_bis_monat BisMonat, teileinfo_gueltig_bis_jahr BisJahr, MBEN.ben_text MONATBEN, teileinfo_allgemein Allgemein from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&' and teileinfo_firma_id = '&FIRMA&' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&'
```

## DELETE_TEILEINFO_NOTIZ

- Type: DELETE
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&' and teileinfo_firma_id = '&FIRMA&'
```

## INSERT_TEILEINFO_NOTIZ

- Type: INSERT
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_teileinfo@etk_nutzer (teileinfo_sachnr, teileinfo_user_id, teileinfo_firma_id, teileinfo_allgemein, teileinfo_notiz, teileinfo_gueltig_bis_monat, teileinfo_gueltig_bis_jahr) values ('&SACHNR&', '&ID&', '&FIRMA&', '&ALLGEMEIN&', '&NOTIZTEXT&', &BISMONAT&, &BISJAHR&)
```

## RETRIEVE_TEILEINFO_NOTIZEN_OTHERS

- Type: SELECT
- Tables: w_teileinfo, w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teileinfo_notiz Notiz, MBEN.ben_text Monat, teileinfo_gueltig_bis_jahr BisJahr from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id <> '&ID&' and teileinfo_firma_id = '&FIRMA&' and teileinfo_allgemein = 'J' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&'
```

## RETRIEVE_COUNT_PREISE

- Type: SELECT
- Tables: w_preise
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count (*) from w_preise@etk_preise where preise_firma = '&FIRMA&'
```
