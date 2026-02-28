# NotizuebersichtJAVA SQL Queries

Total queries: 4

## Table Relationships (Co-occurrence)

- w_teileinfo ↔ w_publben ↔ w_ben_gk ↔ w_teil
- w_teileinfo ↔ w_teil

## RETRIEVE_ANZ_NOTIZEN

- Type: SELECT
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count (distinct teileinfo_sachnr) ANZ from w_teileinfo@etk_nutzer where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&'
```

## RETRIEVE_ANZ_NOTIZEN_ZU_SACHNUMMER

- Type: SELECT
- Tables: w_teileinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count (distinct teileinfo_sachnr) ANZ from w_teileinfo@etk_nutzer where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = '&SACHNUMMER&'
```

## RETRIEVE_MIN_HG

- Type: SELECT
- Tables: w_teileinfo, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select min(teil_hauptgr) HG from w_teileinfo@etk_nutzer, w_teil where teileinfo_user_id =  '&NUTZER&' and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = teil_sachnr
```

## RETRIEVE_NOTIZEN

- Type: SELECT
- Tables: w_teileinfo, w_publben, w_ben_gk, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, TBEN.ben_text BENENNUNG, MBEN.ben_text MONAT, teileinfo_gueltig_bis_jahr JAHR from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN, w_ben_gk TBEN, w_teil where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = teil_sachnr &HG_STMT& and teil_textcode = TBEN.ben_textcode and TBEN.ben_iso = '&ISO&' and TBEN.ben_regiso = '&REGISO&' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&' order by HG, UG, SACHNR
```
