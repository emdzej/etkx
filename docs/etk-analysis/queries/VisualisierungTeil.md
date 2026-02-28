# VisualisierungTeil SQL Queries

Total queries: 2

## Table Relationships (Co-occurrence)

- w_btzeilen_verbauung ↔ w_grafik ↔ w_baureihe ↔ w_fztyp ↔ w_ben_gk ↔ w_teil ↔ w_bildtaf ↔ w_btzeilen
- w_btzeilenugb ↔ w_grafik ↔ w_ben_gk ↔ w_teil ↔ w_bildtaf ↔ w_btzeilenugb_verbauung

## RETRIEVE_VISUALISIERUNGSINFO_GEB
**Description:** Retrieves data from w_btzeilen_verbauung, w_grafik, w_baureihe, w_fztyp, w_ben_gk, w_teil, w_bildtaf, w_btzeilen filtered by btzeilenv_sachnr, btzeilenv_mospid, fztyp_baureihe, baureihe_marke_tps, btzeilenv_btnr, bildtaf_produktart, btzeilenv_pos, teil_textcode, ben_iso, ben_regiso, bildtaf_textc, bildtaf_grafikid, grafik_art. Used in the VisualisierungTeil module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_grafik, w_baureihe, w_fztyp, w_ben_gk, w_teil, w_bildtaf, w_btzeilen
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, t.ben_text Teilebenennung, teil_benennzus TeilebenennungZusatz, bildtaf_btnr BildtafelNummer, bt.ben_text BildtafelUeberschrift, btzeilen_bildposnr Bildnummer, bildtaf_grafikid GrafikId, grafik_format GrafikFormat, grafik_moddate GrafikTimestamp from w_btzeilen_verbauung, w_grafik, w_baureihe, w_fztyp, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilen where btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_mospid = fztyp_mospid and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps in ('&MARKE&') and btzeilenv_btnr = bildtaf_btnr and bildtaf_produktart in ('&PRODUKTART&') and btzeilenv_sachnr = teil_sachnr and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos  and teil_textcode = t.ben_textcode and t.ben_iso = '&ISO&' and t.ben_regiso = '&REGISO&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' and bildtaf_grafikid = grafik_grafikid and grafik_art = 'Z'
```

## RETRIEVE_VISUALISIERUNGSINFO_UGB
**Description:** Retrieves data from w_btzeilenugb, w_grafik, w_ben_gk, w_teil, w_bildtaf, w_btzeilenugb_verbauung filtered by btzeilenu_sachnr, btzeilenu_btnr, btzeilenu_pos, btzeilenuv_marke_tps, bildtaf_produktart, teil_textcode, ben_iso, ben_regiso, bildtaf_textc, bildtaf_grafikid, grafik_art. Used in the VisualisierungTeil module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilenugb, w_grafik, w_ben_gk, w_teil, w_bildtaf, w_btzeilenugb_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, t.ben_text Teilebenennung, teil_benennzus TeilebenennungZusatz, bildtaf_btnr BildtafelNummer, bt.ben_text BildtafelUeberschrift, btzeilenu_bildposnr Bildnummer, bildtaf_grafikid GrafikId, grafik_format GrafikFormat, grafik_moddate GrafikTimestamp from w_btzeilenugb, w_grafik, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilenugb_verbauung where btzeilenu_sachnr = '&SACHNUMMER&' and btzeilenu_btnr = btzeilenuv_btnr and btzeilenu_pos = btzeilenuv_pos and btzeilenuv_marke_tps in ('&MARKE&') and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart in ('&PRODUKTART&') and btzeilenu_sachnr = teil_sachnr and teil_textcode = t.ben_textcode and t.ben_iso = '&ISO&' and t.ben_regiso = '&REGISO&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' and bildtaf_grafikid = grafik_grafikid and grafik_art = 'Z'
```
