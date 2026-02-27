# BMW ETK Database Schema

## Overview
- **Database:** Transbase (etk_publ)
- **Total Tables:** 116
- **Source:** Decompiled from SQLStatementsTransbase.java

## Core Tables

### w_teil (Parts Master)
Main parts catalog table.

| Column | Description |
|--------|-------------|
| teil_sachnr | Part number (7-digit) |
| teil_art | Part type |
| teil_marke | Brand (BMW/MINI/RR) |
| teil_hauptgr | Main group (HG) |
| teil_untergrup | Sub group (UG) |
| teil_mengeeinh | Unit of measure |
| teil_teile_gew | Part weight |
| teil_bestellbar | Orderable flag |
| teil_entfall_dat | Discontinuation date |
| teil_entfall_kez | Discontinuation code |
| teil_austausch_alt | Replacement part (old) |
| teil_ist_diebstahlrelevant | Theft-relevant flag |
| teil_ist_reach | REACH compliance |
| teil_ist_stecker | Connector flag |
| teil_ist_valueline | Value line flag |
| teil_normnummer | Standard number |
| teil_produktart | Product type |
| teil_produktkl | Product class |
| teil_lkz | Country code |
| teil_lzb | Availability code |

### w_bildtaf (Diagrams/Illustrations)
Exploded view diagrams showing parts.

| Column | Description |
|--------|-------------|
| bildtaf_btnr | Diagram number |
| bildtaf_hg | Main group |
| bildtaf_fg | Function group |
| bildtaf_grafikid | Graphic ID (image reference) |
| bildtaf_marke | Brand |
| bildtaf_bereich | Area/Section |
| bildtaf_bteart | Diagram type |
| bildtaf_bedkez | Condition code |
| bildtaf_textc | Text code |
| bildtaf_pos | Position |
| bildtaf_lkz | Country code |
| bildtaf_sicher | Security flag |

### w_btzeilen (Diagram Lines/Parts List)
Parts listed in each diagram.

| Column | Description |
|--------|-------------|
| btzeilen_btnr | Diagram number (FK to w_bildtaf) |
| btzeilen_pos | Position in diagram |
| btzeilen_sachnr | Part number |
| btzeilen_menge | Quantity |
| btzeilen_bedkez | Condition code |
| btzeilen_textc | Text code |
| btzeilen_cp | Change point |

### w_fztyp (Vehicle Types)
Vehicle model definitions.

| Column | Description |
|--------|-------------|
| fztyp_mospid | Model/Series/Production ID |
| fztyp_baureihe | Series (E46, F30, etc.) |
| fztyp_typschl | Type key |
| fztyp_motor | Engine code |
| fztyp_getriebe | Transmission type |
| fztyp_karosserie | Body type |
| fztyp_lenkung | Steering (LHD/RHD) |
| fztyp_vbez | Model designation |
| fztyp_erwvbez | Extended designation |
| fztyp_einsatz | Usage/market |
| fztyp_vbereich | Validity range |
| fztyp_ktlgausf | Catalog version |

### w_baureihe (Model Series)
Series definitions (E46, F30, G20, etc.)

| Column | Description |
|--------|-------------|
| baureihe_baureihe | Series code |
| baureihe_bauart | Construction type |
| baureihe_grafikid | Graphic ID (silhouette) |
| baureihe_textcode | Text code |
| baureihe_vbereich | Validity range |
| baureihe_produktart | Product type |
| baureihe_position | Sort position |

### w_hgfg (Main Group / Function Group)
Parts catalog structure.

| Column | Description |
|--------|-------------|
| hgfg_hg | Main group |
| hgfg_fg | Function group |
| hgfg_textcode | Text code for name |
| hgfg_grafikid | Group icon |

### w_grafik (Graphics/Images)
Image storage.

| Column | Description |
|--------|-------------|
| grafik_id | Graphic ID |
| grafik_art | Type (BT=diagram, HG=group, etc.) |
| grafik_daten | Binary image data |

### w_preise (Prices)
Part pricing.

| Column | Description |
|--------|-------------|
| preise_sachnr | Part number |
| preise_evpreis | Price |
| preise_rabattschluessel | Discount code |
| preise_mwst | VAT rate |

### w_publben (Part Names/Descriptions)
Multilingual part descriptions.

| Column | Description |
|--------|-------------|
| publben_textcode | Text code |
| publben_iso | Language ISO code |
| publben_regiso | Region ISO code |
| publben_text | Description text |

## Condition/Validity Tables

### w_bed (Conditions)
Parts applicability conditions.

| Column | Description |
|--------|-------------|
| bed_bedkez | Condition code |
| bed_baureihe | Applicable series |
| bed_sala | SA (Special Equipment) code |
| bed_mospid | Model/Series/Production ID |

### w_bed_sala (SA Conditions)
Special equipment conditions.

### w_bed_afl (Production Date Conditions)
Valid from/to production dates.

## User/Configuration Tables

### w_user
| user_id | user_name | user_passwort | user_firma_id | user_filiale_id |

### w_firma (Companies)
Dealer/company definitions.

### w_filiale (Branches)
Branch locations.

### w_konfig (Configuration)
User settings and preferences.

### w_teileliste (Shopping Lists)
User part lists.

### w_teilelistepos (Shopping List Items)
Parts in user lists.

## All Tables (116)

```
w_abk              w_fremdtl           w_normnummer
w_auftrag          w_fuellmengen       w_normnummergruppe
w_bauart           w_fztyp             w_normteilben
w_baureihe         w_grafik            w_preise
w_baureihe_kar_thb w_grafik_hs         w_proxy
w_bed              w_grp_information   w_publben
w_bed_afl          w_hg_thumbnail      w_sft
w_bed_aflpc        w_hgfg              w_sft_aspg
w_bed_etktext      w_hgfg_mosp         w_sftfeder
w_bed_sala         w_hist              w_sftsala
w_bed_zusatzinfo   w_komm              w_sfttyp
w_bedeutung        w_komm_help         w_si
w_ben_gk           w_kommugb_help      w_tc_kampagne
w_bestellliste     w_kompl_einzelteil  w_tc_kampagne_proddatum
w_bestelllistepos  w_kompl_satz        w_tc_performance
w_bildtaf          w_konfig            w_tc_performance_allg
w_bildtaf_bnbben   w_mailadressen      w_tc_sachnummer
w_bildtaf_cp       w_markt_etk         w_teil
w_bildtaf_marke    w_markt_ipac        w_teil_aspg
w_bildtaf_suche    w_netz              w_teil_atb
w_bildtaf_verweis  w_netzurl           w_teil_marken
w_bildtafzub       w_news_grafik       w_teil_reach
w_bte_bedelem      w_news_text         w_teileersetzung
w_bte_bedgesamt                        w_teileersetzung_suche
w_bte_bedkurz                          w_teileinfo
w_bte_bedog                            w_teileliste
w_bte_bedueber                         w_teileliste_hist
w_btzeilen                             w_teileliste_job
w_btzeilen_cp                          w_teileliste_sendeinfo
w_btzeilen_verbauung                   w_teileliste_srp
w_btzeilenugb                          w_teilelistepos
w_btzeilenugb_verbauung                w_teileverwendungfzg_suche
w_eg                                   w_tipp
w_erstbevorratung                      w_tl_sprache_bnb
w_erstbevorratung_suche                w_url
w_etk_grafiken                         w_user
w_eu_reifen                            w_user_berechtigungen
w_fg_thumbnail                         w_user_einstellungen
w_fgstnr                               w_user_einstellungen_region
w_fgstnr_sala                          w_user_einstellungen_wmaerkte
w_filiale                              w_user_funktionsrechte
w_firma                                w_user_log
w_firma_berechtigungen                 w_user_mailoptions
                                       w_user_rr
                                       w_user_tabellenkonfig
                                       w_user_tipps
                                       w_vbez_pos
                                       w_verwaltung
                                       w_zub_konfig
                                       w_zub_user
```

## Key Relationships

```
w_baureihe (series)
    └── w_fztyp (vehicle types) via fztyp_baureihe
        └── w_bed (conditions) via bed_mospid = fztyp_mospid
            └── w_btzeilen (part lines) via btzeilen_bedkez = bed_bedkez

w_bildtaf (diagrams)
    └── w_btzeilen (parts in diagram) via btzeilen_btnr = bildtaf_btnr
        └── w_teil (part details) via btzeilen_sachnr = teil_sachnr

w_teil (parts)
    └── w_publben (names) via teil_textcode
    └── w_preise (prices) via preise_sachnr = teil_sachnr
    └── w_teileersetzung (replacements) via teileersetzung_sachnr
```

## Data Flow

1. **Vehicle Selection:** w_baureihe → w_fztyp
2. **Group Navigation:** w_hgfg (HG/FG hierarchy)
3. **Diagram Display:** w_bildtaf → w_grafik (image)
4. **Parts List:** w_btzeilen → w_teil → w_publben (names) → w_preise
5. **Conditions:** w_bed* tables filter by SA codes, production dates, markets

## Notes

- Part numbers (sachnr) are 7 digits
- Graphics stored as BLOBs in w_grafik
- Text in multiple languages via w_publben (ISO/RegISO codes)
- Conditions system is complex (bed_bedkez links to multiple condition types)
