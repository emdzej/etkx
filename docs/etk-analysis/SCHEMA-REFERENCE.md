# ETK SQLite Schema Reference

## Core Tables

### w_teil (Parts)
Primary parts master table.

| Column | Type | Description |
|--------|------|-------------|
| teil_sachnr | TEXT | Part number (PK) |
| teil_hauptgr | TEXT | Main group |
| teil_untergrup | TEXT | Sub group |
| teil_textcode | INTEGER | Text code for name |
| teil_textcode_kom | INTEGER | Text code for comment |
| teil_kom_pi | INTEGER | Comment PI |
| teil_benennzus | TEXT | Name suffix |
| teil_rundung | TEXT | Rounding indicator |
| teil_produktkl | TEXT | Product class |
| teil_alt | TEXT | Old part number |
| teil_austausch_alt | TEXT | Exchangeable old |
| teil_tausch | TEXT | Replacement part |
| teil_art | TEXT | Part type |
| teil_mengeeinh | TEXT | Unit of measure |
| teil_teile_gew | REAL | Weight |
| teil_normnummer | TEXT | Norm number (DIN) |
| teil_ist_reach | TEXT | REACH compliant |
| teil_ist_valueline | TEXT | ValueLine part |

### w_fztyp (Vehicle Types)
Vehicle model type definitions.

| Column | Type | Description |
|--------|------|-------------|
| fztyp_mospid | INTEGER | MOSP ID |
| fztyp_typschl | TEXT | Type key |
| fztyp_vbereich | TEXT | Sales area |
| fztyp_katalysator | TEXT | Catalytic converter |
| fztyp_sichtschutz | TEXT | Visibility protection |
| fztyp_lenkung | TEXT | Steering (L/R) |
| fztyp_getriebe | TEXT | Transmission |
| fztyp_baureihe | TEXT | Model series |
| fztyp_ktlgausf | TEXT | Catalog edition |
| fztyp_vbez | TEXT | Sales designation |
| fztyp_erwvbez | TEXT | Extended sales designation |
| fztyp_motor | TEXT | Engine |
| fztyp_karosserie | TEXT | Body type |
| fztyp_einsatz | INTEGER | Usage |

### w_baureihe (Model Series)
BMW model series.

| Column | Type | Description |
|--------|------|-------------|
| baureihe_baureihe | TEXT | Series code (PK) |
| baureihe_produktart | TEXT | Product type |
| baureihe_bauart | TEXT | Construction type |
| baureihe_vbereich | TEXT | Sales area |
| baureihe_textcode | INTEGER | Text code |
| baureihe_position | INTEGER | Display position |
| baureihe_bereich | TEXT | Area |
| baureihe_marke_tps | TEXT | Brand TPS |
| baureihe_grafikid | INTEGER | Graphic ID |

### w_fgstnr (VIN Ranges)
VIN number ranges for vehicle identification.

| Column | Type | Description |
|--------|------|-------------|
| fgstnr_anf | TEXT | Start VIN |
| fgstnr_von | TEXT | From |
| fgstnr_bis | TEXT | To |
| fgstnr_typschl | TEXT | Type key |
| fgstnr_prod | INTEGER | Production date |
| fgstnr_mospid | INTEGER | MOSP ID |
| fgstnr_werk | TEXT | Factory |

### w_bildtaf (Diagram Panels)
Parts diagrams/illustrations.

| Column | Type | Description |
|--------|------|-------------|
| bildtaf_btnr | TEXT | Diagram number (PK) |
| bildtaf_hg | TEXT | Main group |
| bildtaf_fg | TEXT | Sub group |
| bildtaf_produktart | TEXT | Product type |
| bildtaf_vbereich | TEXT | Sales area |
| bildtaf_bteart | TEXT | Diagram type |
| bildtaf_textc | INTEGER | Text code |
| bildtaf_grafikid | INTEGER | Graphic ID |
| bildtaf_pos | INTEGER | Position |

### w_btzeilen (Diagram Lines)
Parts listed in diagrams.

| Column | Type | Description |
|--------|------|-------------|
| btzeilen_btnr | TEXT | Diagram number (FK) |
| btzeilen_pos | INTEGER | Position |
| btzeilen_hg | TEXT | Main group |
| btzeilen_bildposnr | TEXT | Image position |
| btzeilen_sachnr | TEXT | Part number (FK) |
| btzeilen_kat | TEXT | Category |
| btzeilen_automatik | TEXT | Automatic |
| btzeilen_lenkg | TEXT | Steering |
| btzeilen_eins | INTEGER | Start |
| btzeilen_auslf | INTEGER | End |

### w_hgfg (Main/Sub Groups)
Part group hierarchy.

| Column | Type | Description |
|--------|------|-------------|
| hgfg_hg | TEXT | Main group |
| hgfg_fg | TEXT | Sub group |
| hgfg_produktart | TEXT | Product type |
| hgfg_textcode | INTEGER | Text code |
| hgfg_bereich | TEXT | Area |
| hgfg_grafikid | INTEGER | Graphic ID |

### w_publben (Published Names)
Multilingual part names.

| Column | Type | Description |
|--------|------|-------------|
| publben_art | TEXT | Type |
| publben_textcode | INTEGER | Text code |
| publben_bezeichnung | TEXT | Designation |

### w_ben_gk (Names GK)
Localized text lookup.

| Column | Type | Description |
|--------|------|-------------|
| ben_textcode | INTEGER | Text code |
| ben_iso | TEXT | Language ISO |
| ben_regiso | TEXT | Region ISO |
| ben_text | TEXT | Text value |

### w_grafik (Graphics)
Images and diagrams (BLOBs).

| Column | Type | Description |
|--------|------|-------------|
| grafik_grafikid | INTEGER | Graphic ID (PK) |
| grafik_laenge | INTEGER | Length |
| grafik_art | TEXT | Type |
| grafik_format | TEXT | Format |
| grafik_blob | BLOB | Image data |
| grafik_moddate | REAL | Modified date |

### w_teileersetzung (Part Replacement)
Part supersession/replacement data.

| Column | Type | Description |
|--------|------|-------------|
| ts_hg | TEXT | Main group |
| ts_mospid | INTEGER | MOSP ID |
| ts_sachnr | TEXT | Part number |
| ts_lenkung | TEXT | Steering |
