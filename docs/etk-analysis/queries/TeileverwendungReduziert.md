# TeileverwendungReduziert SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_btzeilen_verbauung ↔ w_ben_gk ↔ w_baureihe ↔ w_publben ↔ w_fztyp
- w_teil ↔ w_ben_gk

## RETRIEVE_TEIL
**Description:** Retrieves data from w_teil, w_ben_gk filtered by teil_sachnr, teil_textcode, ben_iso, ben_regiso. Used in the TeileverwendungReduziert module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Ben, teil_benennzus Zusatz from w_teil, w_ben_gk where teil_sachnr = '&SACHNUMMER&' and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## RETRIEVE_VERWENDUNG
**Description:** Retrieves data from w_btzeilen_verbauung, w_ben_gk, w_baureihe, w_publben, w_fztyp filtered by btzeilenv_sachnr, btzeilenv_mospid, fztyp_sichtschutz, fztyp_baureihe, baureihe_textcode, ben_iso, ben_regiso, publben_art, fztyp_karosserie, publben_textcode and ordered by Baureihe, Modell, Karosserie, Region. Used in the TeileverwendungReduziert module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_ben_gk, w_baureihe, w_publben, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct BR.ben_text Baureihe, fztyp_erwvbez Modell, KAR.ben_text Karosserie, fztyp_ktlgausf Region from w_btzeilen_verbauung, w_ben_gk BR, w_ben_gk KAR, w_baureihe, w_publben, w_fztyp where btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_mospid IN (&MOSPIDS&) and btzeilenv_mospid = fztyp_mospid and fztyp_sichtschutz = 'N' and fztyp_baureihe = baureihe_baureihe and baureihe_textcode = BR.ben_textcode and BR.ben_iso = '&ISO&' and BR.ben_regiso = '&REGISO&' and publben_art = 'K' and fztyp_karosserie = publben_bezeichnung and publben_textcode = KAR.ben_textcode and KAR.ben_iso = '&ISO&' and KAR.ben_regiso = '&REGISO&' order by Baureihe, Modell, Karosserie, Region
```
