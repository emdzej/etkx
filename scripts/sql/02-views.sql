-- ETKx Views
-- Order: 02 (run after indexes)
-- These views simplify common query patterns with English names pre-joined

-- Series/Baureihe with names
DROP VIEW IF EXISTS v_series;
CREATE VIEW v_series AS
SELECT 
    b.baureihe_baureihe AS baureihe,
    b.baureihe_produktart AS produktart,
    b.baureihe_bauart AS bauart,
    b.baureihe_vbereich AS vbereich,
    b.baureihe_bereich AS bereich,
    b.baureihe_marke_tps AS marke,
    b.baureihe_grafikid AS grafikId,
    b.baureihe_position AS position,
    g.ben_text AS name
FROM w_baureihe b
LEFT JOIN w_ben_gk g ON (
    b.baureihe_textcode = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
);

-- Main groups (fg = '00') with names
DROP VIEW IF EXISTS v_main_groups;
CREATE VIEW v_main_groups AS
SELECT 
    h.hgfg_hg AS hg,
    h.hgfg_produktart AS produktart,
    h.hgfg_bereich AS bereich,
    h.hgfg_grafikid AS grafikId,
    g.ben_text AS name
FROM w_hgfg h
LEFT JOIN w_ben_gk g ON (
    h.hgfg_textcode = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
)
WHERE h.hgfg_fg = '00';

-- Sub groups (fg != '00') with names
DROP VIEW IF EXISTS v_sub_groups;
CREATE VIEW v_sub_groups AS
SELECT 
    h.hgfg_hg AS hg,
    h.hgfg_fg AS fg,
    h.hgfg_produktart AS produktart,
    h.hgfg_bereich AS bereich,
    h.hgfg_grafikid AS grafikId,
    g.ben_text AS name
FROM w_hgfg h
LEFT JOIN w_ben_gk g ON (
    h.hgfg_textcode = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
)
WHERE h.hgfg_fg != '00';

-- Diagrams with names
DROP VIEW IF EXISTS v_diagrams;
CREATE VIEW v_diagrams AS
SELECT 
    b.bildtaf_btnr AS btnr,
    b.bildtaf_hg AS hg,
    b.bildtaf_fg AS fg,
    b.bildtaf_produktart AS produktart,
    b.bildtaf_vbereich AS vbereich,
    b.bildtaf_grafikid AS grafikId,
    b.bildtaf_pos AS pos,
    b.bildtaf_bereich AS bereich,
    g.ben_text AS name
FROM w_bildtaf b
LEFT JOIN w_ben_gk g ON (
    b.bildtaf_textc = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
);

-- Parts with names (basic info for search/display)
DROP VIEW IF EXISTS v_parts;
CREATE VIEW v_parts AS
SELECT 
    t.teil_sachnr AS sachnr,
    t.teil_hauptgr AS hauptgr,
    t.teil_untergrup AS untergrup,
    t.teil_benennzus AS zusatz,
    t.teil_art AS art,
    t.teil_mengeeinh AS mengeeinh,
    t.teil_einsatz AS einsatz,
    t.teil_entfall_dat AS entfall_dat,
    t.teil_produktart AS produktart,
    g.ben_text AS name
FROM w_teil t
LEFT JOIN w_ben_gk g ON (
    t.teil_textcode = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
);

-- Vehicle types with series info
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
    f.fztyp_einsatz AS einsatz,
    b.baureihe_marke_tps AS marke,
    b.baureihe_produktart AS produktart,
    g.ben_text AS series_name
FROM w_fztyp f
LEFT JOIN w_baureihe b ON f.fztyp_baureihe = b.baureihe_baureihe
LEFT JOIN w_ben_gk g ON (
    b.baureihe_textcode = g.ben_textcode 
    AND g.ben_iso = 'en' 
    AND TRIM(g.ben_regiso) = ''
);

-- Part replacements with both part names
DROP VIEW IF EXISTS v_replacements;
CREATE VIEW v_replacements AS
SELECT 
    r.ts_sachnr AS old_sachnr,
    r.ts_hg AS hg,
    r.ts_mospid AS mospid,
    r.ts_lenkung AS lenkung,
    old_p.ben_text AS old_name,
    new_t.teil_sachnr AS new_sachnr,
    new_p.ben_text AS new_name
FROM w_teileersetzung r
LEFT JOIN w_teil old_t ON r.ts_sachnr = old_t.teil_sachnr
LEFT JOIN w_ben_gk old_p ON (
    old_t.teil_textcode = old_p.ben_textcode 
    AND old_p.ben_iso = 'en' 
    AND TRIM(old_p.ben_regiso) = ''
)
LEFT JOIN w_teileersetzung_neu n ON r.ts_sachnr = n.tsn_sachnr
LEFT JOIN w_teil new_t ON n.tsn_neusachnr = new_t.teil_sachnr
LEFT JOIN w_ben_gk new_p ON (
    new_t.teil_textcode = new_p.ben_textcode 
    AND new_p.ben_iso = 'en' 
    AND TRIM(new_p.ben_regiso) = ''
);
