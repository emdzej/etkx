# Interpretation SQL Queries

Total queries: 3

## Table Relationships (Co-occurrence)

- w_hist ↔ w_teil ↔ w_ben_gk ↔ w_teil_marken

## EXIST_SACHNUMMER_MARKE

- Type: SELECT
- Tables: w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct teilm_marke_tps Marke from w_teil_marken where teilm_sachnr = '&SACHNUMMER&' and teilm_marke_tps IN (&MARKEN&)
```

## LOAD_INTERPRETATION_EINSTIEG

- Type: SELECT
- Tables: w_hist
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select hist_sachnr Sachnummer, hist_zweig_nr ZweigNr, hist_struktur_nr StrukturNr from w_hist where hist_sachnr_h = '&SACHNUMMER&'
```

## LOAD_INTERPRETATION

- Type: SELECT
- Tables: w_hist, w_teil, w_ben_gk, w_teil_marken
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teil_hauptgr Hg, teil_untergrup Ug, hist_sachnr_h Sachnummer, ben_text Benennung, teil_entfall_dat EntfallDatum, teil_bestellbar Bestellbar, teilm_marke_tps Marke, hist_zweig_nr ZweigNr, hist_struktur_nr StrukturNr from w_hist, w_teil, w_ben_gk, w_teil_marken where hist_sachnr = '&SACHNUMMER&' and hist_sachnr_h = teil_sachnr and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'  and teilm_sachnr = teil_sachnr order by ZweigNr, StrukturNr
```
