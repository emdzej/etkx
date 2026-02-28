# TeilevwdgTeil SQL Queries

Total queries: 4

## Table Relationships (Co-occurrence)

- w_btzeilen_verbauung ↔ w_btzeilen ↔ w_ben_gk ↔ w_baureihe ↔ w_fztyp
- w_fztyp ↔ w_markt_etk ↔ w_ben_gk ↔ w_publben ↔ w_baureihe ↔ w_btzeilen ↔ w_btzeilen_verbauung ↔ w_bildtaf
- w_teil ↔ w_teil_marken ↔ w_ben_gk

## RETRIEVE_TEIL_ZU_MARKE_PROD
**Description:** Retrieves data from w_teil, w_teil_marken, w_ben_gk filtered by teil_sachnr, teil_produktart, teilm_sachnr, teilm_marke_tps, teil_textcode, ben_iso, ben_regiso. Used in the TeilevwdgTeil module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil, w_teil_marken, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr HG, teil_untergrup UG, ben_text BEN    , teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk where teil_sachnr = '&SACHNUMMER&' and (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and teilm_sachnr = teil_sachnr and teilm_marke_tps = '&MARKE&' and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## RETRIEVE_TEIL
**Description:** Retrieves data from w_teil, w_teil_marken, w_ben_gk filtered by teil_sachnr, teil_textcode, ben_iso, ben_regiso. Used in the TeilevwdgTeil module to support ETK workflows for part data.


- Type: SELECT
- Tables: w_teil, w_teil_marken, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr HG,  teil_untergrup UG,  teilm_marke_tps MARKE,  teil_produktart PRODUKTART,  ben_text BEN,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk  where teil_sachnr = '&SACHNUMMER&' and  teil_textcode = ben_textcode  and ben_iso = '&ISO&'  and ben_regiso = '&REGISO&' and teil_sachnr = teilm_sachnr
```

## RETRIEVE_BAUREIHEN
**Description:** Retrieves data from w_btzeilen_verbauung, w_btzeilen, w_ben_gk, w_baureihe, w_fztyp filtered by btzeilenv_sachnr, btzeilen_btnr, btzeilen_pos, btzeilen_bildposnr, btzeilenv_mospid, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_baureihe, baureihe_marke_tps, baureihe_produktart, baureihe_vbereich, baureihe_textcode, ben_iso, ben_regiso and ordered by POS. Used in the TeilevwdgTeil module to support ETK workflows for parts list lines.


- Type: SELECT
- Tables: w_btzeilen_verbauung, w_btzeilen, w_ben_gk, w_baureihe, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from w_btzeilen_verbauung, w_btzeilen, w_ben_gk, w_baureihe, w_fztyp where btzeilenv_sachnr = '&SACHNUMMER&' &MODELLSPALTEN_STMT&  and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_bildposnr <> '--' and btzeilenv_mospid = fztyp_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by POS
```

## RETRIEVE_MODELLSPALTEN
**Description:** Retrieves data from w_fztyp, w_markt_etk, w_ben_gk, w_publben, w_baureihe, w_btzeilen, w_btzeilen_verbauung, w_bildtaf filtered by marktetk_lkz, fztyp_vbereich, fztyp_sichtschutz, fztyp_ktlgausf, fztyp_mospid, btzeilenv_sachnr, btzeilen_btnr, btzeilen_pos, btzeilen_bildposnr, btzeilenv_btnr, fztyp_baureihe, fztyp_karosserie, publben_art, publben_textcode, ben_iso, ben_regiso, bildtaf_textc and ordered by MODELL, BTNR, KAROSSERIE, REGION. Used in the TeilevwdgTeil module to support ETK workflows for vehicle identification.


- Type: SELECT
- Tables: w_fztyp, w_markt_etk, w_ben_gk, w_publben, w_baureihe, w_btzeilen, w_btzeilen_verbauung, w_bildtaf
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btzeilenv_vmenge MENGE, btzeilenv_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_fztyp, w_ben_gk B, w_ben_gk K, w_publben, w_baureihe, w_btzeilen, w_btzeilen_verbauung, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_mospid = btzeilenv_mospid &MODELLSPALTEN_STMT&  and btzeilenv_sachnr = '&SACHNUMMER&' and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_bildposnr <> '--' and btzeilenv_btnr = bildtaf_btnr and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by MODELL, BTNR, KAROSSERIE, REGION
```
