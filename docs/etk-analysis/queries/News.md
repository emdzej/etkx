# News SQL Queries

Total queries: 9

## INSERT_NEWSTEXT

- Type: INSERT
- Tables: w_news_text
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_news_text (NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_TEXT, NEWST_AKTUELL, NEWST_STANDARD) values ('&MARKE&','&ISO&','&REGISO&','&TEXT&','&AKTUELL&','&STANDARD&')
```

## UPDATE_NEWSTEXT

- Type: UPDATE
- Tables: w_news_text
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_news_text set NEWST_TEXT = '&TEXT&', NEWST_AKTUELL = '&AKTUELL&', NEWST_STANDARD = '&STANDARD&' where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&'
```

## LOAD_NEWSTEXTE

- Type: SELECT
- Tables: w_news_text
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select NEWST_TEXT Text, NEWST_AKTUELL IsAktiviert, NEWST_STANDARD IsStandard from w_news_text where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' order by NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_STANDARD
```

## DELETE_NEWSTEXT

- Type: DELETE
- Tables: w_news_text
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_news_text where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' and NEWST_TEXT = '&TEXT&'
```

## UPDATE_NEWSTEXT_AKTIVIERUNG

- Type: UPDATE
- Tables: w_news_text
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
update w_news_text set NEWST_AKTUELL = '&AKTUELL&' where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' and NEWST_TEXT = '&TEXT&'
```

## DELETE_IMAGE

- Type: DELETE
- Tables: w_news_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_news_grafik where newsg_marke_tps = '&MARKE&'
```

## INSERT_IMAGE

- Type: INSERT
- Tables: w_news_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: no | Sorting: no

```sql
insert into w_news_grafik values ( '&MARKE&', EMPTY_BLOB() )
```

## LOAD_IMAGE

- Type: SELECT
- Tables: w_news_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select newsg_grafik Grafik from w_news_grafik where newsg_marke_tps = '&MARKE&'
```

## UPDATE_IMAGE

- Type: SELECT
- Tables: w_news_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select newsg_grafik from w_news_grafik where newsg_marke_tps = '&MARKE&' for update
```
