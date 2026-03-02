-- ETKx Performance Indexes
-- Based on actual query patterns in DAL repositories

-- Translations - textcode + iso for JOINs
CREATE INDEX IF NOT EXISTS idx_w_ben_gk_textcode_iso ON w_ben_gk(ben_textcode, ben_iso);
CREATE INDEX IF NOT EXISTS idx_w_ben_gk_textcode_iso_regiso ON w_ben_gk(ben_textcode, ben_iso, ben_regiso);

-- Main/Sub Groups - for catalog navigation
CREATE INDEX IF NOT EXISTS idx_w_hgfg_hg_fg ON w_hgfg(hgfg_hg, hgfg_fg);
CREATE INDEX IF NOT EXISTS idx_w_hgfg_textcode ON w_hgfg(hgfg_textcode);

-- Groups per vehicle (w_hgfg_mosp)
CREATE INDEX IF NOT EXISTS idx_w_hgfg_mosp_mospid ON w_hgfg_mosp(hgfgm_mospid);
CREATE INDEX IF NOT EXISTS idx_w_hgfg_mosp_mospid_hg ON w_hgfg_mosp(hgfgm_mospid, hgfgm_hg);
CREATE INDEX IF NOT EXISTS idx_w_hgfg_mosp_hg_fg ON w_hgfg_mosp(hgfgm_hg, hgfgm_fg);

-- Thumbnails
CREATE INDEX IF NOT EXISTS idx_w_hg_thumbnail_hg ON w_hg_thumbnail(hgthb_hg);
CREATE INDEX IF NOT EXISTS idx_w_hg_thumbnail_hg_produktart ON w_hg_thumbnail(hgthb_hg, hgthb_produktart, hgthb_marke_tps);

-- Diagrams - for navigation and display
CREATE INDEX IF NOT EXISTS idx_w_bildtaf_hg_fg ON w_bildtaf(bildtaf_hg, bildtaf_fg);
CREATE INDEX IF NOT EXISTS idx_w_bildtaf_textc ON w_bildtaf(bildtaf_textc);

-- Diagram lines with vehicle context
CREATE INDEX IF NOT EXISTS idx_w_btzeilen_verbauung_mospid ON w_btzeilen_verbauung(btzeilenv_mospid);
CREATE INDEX IF NOT EXISTS idx_w_btzeilen_verbauung_mospid_btnr ON w_btzeilen_verbauung(btzeilenv_mospid, btzeilenv_btnr);
CREATE INDEX IF NOT EXISTS idx_w_btzeilen_verbauung_btnr ON w_btzeilen_verbauung(btzeilenv_btnr);

-- Parts - textcode for name lookups
CREATE INDEX IF NOT EXISTS idx_w_teil_textcode ON w_teil(teil_textcode);

-- Vehicle type - baureihe for JOINs
CREATE INDEX IF NOT EXISTS idx_w_fztyp_baureihe ON w_fztyp(fztyp_baureihe);

-- Series - baureihe for lookups
CREATE INDEX IF NOT EXISTS idx_w_baureihe_marke ON w_baureihe(baureihe_marke_tps);

-- Part Replacements
CREATE INDEX IF NOT EXISTS idx_w_teileersetzung_sachnr ON w_teileersetzung(ts_sachnr);
CREATE INDEX IF NOT EXISTS idx_w_teileersetzung_mospid ON w_teileersetzung(ts_mospid);

-- VIN Lookup
CREATE INDEX IF NOT EXISTS idx_w_fgstnr_anf ON w_fgstnr(fgstnr_anf);
CREATE INDEX IF NOT EXISTS idx_w_fgstnr_typschl ON w_fgstnr(fgstnr_typschl);
