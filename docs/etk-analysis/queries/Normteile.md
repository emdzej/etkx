# Normteile SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_normnummer ↔ w_grafik
- w_normnummergruppe ↔ w_grafik
- w_normteilben ↔ w_ben_gk
- w_teil ↔ w_ben_gk ↔ w_teil_marken ↔ w_normnummer

## RETRIEVE_BENENNUNGEN

- Type: SELECT
- Tables: w_normteilben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct ben_text BENENNUNG, normteilben_textcode TEXTCODE from w_normteilben, w_ben_gk where normteilben_marke_tps = '&MARKE&' and (normteilben_produktart = '&PRODUKTART&' or normteilben_produktart = 'B') and normteilben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by BENENNUNG
```

## RETRIEVE_TEILE_ZU_BENENNUNG

- Type: SELECT
- Tables: w_teil, w_ben_gk, w_teil_marken, w_normnummer
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct ben_text BENENNUNG, teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER, teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART, teilm_marke_tps MARKE,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps) where (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10')) and (teil_lkz = '   ' or teil_lkz IS NULL) and teil_technik IN ('0', '3', '4', '7') and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > &COMPARE_DATE& )) and &TEXTCODE_STMT& order by ZUSATZ, NORMNUMMER
```

## RETRIEVE_TEILE_ZU_NORMNUMMER

- Type: SELECT
- Tables: w_teil, w_ben_gk, w_teil_marken, w_normnummer
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER, ben_text BENENNUNG, teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART, teilm_marke_tps MARKE,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps) where (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and UPPER(teil_normnummer) = UPPER('&NORMNUMMER&') and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10')) and (teil_lkz = '   ' or teil_lkz IS NULL) and teil_technik IN ('0', '3', '4', '7') and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > &COMPARE_DATE& )) order by ZUSATZ, NORMNUMMER
```

## RETRIEVE_NORMNUMMERN_GRUPPEN

- Type: SELECT
- Tables: w_normnummergruppe, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select nng_nngid NUMMER, nng_grafikid GRAFIKID, nng_pos POS, grafik_moddate TS from w_normnummergruppe, w_grafik where nng_marke_tps = '&MARKE&' and (nng_produktart = '&PRODUKTART&' or nng_produktart = 'B') and nng_grafikid = grafik_grafikid and grafik_art = 'T' order by POS
```

## RETRIEVE_NORMNUMMERN

- Type: SELECT
- Tables: w_normnummer, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select nn_nnid NUMMER, nn_art ART, nn_grafikid GRAFIKID, nn_pos POS, grafik_moddate TS from w_normnummer, w_grafik where nn_marke_tps = '&MARKE&' and (nn_produktart = '&PRODUKTART&' or nn_produktart = 'B') and nn_nngid = '&NORMNUMMERNGRUPPE&' and nn_grafikid = grafik_grafikid and grafik_art = 'T' order by POS
```
