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
**Description:** Retrieves data from w_baureihe, w_fztyp filtered by baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_sichtschutz and ordered by Region. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_ktlgausf Region from w_baureihe, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' order by Region
```

## RETRIEVE_LENKUNGEN
**Description:** Retrieves data from w_baureihe, w_ben_gk, w_publben, w_fztyp filtered by baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_lenkung, publben_art, publben_textcode, ben_iso, ben_regiso and ordered by Lenkung. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_ben_gk, w_publben, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_lenkung Lenkung, ben_text ExtLenkung from w_baureihe, w_ben_gk, w_publben, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'P' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung = publben_bezeichnung and publben_art = 'L' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Lenkung
```

## RETRIEVE_BAUARTEN
**Description:** Retrieves data from w_baureihe, w_bauart, w_ben_gk, w_fztyp filtered by baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_sichtschutz, bauart_bauart, ben_textcode, ben_iso, ben_regiso and ordered by Pos. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_bauart, w_ben_gk, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_bauart Bauart, ben_text ExtBauart, bauart_position Pos from w_baureihe, w_bauart, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'M' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' and bauart_bauart = baureihe_bauart and ben_textcode = bauart_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## RETRIEVE_BAUREIHEN
**Description:** Retrieves data from w_baureihe, w_ben_gk, w_fztyp filtered by baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_sichtschutz, baureihe_textcode, ben_iso, ben_regiso and ordered by Pos. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_ben_gk, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe Baureihe, ben_text ExtBaureihe, baureihe_position Pos from w_baureihe, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') &BAUART_STMT& and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
```

## RETRIEVE_KAROSSERIEN
**Description:** Retrieves data from w_fztyp, w_ben_gk, w_publben filtered by fztyp_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_lenkung, fztyp_sichtschutz, fztyp_karosserie, publben_art, publben_textcode, ben_iso, ben_regiso and ordered by ExtKarosserie. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_karosserie Karosserie, ben_text ExtKarosserie from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe IN (&BAUREIHEN&) and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung IN (&LENKUNGEN&) and fztyp_sichtschutz = 'N' and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ExtKarosserie
```

## RETRIEVE_MODELLE
**Description:** Retrieves data from w_vbez_pos, w_baureihe, w_fztyp filtered by baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_sichtschutz, vbezp_baureihe, vbezp_vbez and ordered by Pos, Modell. Used in the FzgUmfang module to support ETK workflows for vbez pos data.


- Type: SELECT
- Tables: w_vbez_pos, w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_erwvbez Modell, vbezp_pos Pos from w_vbez_pos, w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and vbezp_baureihe = baureihe_baureihe and vbezp_vbez = fztyp_vbez order by Pos, Modell
```

## RETRIEVE_MODELLSPALTEN
**Description:** Retrieves data from w_baureihe, w_fztyp filtered by baureihe_baureihe, fztyp_vbereich, fztyp_ktlgausf, fztyp_sichtschutz and ordered by MospID. Used in the FzgUmfang module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_mospid MospID from w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &MODELL_STMT& &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' order by MospID
```
