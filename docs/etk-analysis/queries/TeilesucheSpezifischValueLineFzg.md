# TeilesucheSpezifischValueLineFzg SQL Queries

Total queries: 1

## Table Relationships (Co-occurrence)

- w_kompl_satz ↔ w_markt_etk ↔ w_btzeilen_verbauung ↔ w_bildtaf ↔ w_ben_gk ↔ w_bildtaf_suche

## RETRIEVE_BTES_VALUE_LINE
**Description:** Retrieves data from w_kompl_satz, w_markt_etk, w_btzeilen_verbauung, w_bildtaf, w_ben_gk, w_bildtaf_suche filtered by marktetk_lkz, tbildtaf_hg, tbildtaf_fg, tbildtafs_btnr, tbildtafs_hg, tbildtafs_mospid, tben_textcode, tben_iso, tben_regiso, ks_sachnr_satz, ks_hg, btzeilenv_btnr, btzeilenv_mospid, bildtafs_btnr, bildtafs_hg, bildtafs_mospid, ben_textcode, ben_iso, ben_regiso and ordered by BildtafelHG, BildtafelNr. Used in the TeilesucheSpezifischValueLineFzg module to support ETK workflows for kompl satz data.


- Type: SELECT
- Tables: w_kompl_satz, w_markt_etk, w_btzeilen_verbauung, w_bildtaf, w_ben_gk, w_bildtaf_suche
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct                     bildtaf_hg BildtafelHG,                     bildtaf_btnr BildtafelNr,            \t\t\tbildtaf_bteart BildtafelArt, \t\t\t\t\tben_text Benennung, \t\t\t\t\tbildtaf_pos Pos, \t\t\t\t\tbildtaf_kommbt Kommentar, \t\t\t\t\tbildtaf_vorh_cp CPVorhanden, \t\t\t\t\tbildtaf_bedkez BedingungKZ, \t\t\t\t\tmarktetk_isokz MarktIso  from  \t\t\t\tw_hgfg, \t\t\t\t\tw_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz), \t\t\t\t\tw_ben_gk , \t\t\t\t\tw_bildtaf_suche  where \t\t\t\thgfg_ist_valueline = 'J'  and \t\t\t\tbildtaf_hg = hgfg_hg  and \t\t\t\tbildtaf_fg = hgfg_fg  and \t\t\t\tbildtafs_btnr = bildtaf_btnr  and \t\t\t\tbildtafs_hg = bildtaf_hg  and \t\t\t\tbildtafs_mospid = &MOSP&  and \t\t\t\tben_textcode = bildtaf_textc  and \t\t\t\tben_iso = '&ISO&'  and \t\t\t\tben_regiso = '&REGISO&'  union  select distinct                     bildtaf_hg BildtafelHG,                     bildtaf_btnr BildtafelNr,            \t\t\tbildtaf_bteart BildtafelArt, \t\t\t\t\tben_text Benennung, \t\t\t\t\tbildtaf_pos Pos, \t\t\t\t\tbildtaf_kommbt Kommentar, \t\t\t\t\tbildtaf_vorh_cp CPVorhanden, \t\t\t\t\tbildtaf_bedkez BedingungKZ, \t\t\t\t\tmarktetk_isokz MarktIso  from              w_kompl_satz     ,              w_btzeilen_verbauung     ,              w_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)     ,              w_ben_gk     ,              w_bildtaf_suche  where             ks_ist_valueline = 'J'  and               ks_sachnr_satz = btzeilenv_sachnr  and               ks_hg = bildtaf_hg  and               btzeilenv_btnr   = bildtaf_btnr  and               btzeilenv_mospid = bildtafs_mospid  and               bildtafs_btnr = bildtaf_btnr  and               bildtafs_hg = bildtaf_hg  and               bildtafs_mospid = &MOSP&  and               ben_textcode = bildtaf_textc  and               ben_iso = '&ISO&'  and               ben_regiso = '&REGISO&'  order by          BildtafelHG         ,          BildtafelNr
```
