# Hilfe SQL Queries

Total queries: 4

## Table Relationships (Co-occurrence)

- w_abk ↔ w_ben_gk
- w_bed_sala ↔ w_ben_gk ↔ w_bed
- w_bedeutung ↔ w_ben_gk ↔ w_komm

## RETRIEVE_ABKUERZUNGEN
**Description:** Retrieves data from w_abk, w_ben_gk filtered by abk_textcode, ben_iso, ben_regiso and ordered by ABKUERZUNG. Used in the Hilfe module to support ETK workflows for abk data.


- Type: SELECT
- Tables: w_abk, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select abk_kuerzel ABKUERZUNG, abk_bedeutung BEDEUTUNG, ben_text UEBERSETZUNG from w_abk, w_ben_gk where abk_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ABKUERZUNG
```

## RETRIEVE_VERSIONSINFO
**Description:** Retrieves data from w_verwaltung. Used in the Hilfe module to support ETK workflows for verwaltung data.


- Type: SELECT
- Tables: w_verwaltung
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select verwaltung_info Info, verwaltung_wert Wert from w_verwaltung
```

## RETRIEVE_BEDEUTUNGEN
**Description:** Retrieves data from w_bedeutung, w_ben_gk, w_komm filtered by bedeutung_art, bedeutung_kommid, komm_textcode, ben_iso, ben_regiso and ordered by WERT, POS. Used in the Hilfe module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bedeutung, w_ben_gk, w_komm
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select bedeutung_wert WERT, ben_text BEDEUTUNG, komm_pos POS from w_bedeutung, w_ben_gk, w_komm where bedeutung_art = '&ART&' and bedeutung_kommid = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by WERT, POS
```

## RETRIEVE_SALAPAS
**Description:** Retrieves data from w_bed_sala, w_ben_gk, w_bed filtered by bedsala_art, bedsala_id, bed_textcode, ben_iso, ben_regiso and ordered by Code. Used in the Hilfe module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bed_sala, w_ben_gk, w_bed
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung from w_bed_sala, w_ben_gk, w_bed where bedsala_art IN (&ARTEN&) and bedsala_id = bed_elemid and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Code
```
