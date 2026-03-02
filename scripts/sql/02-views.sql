-- ETKx Views (language-neutral)
-- Order: 02 (run after indexes)
-- These views pre-join related tables but leave language as parameter

-- Series/Baureihe base info with textcode for name lookup
DROP VIEW IF EXISTS v_series;
CREATE VIEW v_series AS
SELECT 
    baureihe_baureihe AS baureihe,
    baureihe_produktart AS produktart,
    baureihe_bauart AS bauart,
    baureihe_vbereich AS vbereich,
    baureihe_bereich AS bereich,
    baureihe_marke_tps AS marke,
    baureihe_grafikid AS grafikId,
    baureihe_position AS position,
    baureihe_textcode AS textcode
FROM w_baureihe;

-- Main groups (fg = '00') with textcode
DROP VIEW IF EXISTS v_main_groups;
CREATE VIEW v_main_groups AS
SELECT 
    hgfg_hg AS hg,
    hgfg_produktart AS produktart,
    hgfg_bereich AS bereich,
    hgfg_grafikid AS grafikId,
    hgfg_textcode AS textcode
FROM w_hgfg
WHERE hgfg_fg = '00';

-- Sub groups (fg != '00') with textcode
DROP VIEW IF EXISTS v_sub_groups;
CREATE VIEW v_sub_groups AS
SELECT 
    hgfg_hg AS hg,
    hgfg_fg AS fg,
    hgfg_produktart AS produktart,
    hgfg_bereich AS bereich,
    hgfg_grafikid AS grafikId,
    hgfg_textcode AS textcode
FROM w_hgfg
WHERE hgfg_fg != '00';

-- Diagrams with textcode
DROP VIEW IF EXISTS v_diagrams;
CREATE VIEW v_diagrams AS
SELECT 
    bildtaf_btnr AS btnr,
    bildtaf_hg AS hg,
    bildtaf_fg AS fg,
    bildtaf_produktart AS produktart,
    bildtaf_vbereich AS vbereich,
    bildtaf_grafikid AS grafikId,
    bildtaf_pos AS pos,
    bildtaf_bereich AS bereich,
    bildtaf_lkz AS lkz,
    bildtaf_textc AS textcode
FROM w_bildtaf;

-- Parts with textcode (basic info)
DROP VIEW IF EXISTS v_parts;
CREATE VIEW v_parts AS
SELECT 
    teil_sachnr AS sachnr,
    teil_hauptgr AS hauptgr,
    teil_untergrup AS untergrup,
    teil_benennzus AS zusatz,
    teil_art AS art,
    teil_mengeeinh AS mengeeinh,
    teil_einsatz AS einsatz,
    teil_entfall_dat AS entfall_dat,
    teil_produktart AS produktart,
    teil_textcode AS textcode,
    teil_textcode_kom AS textcode_kom
FROM w_teil;

-- Vehicle types with series info (joined)
DROP VIEW IF EXISTS v_vehicle_types;
CREATE VIEW v_vehicle_types AS
SELECT 
    f.fztyp_mospid AS mospid,
    f.fztyp_typschl AS typschl,
    f.fztyp_baureihe AS baureihe,
    f.fztyp_motor AS motor,
    f.fztyp_karosserie AS karosserie,
    f.fztyp_getriebe AS getriebe,
    f.fztyp_lenkung AS lenkung,
    f.fztyp_vbereich AS vbereich,
    f.fztyp_ktlgausf AS ktlgausf,
    f.fztyp_einsatz AS einsatz,
    f.fztyp_sichtschutz AS sichtschutz,
    b.baureihe_marke_tps AS marke,
    b.baureihe_produktart AS produktart,
    b.baureihe_textcode AS series_textcode
FROM w_fztyp f
INNER JOIN w_baureihe b ON f.fztyp_baureihe = b.baureihe_baureihe;

-- Groups per vehicle (mosp) - commonly used join
DROP VIEW IF EXISTS v_vehicle_groups;
CREATE VIEW v_vehicle_groups AS
SELECT 
    m.hgfgm_mospid AS mospid,
    m.hgfgm_hg AS hg,
    m.hgfgm_fg AS fg,
    m.hgfgm_produktart AS produktart,
    h.hgfg_textcode AS textcode,
    h.hgfg_grafikid AS grafikId
FROM w_hgfg_mosp m
INNER JOIN w_hgfg h ON (m.hgfgm_hg = h.hgfg_hg AND m.hgfgm_fg = h.hgfg_fg);

-- Diagram lines base (btzeilen + verbauung) - heavy join simplified
DROP VIEW IF EXISTS v_diagram_lines;
CREATE VIEW v_diagram_lines AS
SELECT 
    v.btzeilenv_mospid AS mospid,
    v.btzeilenv_btnr AS btnr,
    v.btzeilenv_pos AS pos,
    v.btzeilenv_vmenge AS menge,
    v.btzeilenv_sachnr AS sachnr,
    v.btzeilenv_alter_kz AS alter_kz,
    b.btzeilen_bildposnr AS bildposnr,
    b.btzeilen_hg AS hg,
    b.btzeilen_kat AS kat,
    b.btzeilen_automatik AS automatik,
    b.btzeilen_lenkg AS lenkung,
    b.btzeilen_eins AS einsatz,
    b.btzeilen_auslf AS auslauf,
    b.btzeilen_bedkez AS bedkez,
    b.btzeilen_regelnr AS regelnr,
    b.btzeilen_kommbt AS kommbt,
    b.btzeilen_kommvor AS kommvor,
    b.btzeilen_kommnach AS kommnach,
    b.btzeilen_gruppeid AS gruppeid,
    b.btzeilen_blocknr AS blocknr
FROM w_btzeilen_verbauung v
INNER JOIN w_btzeilen b ON (v.btzeilenv_btnr = b.btzeilen_btnr AND v.btzeilenv_pos = b.btzeilen_pos);
