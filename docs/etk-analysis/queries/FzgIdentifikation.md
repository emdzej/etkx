# FzgIdentifikation SQL Queries

Total queries: 25

## Table Relationships (Co-occurrence)

- _publben ↔ w_ben_gk
- w_baureihe ↔ w_bauart ↔ w_ben_gk ↔ w_fztyp
- w_baureihe ↔ w_ben_gk ↔ w_fztyp
- w_baureihe ↔ w_grafik
- w_baureihe_kar_thb ↔ w_grafik
- w_bed_afl ↔ w_ben_gk ↔ w_bed ↔ w_eg
- w_bed_sala ↔ w_ben_gk ↔ w_bed ↔ w_eg
- w_etk_grafiken ↔ w_grafik
- w_fgstnr ↔ w_fztyp ↔ w_baureihe ↔ w_publben ↔ w_ben_gk
- w_fgstnr_sala ↔ w_bed_sala ↔ w_bed ↔ w_ben_gk ↔ w_eg ↔ w_bed_zusatzinfo
- w_fztyp ↔ w_baureihe ↔ w_publben ↔ w_ben_gk
- w_fztyp ↔ w_ben_gk ↔ w_publben
- w_fztyp ↔ w_ben_gk ↔ w_publben ↔ w_fgstnr
- w_fztyp ↔ w_fgstnr
- w_komm ↔ w_bed_zusatzinfo ↔ w_ben_gk
- w_vbez_pos ↔ w_fztyp

## RETRIEVE_BAUARTEN

- Type: SELECT
- Tables: w_baureihe, w_bauart, w_ben_gk, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct baureihe_bauart Bauart, ben_text ExtBauart, bauart_position Pos from w_baureihe, w_bauart, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and fztyp_baureihe = baureihe_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' and bauart_bauart = baureihe_bauart and ben_textcode = bauart_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos
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

## GET_GRAFIKID_FOR_BAUREIHE

- Type: SELECT
- Tables: w_baureihe, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select grafik_grafikid GrafikId, grafik_moddate ModStamp from w_baureihe, w_grafik where baureihe_baureihe = '&BAUREIHE&' and baureihe_grafikid = grafik_grafikid and grafik_art = 'T'
```

## GET_GRAFIKID_FOR_BAUREIHE_KAROSSERIE

- Type: SELECT
- Tables: w_baureihe_kar_thb, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select grafik_grafikid GrafikId, grafik_moddate ModStamp from w_baureihe_kar_thb, w_grafik where baureihekar_baureihe = '&BAUREIHE&' and baureihekar_karosserie = '&KAROSSERIE&' and baureihekar_grafikid = grafik_grafikid and grafik_art = 'T'
```

## GET_GRAFIKID_FOR_FIBILD

- Type: SELECT
- Tables: w_etk_grafiken, w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select grafik_grafikid GrafikId, grafik_format Format, grafik_moddate ModStamp from w_etk_grafiken, w_grafik where etkgraf_ablauf = 'FI' and etkgraf_marke = '&MARKE&' and etkgraf_produktart = '&PRODUKTART&' and etkgraf_vbereich = '&KATALOGUMFANG&' and etkgraf_grafikid = grafik_grafikid and grafik_art = 'T'
```

## RETRIEVE_KAROSSERIEN

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_karosserie Karosserie, ben_text ExtKarosserie from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and publben_art = 'K' and fztyp_karosserie = publben_bezeichnung and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ExtKarosserie
```

## RETRIEVE_MODELLE

- Type: SELECT
- Tables: w_vbez_pos, w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_erwvbez Modell, vbezp_pos Pos from w_vbez_pos, w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and vbezp_baureihe = fztyp_baureihe and vbezp_vbez = fztyp_vbez order by Pos, Modell
```

## RETRIEVE_REGIONEN

- Type: SELECT
- Tables: w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: yes

```sql
select distinct fztyp_ktlgausf Region from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' order by Region
```

## RETRIEVE_LENKUNGEN

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_lenkung Lenkung, ben_text ExtLenkung from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' and fztyp_sichtschutz = 'N' and fztyp_lenkung = publben_bezeichnung and publben_art = 'L' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Lenkung
```

## RETRIEVE_GETRIEBEARTEN

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct fztyp_getriebe Getriebe, ben_text ExtGetriebe from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and fztyp_getriebe = publben_bezeichnung and publben_art = 'G' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Getriebe
```

## RETRIEVE_BAUJAHRE

- Type: SELECT
- Tables: w_fztyp, w_fgstnr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct substr(to_char(fgstnr_prod), 1, 4) Baujahr from w_fztyp, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl order by Baujahr
```

## RETRIEVE_ZULASSMONATE

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben, w_fgstnr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct substr(to_char(fgstnr_prod), 5, 2) Zulassungsmonat, ben_text ExtZulassungsmonat from w_fztyp, w_ben_gk, w_publben, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl and substr(to_char(fgstnr_prod), 1, 4) = '&BAUJAHR&' and substr(to_char(fgstnr_prod), 5, 2) = publben_bezeichnung and publben_art = 'M' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Zulassungsmonat
```

## RETRIEVE_ZULASSMONATE2

- Type: SELECT
- Tables: w_fztyp, w_ben_gk, w_publben, w_fgstnr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct substr(to_char(fgstnr_prod), 5, 2) Zulassungsmonat, ben_text ExtZulassungsmonat from w_fztyp, w_ben_gk, w_publben, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl and substr(to_char(fgstnr_prod), 5, 2) = publben_bezeichnung and publben_art = 'M' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Zulassungsmonat
```

## RETRIEVE_MOSP_BY_ATTRIBUTE_PKW

- Type: SELECT
- Tables: w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct fztyp_mospid Modellspalte from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&'
```

## RETRIEVE_MOSP_BY_ATTRIBUTE_KRAD

- Type: SELECT
- Tables: w_fztyp
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct fztyp_mospid Modellspalte from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&'
```

## RETRIEVE_MOSP_BY_FGSTNR

- Type: SELECT
- Tables: w_fgstnr, w_fztyp, w_baureihe, w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct fgstnr_mospid Modellspalte, fgstnr_typschl Typ, fgstnr_werk Werk, baureihe_marke_tps Marke, baureihe_produktart Produktart, fztyp_vbereich Katalogumfang, fztyp_baureihe Baureihe, b.ben_text ExtBaureihe, baureihe_bauart Bauart, bb.ben_text ExtBauart, fztyp_karosserie Karosserie, bk.ben_text ExtKarosserie, fztyp_motor Motor, fztyp_erwvbez Modell, fztyp_ktlgausf Region, fztyp_lenkung Lenkung, fztyp_getriebe Getriebe, fgstnr_prod Produktionsdatum, fztyp_sichtschutz Sichtschutz, NVL(fztyp_einsatz, 0) Einsatz from w_fgstnr inner join w_fztyp on (fgstnr_typschl = fztyp_typschl and fgstnr_mospid = fztyp_mospid) inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe) inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K') inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B') inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = '&ISO&' and b.ben_regiso = '&REGISO&') inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = '&ISO&' and bk.ben_regiso = '&REGISO&') inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = '&ISO&' and bb.ben_regiso = '&REGISO&') where fgstnr_von <= '&FGSTNR&' and fgstnr_bis >= '&FGSTNR&' and fgstnr_anf  = '&FGSTNR_FIRST2&'
```

## RETRIEVE_MOSP_BY_TYP

- Type: SELECT
- Tables: w_fztyp, w_baureihe, w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct fztyp_mospid Modellspalte, fztyp_typschl Typ, null Werk, baureihe_marke_tps Marke, baureihe_produktart Produktart, fztyp_vbereich Katalogumfang, fztyp_baureihe Baureihe, b.ben_text ExtBaureihe, baureihe_bauart Bauart, bb.ben_text ExtBauart, fztyp_karosserie Karosserie, bk.ben_text ExtKarosserie, fztyp_motor Motor, fztyp_erwvbez Modell, fztyp_ktlgausf Region, fztyp_lenkung Lenkung, fztyp_getriebe Getriebe, '&PRODDATUM&' Produktionsdatum, fztyp_sichtschutz Sichtschutz, NVL(fztyp_einsatz, 0) Einsatz from w_fztyp inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe) inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K') inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = '&ISO&' and bk.ben_regiso = '&REGISO&') inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B') inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = '&ISO&' and bb.ben_regiso = '&REGISO&') inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = '&ISO&' and b.ben_regiso = '&REGISO&') where fztyp_typschl= '&TYP&'
```

## RETRIEVE_TYPMENGE_BY_ATTRIBUTE_PKW

- Type: SELECT
- Tables: w_fztyp, w_fgstnr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct fztyp_typschl Typ from w_fztyp, w_fgstnr where fztyp_mospid = &MOSPID& and fztyp_sichtschutz = 'N' and fztyp_lenkung = '&LENKUNG&' and fztyp_getriebe = '&GETRIEBE&' and fztyp_mospid = fgstnr_mospid and fztyp_typschl = fgstnr_typschl and fgstnr_prod = &PRODDATUM&
```

## RETRIEVE_TYPMENGE_BY_ATTRIBUTE_KRAD

- Type: SELECT
- Tables: w_fztyp, w_fgstnr
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct fztyp_typschl Typ from w_fztyp, w_fgstnr where fztyp_mospid = &MOSPID& and fztyp_sichtschutz = 'N' and fztyp_mospid = fgstnr_mospid and fztyp_typschl = fgstnr_typschl and fgstnr_prod = &PRODDATUM&
```

## RETRIEVE_LENKUNG_BEN

- Type: SELECT
- Tables: _publben, w_ben_gk
- Tables not in schema: _publben
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select ben_text Name from _publben, w_ben_gk where publben_art = 'L' and publben_bezeichnung = '&VALUE&' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## LOAD_SALAS_ZU_FGSTNR

- Type: SELECT
- Tables: w_fgstnr_sala, w_bed_sala, w_bed, w_ben_gk, w_eg, w_bed_zusatzinfo
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bed_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, bedsala_sicher Sicher, bedsala_saz SAZ, DECODE(bedzus_elemid, bedzus_elemid, 'J', 'N') HasBedText, fgstnrs_showtext ShowBedText, eg_exklusiv Exklusiv, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_fgstnr_sala inner join w_bed_sala on (fgstnrs_salaid = bedsala_id) inner join w_bed on (bedsala_id = bed_elemid) inner join w_ben_gk on (bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_eg on (bed_egid = eg_id) left join w_bed_zusatzinfo on (bedzus_elemid = fgstnrs_salaid) where fgstnrs_fgstnr = '&FGSTNR&'
```

## LOAD_SS_BEDINGUNGEN_AF

- Type: SELECT
- Tables: w_bed_afl, w_ben_gk, w_bed, w_eg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bed_elemid ElementId, af.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, af.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_ben_gk, w_bed, w_eg where af.bedafl_art = 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and af.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'union select distinct bed_elemid ElementId, f.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, f.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_bed_afl f, w_ben_gk, w_bed, w_eg where af.bedafl_art = 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and f.bedafl_code = substr(af.bedafl_code, 3) and f.bedafl_art = 'F' and f.bedafl_gilt_v <= &PRODDATUM& and NVL(f.bedafl_gilt_b, 99999999) >= &PRODDATUM& and f.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'union select distinct bed_elemid ElementId, a.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, a.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_bed_afl a, w_ben_gk, w_bed, w_eg where af.bedafl_art= 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and a.bedafl_code = substr(af.bedafl_code, 1, 2) and a.bedafl_art = 'A' and a.bedafl_gilt_v <= &PRODDATUM& and NVL(a.bedafl_gilt_b, 99999999) >= &PRODDATUM& and a.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## LOAD_SS_BEDINGUNGEN_LACK

- Type: SELECT
- Tables: w_bed_afl, w_ben_gk, w_bed, w_eg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bed_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bed_afl, w_ben_gk, w_bed, w_eg where bedafl_art= 'L' &LOAD_SS_BEDINGUNGEN_LACK_DECIDE& and bedafl_gilt_v <= &PRODDATUM& and NVL(bedafl_gilt_b, 99999999) >= &PRODDATUM& and bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## LOAD_SS_BEDINGUNGEN_SALAPA

- Type: SELECT
- Tables: w_bed_sala, w_ben_gk, w_bed, w_eg
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct bed_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, bedsala_sicher Sicher, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bed_sala, w_ben_gk, w_bed, w_eg where bedsala_produktart = '&PRODUKTART&' and bedsala_pnr = '&PRIMANUMMER&' and bedsala_gilt_v <= &PRODDATUM& and NVL(bedsala_gilt_b, 99999999) >= &PRODDATUM& and bedsala_art not in ('N', 'V') &LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_ART& &LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_HZAEHLER& and bedsala_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'
```

## RETRIEVE_BED_ZUSATZINFO

- Type: SELECT
- Tables: w_komm, w_bed_zusatzinfo, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select komm_id KommId, ben_text Ben, komm_pos Pos from w_komm, w_ben_gk where komm_id in   (select bedzus_kommid     from w_bed_zusatzinfo     where bedzus_elemid IN (&BED_ELEMIDS&)   diff    select bedzus_kommid     from w_bed_zusatzinfo     where bedzus_elemid NOT IN (&BED_ELEMIDS&)) and   komm_textcode = ben_textcode and   ben_iso = '&ISO&' and   ben_regiso = '&REGISO&' order by KommId, Pos
```
