# FzgUmfang SQL Queries

Total queries: 7

## Table Relationships (Co-occurrence)

- w_baureihe ↔ w_bauart ↔ w_ben_gk ↔ w_fztyp
- w_baureihe ↔ w_ben_gk ↔ w_fztyp
- w_baureihe ↔ w_ben_gk ↔ w_publben ↔ w_fztyp
- w_baureihe ↔ w_fztyp
- w_fztyp ↔ w_ben_gk ↔ w_publben
- w_vbez_pos ↔ w_baureihe ↔ w_fztyp

## RETRIEVE_REGIONEN

- Type: SELECT
- Tables: w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_ktlgausf Region from w_baureihe, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' order by Region
```

## RETRIEVE_LENKUNGEN

- Type: SELECT
- Tables: w_baureihe, w_ben_gk, w_publben, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_lenkung Lenkung, ben_text ExtLenkung from w_baureihe, w_ben_gk, w_publben, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'P' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung = publben_bezeichnung and publben_art = 'L' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Lenkung
```

## RETRIEVE_BAUARTEN

- Type: SELECT
- Tables: w_baureihe, w_bauart, w_ben_gk, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_bauart Bauart, ben_text ExtBauart, bauart_position Pos from w_baureihe, w_bauart, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'M' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' and bauart_bauart = baureihe_bauart and ben_textcode = bauart_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## RETRIEVE_BAUREIHEN

- Type: SELECT
- Tables: w_baureihe, w_ben_gk, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe Baureihe, ben_text ExtBaureihe, baureihe_position Pos from w_baureihe, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') &BAUART_STMT& and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## RETRIEVE_KAROSSERIEN

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_karosserie Karosserie, ben_text ExtKarosserie from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe IN (&BAUREIHEN&) and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung IN (&LENKUNGEN&) and fztyp_sichtschutz = 'N' and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ExtKarosserie
```

## RETRIEVE_MODELLE

- Type: SELECT
- Tables: w_vbez_pos, w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_erwvbez Modell, vbezp_pos Pos from w_vbez_pos, w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and vbezp_baureihe = baureihe_baureihe and vbezp_vbez = fztyp_vbez order by Pos, Modell
```

## RETRIEVE_MODELLSPALTEN

- Type: SELECT
- Tables: w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_mospid MospID from w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &MODELL_STMT& &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' order by MospID
```
