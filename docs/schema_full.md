# ETK_PUBL Full Schema (with English translations)

Generated: Fri Feb 27 16:03:31 CET 2026

**Total tables:** 129

---

## w_abk (Abbreviations)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| abk_textcode | INTEGER | 10 | NO | text_code |
| abk_art | CHAR | 1 | NO | type |
| abk_kuerzel | VARCHAR | 16 | NO | abbreviation |
| abk_bedeutung | VARCHAR | 80 | NO | meaning |

## w_bauart (Build Type)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bauart_bauart | CHAR | 1 | NO | type |
| bauart_textcode | INTEGER | 10 | NO | text_code |
| bauart_position | SMALLINT | 5 | NO | position |

## w_baureihe (Series (E46, F30, etc.))

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| baureihe_produktart | CHAR | 1 | NO | product_type |
| baureihe_bauart | CHAR | 1 | NO | series |
| baureihe_vbereich | CHAR | 2 | NO | v_area |
| baureihe_textcode | INTEGER | 10 | NO | text_code |
| baureihe_position | SMALLINT | 5 | NO | position |
| baureihe_baureihe | VARCHAR | 4 | NO | series |
| baureihe_bereich | VARCHAR | 7 | NO | area |
| baureihe_marke_tps | VARCHAR | 11 | NO | brand |
| baureihe_grafikid | INTEGER | 10 | YES | graphic_id |

## w_baureihe_kar_thb (Series (E46, F30, etc.) - kar_thb)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| baureihekar_baureihe | VARCHAR | 4 | NO | series |
| baureihekar_karosserie | VARCHAR | 10 | NO | body_type |
| baureihekar_grafikid | INTEGER | 10 | YES | graphic_id |

## w_bed (Conditions)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bed_elemid | INTEGER | 10 | NO | element_id |
| bed_textcode | INTEGER | 10 | NO | text_code |
| bed_egid | SMALLINT | 5 | NO | id |
| bed_ogid | VARCHAR | 8 | NO | id |
| bed_vorhanden_info | CHAR | 1 | NO | available |

## w_bed_afl (Conditions - afl)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedafl_id | INTEGER | 10 | NO | id |
| bedafl_gilt_v | INTEGER | 10 | NO | valid_from |
| bedafl_gilt_b | INTEGER | 10 | YES | valid_to |
| bedafl_art | VARCHAR | 3 | NO | type |
| bedafl_code | VARCHAR | 8 | NO | code |
| bedafl_pcode | CHAR | 4 | YES | code |

## w_bed_aflpc (Conditions - aflpc)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedaflpc_pos | INTEGER | 10 | NO | position |
| bedaflpc_code | VARCHAR | 4 | NO | code |
| bedaflpc_pcode | CHAR | 4 | YES | code |
| bedaflpc_textcode | INTEGER | 10 | NO | text_code |
| bedaflpc_gilt_v | INTEGER | 10 | YES | valid_from |
| bedaflpc_gilt_b | INTEGER | 10 | YES | valid_to |
| bedaflpc_art | VARCHAR | 3 | NO | type |

## w_bed_etktext (Conditions - etktext)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedetkt_elemid | INTEGER | 10 | NO | element_id |
| bedetkt_hg | CHAR | 2 | NO | - |
| bedetkt_fg | CHAR | 2 | NO | - |
| bedetkt_produktart | CHAR | 1 | NO | product_type |
| bedetkt_kommid | INTEGER | 10 | NO | comment_id |

## w_bed_sala (Conditions - sala)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedsala_id | INTEGER | 10 | NO | id |
| bedsala_produktart | CHAR | 1 | NO | product_type |
| bedsala_art | CHAR | 1 | NO | type |
| bedsala_pnr | CHAR | 3 | NO | number |
| bedsala_hz | CHAR | 1 | NO | - |
| bedsala_sicher | CHAR | 1 | NO | - |
| bedsala_gilt_v | INTEGER | 10 | NO | valid_from |
| bedsala_gilt_b | INTEGER | 10 | YES | valid_to |
| bedsala_saz | CHAR | 1 | NO | - |

## w_bed_zusatzinfo (Conditions - zusatzinfo)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedzus_kombiid | INTEGER | 10 | NO | id |
| bedzus_elemid | INTEGER | 10 | NO | element_id |
| bedzus_kommid | INTEGER | 10 | NO | comment_id |

## w_bedeutung (Conditions - eutung)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bedeutung_art | VARCHAR | 3 | NO | type |
| bedeutung_wert | VARCHAR | 10 | NO | meaning |
| bedeutung_kommid | INTEGER | 10 | NO | comment_id |

## w_ben_gk (Names - gk)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| ben_textcode | INTEGER | 10 | NO | text_code |
| ben_iso | CHAR | 2 | NO | - |
| ben_regiso | CHAR | 2 | NO | - |
| ben_text | VARCHAR | 420 | NO | text |

## w_bildtaf (Diagram/Image Table)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtaf_btnr | CHAR | 7 | NO | diagram_number |
| bildtaf_hg | CHAR | 2 | NO | - |
| bildtaf_fg | CHAR | 2 | NO | - |
| bildtaf_produktart | CHAR | 1 | NO | product_type |
| bildtaf_vbereich | CHAR | 2 | NO | v_area |
| bildtaf_bteart | CHAR | 1 | NO | type |
| bildtaf_sicher | CHAR | 1 | NO | - |
| bildtaf_textc | INTEGER | 10 | NO | text |
| bildtaf_grafikid | INTEGER | 10 | NO | graphic_id |
| bildtaf_pos | INTEGER | 10 | NO | position |
| bildtaf_kommbt | INTEGER | 10 | YES | - |
| bildtaf_bedkez | VARCHAR | 2 | YES | - |
| bildtaf_bereich | VARCHAR | 7 | YES | area |
| bildtaf_vorh_cp | CHAR | 1 | NO | - |
| bildtaf_lkz | VARCHAR | 3 | YES | - |
| bildtaf_ist_valueline | CHAR | 1 | NO | - |

## w_bildtaf_bnbben (Diagram/Image Table - bnbben)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafb_btnr | CHAR | 7 | NO | diagram_number |
| bildtafb_bildposnr | CHAR | 2 | NO | position |
| bildtafb_textcode | INTEGER | 10 | NO | text_code |

## w_bildtaf_cp (Diagram/Image Table - Cross-reference)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafc_btnr | CHAR | 7 | NO | diagram_number |
| bildtafc_art | VARCHAR | 4 | NO | type |
| bildtafc_datum | INTEGER | 10 | NO | date |

## w_bildtaf_marke (Diagram/Image Table - Brand)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafm_btnr | CHAR | 7 | NO | diagram_number |
| bildtafm_marke_tps | VARCHAR | 11 | NO | brand |

## w_bildtaf_suche (Diagram/Image Table - Search)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafs_btnr | CHAR | 7 | NO | diagram_number |
| bildtafs_hg | CHAR | 2 | NO | - |
| bildtafs_fg | CHAR | 2 | NO | - |
| bildtafs_mospid | INTEGER | 10 | NO | model_series_prod_id |
| bildtafs_automatik | CHAR | 1 | YES | - |
| bildtafs_lenkg | CHAR | 1 | YES | - |
| bildtafs_eins | INTEGER | 10 | YES | - |
| bildtafs_auslf | INTEGER | 10 | YES | - |

## w_bildtaf_verweis (Diagram/Image Table - Reference)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bv_btnr_von | CHAR | 7 | NO | diagram_number |
| bv_btnr_nach | CHAR | 7 | NO | diagram_number |
| bv_kommid | INTEGER | 10 | NO | comment_id |

## w_bildtafzub (Diagram/Image Table - zub)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafz_btnr | CHAR | 7 | NO | diagram_number |
| bildtafz_salaid | INTEGER | 10 | NO | id |
| bildtafz_bedkez | VARCHAR | 2 | YES | - |
| bildtafz_ipac | CHAR | 1 | NO | - |
| bildtafz_sa2 | CHAR | 1 | NO | - |
| bildtafz_sa3 | CHAR | 1 | NO | - |
| bildtafz_zusatz_kommid | INTEGER | 10 | YES | comment_id |

## w_bildtafzub_baureihe (Diagram/Image Table - zub_baureihe)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzb_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzb_baureihe | VARCHAR | 4 | NO | series |
| bildtafzb_ktlgausf | VARCHAR | 3 | NO | - |

## w_bildtafzub_bedelem (Diagram/Image Table - zub_bedelem)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbe_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbe_elemid | INTEGER | 10 | NO | element_id |
| bildtafzbe_pos | SMALLINT | 5 | NO | position |
| bildtafzbe_vz | CHAR | 1 | NO | - |
| bildtafzbe_ogid | VARCHAR | 8 | NO | id |
| bildtafzbe_kez | VARCHAR | 2 | NO | - |

## w_bildtafzub_bedgesamt (Diagram/Image Table - zub_bedgesamt)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbg_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbg_kez | VARCHAR | 2 | NO | - |
| bildtafzbg_vz | CHAR | 1 | NO | - |
| bildtafzbg_gesamtterm | VARCHAR | 107 | NO | - |

## w_bildtafzub_bedog (Diagram/Image Table - zub_bedog)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbo_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbo_vart | CHAR | 1 | NO | type |
| bildtafzbo_ogid | VARCHAR | 8 | NO | id |
| bildtafzbo_kez | VARCHAR | 2 | NO | - |
| bildtafzbo_fzeile | VARCHAR | 1024 | YES | - |

## w_bildtafzub_bedueber (Diagram/Image Table - zub_bedueber)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbu_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbu_kez | VARCHAR | 2 | NO | - |
| bildtafzbu_kezueber | VARCHAR | 2 | NO | - |

## w_bildtafzub_bnb (Diagram/Image Table - zub_bnb)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzb_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzb_bildposnr | CHAR | 2 | NO | position |
| bildtafzb_textcode | INTEGER | 10 | NO | text_code |

## w_bildtafzub_bnb_marketing (Diagram/Image Table - zub_bnb_marketing)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbnb_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbnb_bildpos | CHAR | 2 | NO | position |
| bildtafzbnb_produktid | INTEGER | 10 | NO | id |
| bildtafzbnb_marktid | INTEGER | 10 | NO | id |

## w_bildtafzub_bnb_refa (Diagram/Image Table - zub_bnb_refa)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzbnbr_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzbnbr_bildpos | CHAR | 2 | NO | position |
| bildtafzbnbr_mospid | INTEGER | 10 | NO | model_series_prod_id |
| bildtafzbnbr_typschl | CHAR | 4 | NO | - |
| bildtafzbnbr_refaid | INTEGER | 10 | NO | id |
| bildtafzbnbr_aw | SMALLINT | 5 | NO | - |
| bildtafzbnbr_von | INTEGER | 10 | NO | - |
| bildtafzbnbr_bis | INTEGER | 10 | YES | - |

## w_bildtafzub_cp (Diagram/Image Table - zub_cp)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzc_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzc_art | VARCHAR | 4 | NO | type |
| bildtafzc_datum | INTEGER | 10 | NO | date |

## w_bildtafzub_einbauinfo (Diagram/Image Table - zub_einbauinfo)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafze_btnr | CHAR | 7 | NO | diagram_number |
| bildtafze_art | VARCHAR | 5 | NO | type |
| bildtafze_baureihe | VARCHAR | 4 | NO | series |
| bildtafze_id | VARCHAR | 9 | NO | id |
| bildtafze_einbauinfoid | INTEGER | 10 | NO | installation_info |

## w_bildtafzub_einbauinfo_markt (Diagram/Image Table - zub_einbauinfo_markt)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafze_systemid | INTEGER | 10 | NO | id |
| bildtafze_marktid | INTEGER | 10 | NO | id |
| bildtafze_btnr | CHAR | 7 | NO | diagram_number |
| bildtafze_art | VARCHAR | 5 | NO | type |
| bildtafze_baureihe | VARCHAR | 4 | NO | series |
| bildtafze_id | VARCHAR | 9 | NO | id |
| bildtafze_einbauinfoid | INTEGER | 10 | NO | installation_info |

## w_bildtafzub_komm (Diagram/Image Table - zub_komm)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzk_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzk_art | CHAR | 1 | NO | type |
| bildtafzk_bildposnr | CHAR | 2 | NO | position |
| bildtafzk_zeilenpos | INTEGER | 10 | NO | position |
| bildtafzk_kommid | INTEGER | 10 | NO | comment_id |

## w_bildtafzub_marketing (Diagram/Image Table - zub_marketing)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzm_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzm_produktid | INTEGER | 10 | NO | id |
| bildtafzm_marktid | INTEGER | 10 | NO | id |

## w_bildtafzub_refa (Diagram/Image Table - zub_refa)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzr_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzr_mospid | INTEGER | 10 | NO | model_series_prod_id |
| bildtafzr_typschl | CHAR | 4 | NO | - |
| bildtafzr_refaid | INTEGER | 10 | NO | id |
| bildtafzr_aw | SMALLINT | 5 | NO | - |
| bildtafzr_von | INTEGER | 10 | NO | - |
| bildtafzr_bis | INTEGER | 10 | YES | - |

## w_bildtafzub_var_marketing (Diagram/Image Table - zub_var_marketing)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzvm_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzvm_varianteid | INTEGER | 10 | NO | id |
| bildtafzvm_produktid | INTEGER | 10 | NO | id |
| bildtafzvm_marktid | INTEGER | 10 | NO | id |

## w_bildtafzub_variante (Diagram/Image Table - zub_variante)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bildtafzv_btnr | CHAR | 7 | NO | diagram_number |
| bildtafzv_varianteid | INTEGER | 10 | NO | id |

## w_bte_allelem (bte allelem)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| bteae_btnr | CHAR | 7 | NO | diagram_number |
| bteae_mospid | INTEGER | 10 | NO | model_series_prod_id |
| bteae_elemid | INTEGER | 10 | NO | element_id |

## w_bte_bedelem (bte bedelem)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btebe_btnr | CHAR | 7 | NO | diagram_number |
| btebe_elemid | INTEGER | 10 | NO | element_id |
| btebe_pos | SMALLINT | 5 | NO | position |
| btebe_vz | CHAR | 1 | NO | - |
| btebe_ogid | VARCHAR | 8 | NO | id |
| btebe_kez | VARCHAR | 2 | NO | - |

## w_bte_bedgesamt (bte bedgesamt)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btebg_btnr | CHAR | 7 | NO | diagram_number |
| btebg_kez | VARCHAR | 2 | NO | - |
| btebg_vz | CHAR | 1 | NO | - |
| btebg_gesamtterm | VARCHAR | 107 | NO | - |

## w_bte_bedkurz (bte bedkurz)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btebk_btnr | CHAR | 7 | NO | diagram_number |
| btebk_mospid | INTEGER | 10 | NO | model_series_prod_id |
| btebk_kez | VARCHAR | 2 | NO | - |

## w_bte_bedog (bte bedog)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btebo_btnr | CHAR | 7 | NO | diagram_number |
| btebo_vart | CHAR | 1 | NO | type |
| btebo_ogid | VARCHAR | 8 | NO | id |
| btebo_kez | VARCHAR | 2 | NO | - |
| btebo_fzeile | VARCHAR | 1024 | YES | - |

## w_bte_bedueber (bte bedueber)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btebu_btnr | CHAR | 7 | NO | diagram_number |
| btebu_kez | VARCHAR | 2 | NO | - |
| btebu_kezueber | VARCHAR | 2 | NO | - |

## w_btzeilen (Diagram Lines/Parts List)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilen_btnr | CHAR | 7 | NO | diagram_number |
| btzeilen_pos | INTEGER | 10 | NO | position |
| btzeilen_hg | CHAR | 2 | NO | - |
| btzeilen_bildposnr | CHAR | 2 | YES | position |
| btzeilen_sachnr | CHAR | 7 | YES | part_number |
| btzeilen_kat | CHAR | 1 | YES | - |
| btzeilen_automatik | CHAR | 1 | YES | - |
| btzeilen_lenkg | CHAR | 1 | YES | - |
| btzeilen_eins | INTEGER | 10 | YES | - |
| btzeilen_auslf | INTEGER | 10 | YES | - |
| btzeilen_bedkez | VARCHAR | 2 | YES | - |
| btzeilen_regelnr | SMALLINT | 5 | YES | number |
| btzeilen_kommbt | INTEGER | 10 | YES | - |
| btzeilen_kommvor | INTEGER | 10 | YES | - |
| btzeilen_kommnach | INTEGER | 10 | YES | - |
| btzeilen_gruppeid | INTEGER | 10 | YES | id |
| btzeilen_blocknr | INTEGER | 10 | YES | number |
| btzeilen_bedkez_pg | VARCHAR | 2 | YES | - |

## w_btzeilen_cp (Diagram Lines/Parts List - Cross-reference)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenc_mospid | INTEGER | 10 | NO | model_series_prod_id |
| btzeilenc_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenc_pos | INTEGER | 10 | NO | position |
| btzeilenc_typschl | CHAR | 4 | NO | - |
| btzeilenc_werk | VARCHAR | 8 | NO | - |
| btzeilenc_art | VARCHAR | 4 | NO | type |
| btzeilenc_datum | INTEGER | 10 | YES | date |
| btzeilenc_vin | VARCHAR | 7 | YES | - |
| btzeilenc_vin_proddatum | INTEGER | 10 | YES | date |
| btzeilenc_vin_min | VARCHAR | 7 | YES | - |
| btzeilenc_vin_max | VARCHAR | 7 | YES | - |
| btzeilenc_nart | CHAR | 1 | YES | type |
| btzeilenc_nummer | VARCHAR | 28 | YES | - |
| btzeilenc_alter | SMALLINT | 5 | YES | - |

## w_btzeilen_verbauung (Diagram Lines/Parts List - Installation)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenv_mospid | INTEGER | 10 | NO | model_series_prod_id |
| btzeilenv_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenv_pos | INTEGER | 10 | NO | position |
| btzeilenv_vmenge | VARCHAR | 2 | NO | quantity |
| btzeilenv_sachnr | CHAR | 7 | NO | part_number |
| btzeilenv_textcode | INTEGER | 10 | NO | text_code |
| btzeilenv_alter_kz | CHAR | 1 | YES | - |
| btzeilenv_bed_alter | INTEGER | 10 | YES | - |
| btzeilenv_bed_art | CHAR | 1 | YES | type |

## w_btzeilenugb (Diagram Lines/Parts List - ugb)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenu_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenu_pos | INTEGER | 10 | NO | position |
| btzeilenu_hg | CHAR | 2 | NO | - |
| btzeilenu_bildposnr | CHAR | 2 | YES | position |
| btzeilenu_sachnr | CHAR | 7 | YES | part_number |
| btzeilenu_mmg | VARCHAR | 4 | YES | - |
| btzeilenu_emg | VARCHAR | 4 | YES | - |
| btzeilenu_eins | INTEGER | 10 | YES | - |
| btzeilenu_ausl | INTEGER | 10 | YES | - |
| btzeilenu_kommbt | INTEGER | 10 | YES | - |
| btzeilenu_kommvor | INTEGER | 10 | YES | - |
| btzeilenu_kommnach | INTEGER | 10 | YES | - |

## w_btzeilenugb_verbauung (Diagram Lines/Parts List - ugb_verbauung)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenuv_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenuv_pos | INTEGER | 10 | NO | position |
| btzeilenuv_marke_tps | VARCHAR | 11 | NO | brand |

## w_btzeilenzub (Diagram Lines/Parts List - zub)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenz_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenz_pos | INTEGER | 10 | NO | position |
| btzeilenz_gehzupos | INTEGER | 10 | NO | position |
| btzeilenz_bildposnr | CHAR | 2 | NO | position |
| btzeilenz_art | VARCHAR | 11 | NO | type |
| btzeilenz_verw | TINYINT | 3 | YES | - |
| btzeilenz_sachnr | CHAR | 7 | NO | part_number |
| btzeilenz_elementart | VARCHAR | 20 | NO | type |
| btzeilenz_buendelung | VARCHAR | 6 | YES | - |
| btzeilenz_menge_min | VARCHAR | 2 | NO | quantity |
| btzeilenz_menge_max | VARCHAR | 2 | NO | quantity |
| btzeilenz_kat | CHAR | 1 | YES | - |
| btzeilenz_automatik | CHAR | 1 | YES | - |
| btzeilenz_lenkg | CHAR | 1 | YES | - |
| btzeilenz_eins | INTEGER | 10 | YES | - |
| btzeilenz_auslf | INTEGER | 10 | YES | - |
| btzeilenz_bedkez | VARCHAR | 2 | YES | - |
| btzeilenz_regelnr | SMALLINT | 5 | YES | number |
| btzeilenz_gruppeid | INTEGER | 10 | YES | id |

## w_btzeilenzub_cp (Diagram Lines/Parts List - zub_cp)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenzc_mospid | INTEGER | 10 | NO | model_series_prod_id |
| btzeilenzc_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenzc_pos | INTEGER | 10 | NO | position |
| btzeilenzc_typschl | CHAR | 4 | NO | - |
| btzeilenzc_werk | VARCHAR | 8 | NO | - |
| btzeilenzc_art | VARCHAR | 4 | NO | type |
| btzeilenzc_datum | INTEGER | 10 | YES | date |
| btzeilenzc_vin | VARCHAR | 7 | YES | - |
| btzeilenzc_vin_proddatum | INTEGER | 10 | YES | date |
| btzeilenzc_vin_min | VARCHAR | 7 | YES | - |
| btzeilenzc_vin_max | VARCHAR | 7 | YES | - |
| btzeilenzc_nart | CHAR | 1 | YES | type |
| btzeilenzc_nummer | VARCHAR | 28 | YES | - |

## w_btzeilenzub_variante (Diagram Lines/Parts List - zub_variante)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenzva_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenzva_pos | INTEGER | 10 | NO | position |
| btzeilenzva_varianteid | INTEGER | 10 | NO | id |

## w_btzeilenzub_verbauung (Diagram Lines/Parts List - zub_verbauung)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenzv_mospid | INTEGER | 10 | NO | model_series_prod_id |
| btzeilenzv_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenzv_pos | INTEGER | 10 | NO | position |
| btzeilenzv_vmenge | VARCHAR | 2 | NO | quantity |

## w_btzeilenzubugb (Diagram Lines/Parts List - zubugb)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenuz_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenuz_pos | INTEGER | 10 | NO | position |
| btzeilenuz_gehzupos | INTEGER | 10 | NO | position |
| btzeilenuz_bildposnr | CHAR | 2 | NO | position |
| btzeilenuz_art | VARCHAR | 11 | NO | type |
| btzeilenuz_verw | TINYINT | 3 | YES | - |
| btzeilenuz_sachnr | CHAR | 7 | NO | part_number |
| btzeilenuz_elementart | VARCHAR | 20 | NO | type |
| btzeilenuz_buendelung | VARCHAR | 6 | YES | - |
| btzeilenuz_menge_min | VARCHAR | 2 | NO | quantity |
| btzeilenuz_menge_max | VARCHAR | 2 | NO | quantity |
| btzeilenuz_mmg | VARCHAR | 4 | YES | - |
| btzeilenuz_emg | VARCHAR | 4 | YES | - |
| btzeilenuz_eins | INTEGER | 10 | YES | - |
| btzeilenuz_auslf | INTEGER | 10 | YES | - |

## w_btzeilenzubugb_variante (Diagram Lines/Parts List - zubugb_variante)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenzuva_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenzuva_pos | INTEGER | 10 | NO | position |
| btzeilenzuva_varianteid | INTEGER | 10 | NO | id |

## w_btzeilenzubugb_verbauung (Diagram Lines/Parts List - zubugb_verbauung)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| btzeilenzuv_btnr | CHAR | 7 | NO | diagram_number |
| btzeilenzuv_pos | INTEGER | 10 | NO | position |
| btzeilenzuv_marke_tps | VARCHAR | 11 | NO | brand |

## w_eg (Condition Group)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| eg_pos | SMALLINT | 5 | NO | position |
| eg_textcode | INTEGER | 10 | NO | text_code |
| eg_exklusiv | CHAR | 1 | NO | - |
| eg_id | SMALLINT | 5 | NO | id |
| eg_ogid | VARCHAR | 8 | NO | id |

## w_einbauinfo (einbauinfo)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| einbauinfo_id | INTEGER | 10 | NO | installation_info |
| einbauinfo_komplexitaet | VARCHAR | 7 | NO | installation_info |
| einbauinfo_lesart | VARCHAR | 10 | NO | installation_info |
| einbauinfo_mechanisch | VARCHAR | 4 | YES | installation_info |
| einbauinfo_elektrisch | VARCHAR | 4 | YES | installation_info |
| einbauinfo_programm | VARCHAR | 4 | YES | installation_info |
| einbauinfo_lack | VARCHAR | 4 | YES | installation_info |
| einbauinfo_gesamt | VARCHAR | 20 | YES | installation_info |

## w_erstbevorratung (Initial Stocking)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| eb_hg | CHAR | 2 | NO | - |
| eb_mospid | INTEGER | 10 | NO | model_series_prod_id |
| eb_sachnr | CHAR | 7 | NO | part_number |
| eb_lenkung | CHAR | 1 | YES | steering |

## w_erstbevorratung_suche (Initial Stocking - Search)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| ebs_hg | CHAR | 2 | NO | - |
| ebs_mospid | INTEGER | 10 | NO | model_series_prod_id |
| ebs_lenkung | CHAR | 1 | YES | steering |

## w_etk_grafiken (etk grafiken)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| etkgraf_ablauf | CHAR | 2 | NO | - |
| etkgraf_marke | VARCHAR | 11 | NO | brand |
| etkgraf_produktart | VARCHAR | 4 | NO | product_type |
| etkgraf_vbereich | VARCHAR | 4 | NO | v_area |
| etkgraf_grafikid | INTEGER | 10 | NO | graphic_id |

## w_eu_reifen (eu reifen)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| reifen_sachnr | CHAR | 7 | YES | part_number |
| reifen_kraftstoff | CHAR | 1 | NO | - |
| reifen_nasshaftung | CHAR | 1 | NO | - |
| reifen_rollgeraeusch_stufe | SMALLINT | 5 | NO | - |
| reifen_rollgeraeusch_wert | SMALLINT | 5 | NO | - |

## w_fg_thumbnail (fg thumbnail)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fgthb_hg | CHAR | 2 | NO | - |
| fgthb_fg | CHAR | 2 | NO | - |
| fgthb_marke_tps | VARCHAR | 11 | NO | brand |
| fgthb_produktart | CHAR | 1 | NO | product_type |
| fgthb_bereich | VARCHAR | 7 | NO | area |
| fgthb_grafikid | INTEGER | 10 | NO | graphic_id |

## w_fgstnr (Chassis Number)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fgstnr_anf | CHAR | 2 | NO | number |
| fgstnr_von | CHAR | 7 | NO | number |
| fgstnr_bis | CHAR | 7 | NO | number |
| fgstnr_typschl | CHAR | 4 | NO | number |
| fgstnr_prod | INTEGER | 10 | NO | number |
| fgstnr_mospid | INTEGER | 10 | NO | model_series_prod_id |
| fgstnr_werk | VARCHAR | 8 | YES | number |

## w_fgstnr_sala (Chassis Number - sala)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fgstnrs_fgstnr | CHAR | 7 | NO | number |
| fgstnrs_salaid | INTEGER | 10 | NO | id |
| fgstnrs_showtext | CHAR | 1 | NO | text |

## w_fgstnr_sicher (Chassis Number - sicher)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fgstnrs_fgstnr | CHAR | 7 | NO | number |
| fgstnrs_salaid | INTEGER | 10 | NO | id |

## w_fremdtl (Foreign Parts)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fremdtl_sachnr | CHAR | 7 | NO | part_number |
| fremdtl_fremdsnr | VARCHAR | 35 | NO | number |

## w_fuellmengen (Fill Quantities)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fuellmengen_baureihe | CHAR | 4 | NO | series |
| fuellmengen_modell | VARCHAR | 9 | NO | quantity |
| fuellmengen_typ | CHAR | 4 | NO | quantity |
| fuellmengen_ab | INTEGER | 10 | NO | quantity |
| fuellmengen_bis | INTEGER | 10 | YES | quantity |
| fuellmengen_motor | VARCHAR | 7 | NO | engine |
| fuellmengen_fm_motor | NUMERIC | 4 | NO | engine |
| fuellmengen_getriebe | CHAR | 1 | NO | transmission |
| fuellmengen_getriebeben | VARCHAR | 13 | NO | quantity |
| fuellmengen_fm_getriebe | NUMERIC | 4 | NO | transmission |
| fuellmengen_fm_ha | NUMERIC | 4 | NO | quantity |
| fuellmengen_fm_km_ohneac | NUMERIC | 4 | NO | quantity |
| fuellmengen_fm_km_mitac | NUMERIC | 4 | NO | quantity |
| fuellmengen_fm_bremse | NUMERIC | 4 | NO | quantity |
| fuellmengen_hw_de | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_en | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_fr | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_nl | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_it | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_es | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_sv | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_ja | VARCHAR | 150 | YES | quantity |
| fuellmengen_hw_enus | VARCHAR | 150 | YES | quantity |

## w_fztyp (Vehicle Type)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| fztyp_mospid | INTEGER | 10 | NO | model_series_prod_id |
| fztyp_typschl | CHAR | 4 | NO | - |
| fztyp_vbereich | CHAR | 2 | NO | v_area |
| fztyp_katalysator | CHAR | 3 | NO | - |
| fztyp_sichtschutz | CHAR | 1 | NO | - |
| fztyp_lenkung | CHAR | 1 | YES | steering |
| fztyp_getriebe | CHAR | 1 | YES | transmission |
| fztyp_baureihe | VARCHAR | 4 | NO | series |
| fztyp_ktlgausf | VARCHAR | 3 | NO | - |
| fztyp_vbez | VARCHAR | 90 | NO | - |
| fztyp_erwvbez | VARCHAR | 36 | NO | - |
| fztyp_motor | VARCHAR | 5 | YES | engine |
| fztyp_karosserie | VARCHAR | 10 | YES | body_type |
| fztyp_einsatz | INTEGER | 10 | YES | - |

## w_grafik (Graphics/Images)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| grafik_grafikid | INTEGER | 10 | NO | graphic_id |
| grafik_laenge | INTEGER | 10 | NO | graphic_blob |
| grafik_art | CHAR | 1 | NO | type |
| grafik_format | CHAR | 3 | NO | graphic_blob |
| grafik_blob | BLOB | 2147483647 | YES | graphic_blob |
| grafik_moddate | NUMERIC | 14 | NO | graphic_blob |

## w_grafik_hs (Graphics/Images - hs)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| grafikhs_grafikid | INTEGER | 10 | NO | graphic_id |
| grafikhs_art | CHAR | 1 | NO | type |
| grafikhs_bildposnr | CHAR | 2 | NO | graphic_blob |
| grafikhs_topleft_x | INTEGER | 10 | NO | graphic_blob |
| grafikhs_topleft_y | INTEGER | 10 | NO | graphic_blob |
| grafikhs_bottomright_x | INTEGER | 10 | NO | graphic_blob |
| grafikhs_bottomright_y | INTEGER | 10 | NO | graphic_blob |

## w_grafik_ipac (Graphics/Images - ipac)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| grafikipac_id | INTEGER | 10 | NO | graphic_blob |
| grafikipac_art | CHAR | 1 | NO | type |
| grafikipac_laenge | INTEGER | 10 | NO | graphic_blob |
| grafikipac_mimetype | VARCHAR | 256 | YES | graphic_blob |
| grafikipac_blob | BLOB | 2147483647 | NO | graphic_blob |

## w_grp_information (grp information)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| grpinfo_mospid | INTEGER | 10 | NO | model_series_prod_id |
| grpinfo_sachnr | CHAR | 7 | NO | part_number |
| grpinfo_typ | CHAR | 4 | NO | - |
| grpinfo_leitaw_pa | CHAR | 1 | NO | - |
| grpinfo_leitaw_hg | CHAR | 2 | NO | - |
| grpinfo_leitaw_ug | CHAR | 2 | NO | - |
| grpinfo_leitaw_nr | CHAR | 3 | NO | number |

## w_hauptkategorie (Main Category)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hauptkat_id | INTEGER | 10 | NO | id |
| hauptkat_marke_tps | VARCHAR | 11 | NO | brand |
| hauptkat_produktart | CHAR | 1 | NO | product_type |
| hauptkat_textcode | INTEGER | 10 | NO | text_code |
| hauptkat_grafikid | INTEGER | 10 | YES | graphic_id |
| hauptkat_pos | SMALLINT | 5 | NO | position |

## w_hg_thumbnail (hg thumbnail)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hgthb_hg | CHAR | 2 | NO | - |
| hgthb_marke_tps | VARCHAR | 11 | NO | brand |
| hgthb_produktart | CHAR | 1 | NO | product_type |
| hgthb_bereich | VARCHAR | 7 | NO | area |
| hgthb_grafikid | INTEGER | 10 | NO | graphic_id |

## w_hgfg (Main Group/Sub Group)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hgfg_hg | CHAR | 2 | NO | - |
| hgfg_fg | CHAR | 2 | NO | - |
| hgfg_produktart | CHAR | 1 | NO | product_type |
| hgfg_textcode | INTEGER | 10 | NO | text_code |
| hgfg_bereich | VARCHAR | 7 | NO | area |
| hgfg_grafikid | INTEGER | 10 | YES | graphic_id |
| hgfg_ist_valueline | CHAR | 1 | NO | - |

## w_hgfg_mosp (Main Group/Sub Group - mosp)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hgfgm_mospid | INTEGER | 10 | NO | model_series_prod_id |
| hgfgm_hg | CHAR | 2 | NO | - |
| hgfgm_fg | CHAR | 2 | NO | - |
| hgfgm_produktart | CHAR | 1 | NO | product_type |
| hgfgm_bereich | VARCHAR | 7 | NO | area |

## w_hist (History)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hist_sachnr | CHAR | 7 | NO | part_number |
| hist_zweig_nr | SMALLINT | 5 | NO | number |
| hist_struktur_nr | SMALLINT | 5 | NO | number |
| hist_sachnr_h | CHAR | 7 | NO | part_number |

## w_hk_uk_sa (hk uk sa)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| hkuksa_marke_tps | VARCHAR | 11 | NO | brand |
| hkuksa_produktart | CHAR | 1 | NO | product_type |
| hkuksa_hkid | INTEGER | 10 | NO | id |
| hkuksa_ukid | INTEGER | 10 | NO | id |
| hkuksa_salaid | INTEGER | 10 | NO | id |

## w_komm (Comments)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| komm_id | INTEGER | 10 | NO | id |
| komm_pos | SMALLINT | 5 | NO | position |
| komm_textcode | INTEGER | 10 | NO | text_code |
| komm_tiefe | TINYINT | 3 | NO | - |
| komm_darstellung | CHAR | 1 | NO | - |
| komm_vz | CHAR | 1 | YES | - |
| komm_code | VARCHAR | 8 | YES | code |

## w_komm_help (Comments - help)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| kommh_mospid | INTEGER | 10 | NO | model_series_prod_id |
| kommh_btnr | CHAR | 7 | NO | diagram_number |
| kommh_id | INTEGER | 10 | NO | id |

## w_kommugb_help (Comments - ugb_help)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| kommuh_marke_tps | VARCHAR | 11 | NO | brand |
| kommuh_btnr | CHAR | 7 | NO | diagram_number |
| kommuh_id | INTEGER | 10 | NO | id |

## w_kompl_einzelteil (kompl einzelteil)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| ke_marke_tps | VARCHAR | 11 | NO | brand |
| ke_sachnr_satz | CHAR | 7 | NO | part_number |
| ke_pos | SMALLINT | 5 | NO | position |
| ke_sachnr_einzelteil | CHAR | 7 | NO | part_number |
| ke_menge | SMALLINT | 5 | NO | quantity |
| ke_beziehbar | CHAR | 1 | NO | - |

## w_kompl_satz (kompl satz)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| ks_marke_tps | VARCHAR | 11 | NO | brand |
| ks_produktart | CHAR | 1 | NO | product_type |
| ks_hg | CHAR | 2 | NO | - |
| ks_sachnr_satz | CHAR | 7 | NO | part_number |
| ks_ist_valueline | CHAR | 1 | NO | - |

## w_marketingprodukt (marketingprodukt)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| mprhk_produktid | INTEGER | 10 | NO | id |
| mprhk_herkunft | VARCHAR | 15 | NO | - |

## w_marketingprodukt_grafik (marketingprodukt grafik)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| mgraf_produktid | INTEGER | 10 | NO | id |
| mgraf_marktid | INTEGER | 10 | NO | id |
| mgraf_grafikid | INTEGER | 10 | NO | graphic_id |
| mgraf_pos | TINYINT | 3 | NO | position |
| mgraf_art | CHAR | 1 | NO | type |

## w_marketingprodukt_keyword (marketingprodukt keyword)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| mkeyw_produktid | INTEGER | 10 | NO | id |
| mkeyw_marktid | INTEGER | 10 | NO | id |
| mkeyw_spriso | CHAR | 2 | NO | - |
| mkeyw_sprregiso | CHAR | 2 | NO | - |
| mkeyw_keyword | VARCHAR | 240 | NO | - |

## w_marketingprodukt_related (marketingprodukt related)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| mrellink_produktid | INTEGER | 10 | NO | id |
| mrellink_marktid | INTEGER | 10 | NO | id |
| mrellink_related_productid | INTEGER | 10 | NO | id |

## w_marketingprodukt_text (marketingprodukt text)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| mprod_produktid | INTEGER | 10 | NO | id |
| mprod_marktid | INTEGER | 10 | NO | id |
| mprod_spriso | CHAR | 2 | NO | - |
| mprod_sprregiso | CHAR | 2 | NO | - |
| mprod_art | VARCHAR | 20 | NO | type |
| mprod_text | BLOB | 2147483647 | YES | text |
| mprod_text_anfang | VARCHAR | 300 | YES | text |
| mprod_text_laenge | INTEGER | 10 | YES | text |

## w_markt_etk (Market - etk)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktetk_id | INTEGER | 10 | NO | id |
| marktetk_mkz | VARCHAR | 3 | NO | - |
| marktetk_lkz | VARCHAR | 3 | NO | - |
| marktetk_textcode | INTEGER | 10 | NO | text_code |
| marktetk_anzlokbt | SMALLINT | 5 | NO | - |
| marktetk_isokz | CHAR | 2 | NO | - |

## w_markt_ipac (Market - ipac)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktipac_id | INTEGER | 10 | NO | id |
| marktipac_lkz | VARCHAR | 3 | NO | - |
| marktipac_textcode | INTEGER | 10 | NO | text_code |
| marktipac_kuerzel | VARCHAR | 5 | NO | abbreviation |
| marktipac_bindefrist | SMALLINT | 5 | NO | - |
| marktipac_kundendaten | CHAR | 1 | NO | - |
| marktipac_anz_inkl_mwst | CHAR | 1 | NO | - |
| marktipac_relevanz_pa | CHAR | 1 | NO | - |

## w_markt_ipac_sprache (Market - ipac_sprache)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktipacs_id | INTEGER | 10 | NO | id |
| marktipacs_produktart | CHAR | 1 | NO | product_type |
| marktipacs_spriso | CHAR | 2 | NO | - |
| marktipacs_sprregiso | CHAR | 2 | NO | - |

## w_markt_ipac_werte (Market - ipac_werte)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktipacw_id | INTEGER | 10 | NO | id |
| marktipacw_produktart | CHAR | 1 | NO | product_type |
| marktipacw_art | VARCHAR | 100 | NO | type |
| marktipacw_wert | VARCHAR | 100 | NO | - |

## w_markt_produkt_br (Market - produkt_br)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktprod_systemid | INTEGER | 10 | NO | id |
| marktprod_marktid | INTEGER | 10 | NO | id |
| marktprod_btnr | CHAR | 7 | NO | diagram_number |
| marktprod_baureihe | VARCHAR | 4 | NO | series |
| marktprod_highpotential | CHAR | 1 | YES | - |

## w_markt_produkt_var (Market - produkt_var)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| marktprod_systemid | INTEGER | 10 | NO | id |
| marktprod_marktid | INTEGER | 10 | NO | id |
| marktprod_btnr | CHAR | 7 | NO | diagram_number |
| marktprod_varianteid | INTEGER | 10 | NO | id |

## w_netzurl (netzurl)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| netzurl_netz | VARCHAR | 12 | NO | net_url |
| netzurl_krit | VARCHAR | 40 | NO | net_url |
| netzurl_krit_textcode | INTEGER | 10 | NO | text_code |
| netzurl_url_asap | VARCHAR | 256 | NO | net_url |
| netzurl_asaptunnel | CHAR | 1 | NO | net_url |
| netzurl_url_zr | VARCHAR | 256 | NO | net_url |
| netzurl_url_dom_basic | VARCHAR | 256 | NO | net_url |
| netzurl_url_dom_options | VARCHAR | 256 | NO | net_url |

## w_normnummer (Standard Number)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| nn_nnid | VARCHAR | 8 | NO | id |
| nn_art | VARCHAR | 3 | NO | type |
| nn_grafikid | INTEGER | 10 | NO | graphic_id |
| nn_marke_tps | VARCHAR | 11 | NO | brand |
| nn_produktart | CHAR | 1 | NO | product_type |
| nn_pos | SMALLINT | 5 | NO | position |
| nn_nngid | VARCHAR | 8 | NO | id |

## w_normnummergruppe (Standard Number - gruppe)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| nng_nngid | VARCHAR | 8 | NO | id |
| nng_grafikid | INTEGER | 10 | NO | graphic_id |
| nng_marke_tps | VARCHAR | 11 | NO | brand |
| nng_produktart | CHAR | 1 | NO | product_type |
| nng_pos | SMALLINT | 5 | NO | position |

## w_normteilben (normteilben)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| normteilben_marke_tps | VARCHAR | 11 | NO | brand |
| normteilben_produktart | CHAR | 1 | NO | product_type |
| normteilben_textcode | INTEGER | 10 | NO | text_code |

## w_og (Option Group)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| og_pos | SMALLINT | 5 | NO | position |
| og_textcode | INTEGER | 10 | NO | text_code |
| og_id | VARCHAR | 8 | NO | id |

## w_publben (Public Names (Multilingual))

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| publben_art | VARCHAR | 40 | NO | type |
| publben_textcode | INTEGER | 10 | NO | text_code |
| publben_bezeichnung | VARCHAR | 80 | NO | description |

## w_refa (refa)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| refa_id | INTEGER | 10 | NO | id |
| refa_awnummer | VARCHAR | 9 | NO | - |
| refa_kommid | INTEGER | 10 | NO | comment_id |

## w_sft (Special Tool)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sft_sftid | INTEGER | 10 | NO | id |
| sft_gilt_v | INTEGER | 10 | NO | valid_from |
| sft_gilt_b | INTEGER | 10 | YES | valid_to |
| sftid_ist_aspg | CHAR | 1 | YES | - |

## w_sft_aspg (Special Tool - ASPG)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sftaspg_sftid | INTEGER | 10 | NO | id |
| sftaspg_sachnr_pg | CHAR | 7 | NO | part_number |
| sftaspg_btnr | CHAR | 7 | YES | diagram_number |
| sftaspg_mospid | INTEGER | 10 | NO | model_series_prod_id |
| sftaspg_achse | CHAR | 1 | YES | - |

## w_sftfeder (Special Tool - feder)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sftfeder_sftid | INTEGER | 10 | NO | id |
| sftfeder_sachnr_r | CHAR | 7 | NO | part_number |
| sftfeder_sachnr_l | CHAR | 7 | NO | part_number |
| sftfeder_kz_vh | CHAR | 1 | NO | - |
| sftfeder_punkte_von | SMALLINT | 5 | NO | - |
| sftfeder_punkte_bis | SMALLINT | 5 | NO | - |

## w_sftmosp (Special Tool - mosp)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sftmosp_sftid | INTEGER | 10 | NO | id |
| sftmosp_mospid | INTEGER | 10 | NO | model_series_prod_id |

## w_sftsala (Special Tool - sala)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sftsala_sftid | INTEGER | 10 | NO | id |
| sftsala_salaid | INTEGER | 10 | NO | id |
| sftsala_va_punkte_r | SMALLINT | 5 | NO | - |
| sftsala_va_punkte_l | SMALLINT | 5 | NO | - |
| sftsala_ha_punkte_r | SMALLINT | 5 | NO | - |
| sftsala_ha_punkte_l | SMALLINT | 5 | NO | - |

## w_sfttyp (Special Tool - typ)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sfttyp_sftid | INTEGER | 10 | NO | id |
| sfttyp_typ | CHAR | 4 | NO | - |
| sfttyp_punkte_va_r | SMALLINT | 5 | NO | - |
| sfttyp_punkte_va_l | SMALLINT | 5 | NO | - |
| sfttyp_punkte_ha_r | SMALLINT | 5 | NO | - |
| sfttyp_punkte_ha_l | SMALLINT | 5 | NO | - |

## w_si (si)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| si_sachnr | CHAR | 7 | NO | part_number |
| si_doknr | VARCHAR | 15 | NO | number |
| si_dokart | VARCHAR | 6 | NO | type |

## w_sowuinfo (sowuinfo)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| sowuinfo_salaid | INTEGER | 10 | NO | id |
| sowuinfo_contactsowu | CHAR | 1 | NO | - |
| sowuinfo_showtext | CHAR | 1 | NO | text |
| sowuinfo_detailinfo | VARCHAR | 40 | YES | - |

## w_system (system)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| system_id | INTEGER | 10 | NO | id |
| system_kuerzel | VARCHAR | 10 | NO | abbreviation |

## w_tc_delta (tc delta)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tcd_version | VARCHAR | 20 | NO | - |

## w_tc_kampagne (tc kampagne)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tck_id | INTEGER | 10 | NO | id |
| tck_marke_tps | VARCHAR | 11 | NO | brand |
| tck_produktart | CHAR | 1 | NO | product_type |
| tck_vbereich | CHAR | 2 | NO | v_area |
| tck_pos | TINYINT | 3 | NO | position |
| tck_benennung | VARCHAR | 40 | NO | - |
| tck_landkuerzel | CHAR | 2 | NO | abbreviation |
| tck_datum_von | INTEGER | 10 | NO | date |
| tck_datum_bis | INTEGER | 10 | YES | date |
| tck_baureihen | VARCHAR | 500 | NO | series |
| tck_baureihen_proddat_von | INTEGER | 10 | YES | series |
| tck_baureihen_proddat_bis | INTEGER | 10 | YES | series |
| tck_motoren | VARCHAR | 500 | NO | engine |

## w_tc_kampagne_proddatum (tc kampagne proddatum)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tckp_id | INTEGER | 10 | NO | id |
| tckp_mospid | INTEGER | 10 | NO | model_series_prod_id |
| tckp_proddatum_von | INTEGER | 10 | NO | date |
| tckp_proddatum_bis | INTEGER | 10 | YES | date |

## w_tc_performance (tc performance)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tcp_mospid | INTEGER | 10 | NO | model_series_prod_id |
| tcp_sachnr | CHAR | 7 | NO | part_number |
| tcp_datum_von | INTEGER | 10 | NO | date |
| tcp_datum_bis | INTEGER | 10 | YES | date |
| tcp_proddat_rel | CHAR | 1 | NO | - |
| tcp_landkuerzel | CHAR | 2 | NO | abbreviation |

## w_tc_performance_allg (tc performance allg)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tcp_sachnr | CHAR | 7 | NO | part_number |
| tcp_marke_tps | VARCHAR | 11 | NO | brand |
| tcp_produktart | CHAR | 1 | NO | product_type |
| tcp_vbereich | CHAR | 2 | NO | v_area |
| tcp_landkuerzel | CHAR | 2 | NO | abbreviation |
| tcp_datum_von | INTEGER | 10 | NO | date |
| tcp_datum_bis | INTEGER | 10 | YES | date |

## w_tc_sachnummer (tc sachnummer)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tcs_id | INTEGER | 10 | NO | id |
| tcs_sachnr | CHAR | 7 | NO | part_number |

## w_teil (Parts)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teil_sachnr | CHAR | 7 | NO | part_number |
| teil_hauptgr | CHAR | 2 | NO | main_group |
| teil_untergrup | CHAR | 2 | NO | sub_group |
| teil_textcode | INTEGER | 10 | NO | text_code |
| teil_textcode_kom | INTEGER | 10 | YES | text_code |
| teil_kom_pi | INTEGER | 10 | YES | - |
| teil_benennzus | VARCHAR | 30 | YES | - |
| teil_rundung | CHAR | 1 | YES | - |
| teil_produktkl | VARCHAR | 2 | YES | - |
| teil_alt | CHAR | 7 | YES | - |
| teil_austausch_alt | CHAR | 2 | YES | - |
| teil_tausch | CHAR | 7 | YES | - |
| teil_art | CHAR | 1 | YES | type |
| teil_mengeeinh | VARCHAR | 2 | YES | quantity |
| teil_einsatz | INTEGER | 10 | YES | - |
| teil_einsatz_serie | INTEGER | 10 | YES | - |
| teil_entfall_kez | CHAR | 1 | YES | - |
| teil_entfall_dat | INTEGER | 10 | YES | - |
| teil_ident | CHAR | 1 | YES | - |
| teil_gefahr_kl | CHAR | 1 | YES | - |
| teil_vorhanden_si | CHAR | 1 | YES | available |
| teil_vorhanden_si_ohne_lzb | CHAR | 1 | YES | available |
| teil_ist_eba | CHAR | 1 | YES | - |
| teil_rueck_pfl | CHAR | 1 | YES | - |
| teil_rueck_pfldat | INTEGER | 10 | YES | - |
| teil_vorverpac | INTEGER | 10 | YES | - |
| teil_lagerverp | INTEGER | 10 | YES | - |
| teil_beh_verp | INTEGER | 10 | YES | - |
| teil_mam | INTEGER | 10 | YES | - |
| teil_technik | CHAR | 1 | YES | - |
| teil_dispo | CHAR | 1 | YES | - |
| teil_bestellbar | CHAR | 1 | YES | - |
| teil_teile_gew | NUMERIC | 9 | YES | - |
| teil_lkz | CHAR | 3 | YES | - |
| teil_produktart | CHAR | 1 | YES | product_type |
| teil_recycling_kez | CHAR | 2 | YES | - |
| teil_fertigungshinweis | CHAR | 2 | YES | - |
| teil_normnummer | VARCHAR | 8 | YES | - |
| teil_ebv | CHAR | 1 | YES | - |
| teil_lzb | CHAR | 1 | YES | - |
| teil_verbaubar | CHAR | 1 | YES | - |
| teil_bedarfsteil | CHAR | 1 | YES | - |
| teil_preis | CHAR | 1 | YES | price |
| teil_ist_valueline | CHAR | 1 | YES | - |
| teil_ist_reach | CHAR | 1 | YES | - |
| teil_ist_aspg | CHAR | 1 | YES | - |
| teil_ist_stecker | CHAR | 1 | YES | - |
| teil_ist_diebstahlrelevant | CHAR | 1 | YES | - |
| teil_ist_reifen | CHAR | 1 | YES | - |

## w_teil_aspg (Parts - ASPG)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilaspg_sachnr | CHAR | 7 | NO | part_number |
| teilaspg_sachnr_pg | CHAR | 7 | NO | part_number |
| teilaspg_kz_gruppe | CHAR | 2 | NO | - |
| teilaspg_vmenge | SMALLINT | 5 | NO | quantity |

## w_teil_atb (Parts - ATB)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilatb_sachnr_alt | CHAR | 7 | NO | part_number |
| teilatb_sachnr_neu | CHAR | 7 | NO | part_number |
| teilatb_kennz | CHAR | 1 | NO | - |
| teilatb_bap | CHAR | 1 | NO | - |

## w_teil_marken (Parts - marken)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilm_sachnr | CHAR | 7 | NO | part_number |
| teilm_marke_tps | VARCHAR | 11 | NO | brand |

## w_teil_reach (Parts - REACH compliance)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilreach_sachnr | CHAR | 7 | NO | part_number |
| teilreach_casnr | VARCHAR | 20 | NO | number |
| teilreach_casname | VARCHAR | 250 | NO | name |
| teilreach_gewanteil | NUMERIC | 3 | NO | - |

## w_teil_skip (Parts - Skip)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilskip_sachnr | CHAR | 7 | NO | part_number |

## w_teil_spezben (Parts - Special Names)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| teilsb_sachnr | CHAR | 7 | NO | part_number |
| teilsb_spriso | CHAR | 2 | NO | - |
| teilsb_sprregiso | CHAR | 2 | NO | - |
| teilsb_text | VARCHAR | 80 | NO | text |

## w_teileersetzung (Part Replacement)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| ts_hg | CHAR | 2 | NO | - |
| ts_mospid | INTEGER | 10 | NO | model_series_prod_id |
| ts_sachnr | CHAR | 7 | NO | part_number |
| ts_lenkung | CHAR | 1 | YES | steering |

## w_teileersetzung_suche (Part Replacement - Search)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tss_hg | CHAR | 2 | NO | - |
| tss_mospid | INTEGER | 10 | NO | model_series_prod_id |
| tss_lenkung | CHAR | 1 | YES | steering |
| tss_einsatz_serie_max | INTEGER | 10 | YES | - |

## w_teileverwendungfzg_suche (Parts - everwendungfzg_suche)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tvs_hg | CHAR | 2 | NO | - |
| tvs_mospid | INTEGER | 10 | NO | model_series_prod_id |
| tvs_lenkung | CHAR | 1 | YES | steering |
| tvs_einsatz_serie_max | INTEGER | 10 | YES | - |

## w_tl_sprache_bnb (tl sprache bnb)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| tlsb_iso | CHAR | 2 | NO | - |
| tlsb_regiso | CHAR | 2 | NO | - |
| tlsb_bildnummer | CHAR | 2 | NO | - |

## w_unterkategorie (Sub Category)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| unterkat_id | INTEGER | 10 | NO | id |
| unterkat_hkid | INTEGER | 10 | NO | id |
| unterkat_textcode | INTEGER | 10 | NO | text_code |
| unterkat_grafikid | INTEGER | 10 | YES | graphic_id |
| unterkat_pos | SMALLINT | 5 | NO | position |
| unterkat_ist_lifestyle | CHAR | 1 | NO | - |

## w_variante (Variant)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| variante_id | INTEGER | 10 | NO | id |
| variante_textcode | INTEGER | 10 | NO | text_code |

## w_vbez_pos (vbez pos)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| vbezp_baureihe | VARCHAR | 4 | NO | series |
| vbezp_vbez | VARCHAR | 30 | NO | - |
| vbezp_pos | SMALLINT | 5 | NO | position |

## w_verwaltung (Administration)

| Column | Type | Size | Nullable | English |
|--------|------|------|----------|---------|
| verwaltung_info | VARCHAR | 20 | NO | - |
| verwaltung_wert | VARCHAR | 20 | NO | - |

