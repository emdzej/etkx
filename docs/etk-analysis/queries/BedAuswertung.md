# BedAuswertung SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_bildtaf ↔ w_grafik ↔ w_bildtafzub ↔ w_ben_gk
- w_bte_bedkurz ↔ w_bildtaf ↔ w_ben_gk ↔ w_bed ↔ w_eg ↔ w_bte_bedelem
- w_bte_bedkurz ↔ w_bildtaf ↔ w_ben_gk ↔ w_bed_afl ↔ w_bed ↔ w_eg ↔ w_bte_bedelem
- w_bte_bedkurz ↔ w_bildtaf ↔ w_ben_gk ↔ w_bed_sala ↔ w_bed ↔ w_eg ↔ w_bte_bedelem

## LOAD_BT_STAMM
**Description:** Retrieves data from w_bildtaf, w_grafik, w_bildtafzub, w_ben_gk filtered by bildtaf_grafikid, ben_iso, ben_regiso. Used in the BedAuswertung module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf, w_grafik, w_bildtafzub, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bildtaf_hg HG, bildtaf_fg FG, bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_bedkez BedKuerzel, bildtaf_vorh_cp VorhandenCP, bildtaf_lkz Lkz, bildtaf_grafikid GrafikId, grafik_moddate ModStamp, bildtafz_btnr ZubBtnr from w_bildtaf left join w_grafik on (bildtaf_grafikid = grafik_grafikid) left join w_bildtafzub on (bildtaf_btnr = bildtafz_btnr) inner join w_ben_gk on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') where bildtaf_btnr = '&BTNR&'
```

## LOAD_BT_STAMM_CP
**Description:** Retrieves data from w_bildtaf_cp filtered by bildtafc_btnr and ordered by Art, Datum. Used in the BedAuswertung module to support ETK workflows for illustration plates.


- Type: SELECT
- Tables: w_bildtaf_cp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct bildtafc_art Art, bildtafc_datum Datum from w_bildtaf_cp where bildtafc_btnr = '&BTNR&' order by Art, Datum
```

## LOAD_BT_BEDINGUNGEN_SALA
**Description:** Retrieves data from w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem filtered by btebk_btnr, btebk_mospid, btebk_kez, btebe_elemid, bed_elemid, bed_egid, bed_textcode, ben_iso, ben_regiso, bildtaf_btnr, bildtaf_bedkez and ordered by EGruppenPosition, PrimaNr. Used in the BedAuswertung module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebe_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bte_bedkurz, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedsala_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bildtaf, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedsala_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, PrimaNr
```

## LOAD_BT_BEDINGUNGEN_AFL
**Description:** Retrieves data from w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem filtered by btebk_btnr, btebk_mospid, btebk_kez, btebe_elemid, bed_elemid, bed_egid, bed_textcode, ben_iso, ben_regiso, bildtaf_btnr, bildtaf_bedkez and ordered by EGruppenPosition, Code. Used in the BedAuswertung module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebe_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bte_bedkurz, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedafl_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bildtaf, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedafl_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, Code
```

## LOAD_BT_BEDINGUNGEN_TEXT
**Description:** Retrieves data from w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed, w_eg, w_bte_bedelem filtered by btebk_btnr, btebk_mospid, btebk_kez, btebe_elemid, bed_ogid, bed_egid, bed_textcode, ben_iso, ben_regiso, bildtaf_btnr, bildtaf_bedkez and ordered by EGruppenPosition, Benennung. Used in the BedAuswertung module to support ETK workflows for option codes.


- Type: SELECT
- Tables: w_bte_bedkurz, w_bildtaf, w_ben_gk, w_bed, w_eg, w_bte_bedelem
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct btebe_elemid ElementId, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bte_bedkurz, w_ben_gk, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_ogid = 'TEXT' and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bildtaf, w_ben_gk, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_ogid = 'TEXT' and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, Benennung
```
