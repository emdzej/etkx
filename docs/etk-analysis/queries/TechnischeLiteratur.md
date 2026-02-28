# TechnischeLiteratur SQL Queries

Total queries: 5

## Table Relationships (Co-occurrence)

- w_bildtaf ↔ w_btzeilen ↔ w_teil ↔ w_ben_gk ↔ w_btzeilen_verbauung ↔ w_btzeilenugb ↔ w_btzeilenugb_verbauung
- w_hgfg_mosp ↔ w_bildtaf ↔ w_hgfg ↔ w_ben_gk ↔ w_btzeilenugb_verbauung
- w_publben ↔ w_ben_gk

## RETRIEVE_FGS

- Type: SELECT
- Tables: w_hgfg_mosp, w_bildtaf, w_hgfg, w_ben_gk, w_btzeilenugb_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = '01' and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_bildtaf, w_hgfg, w_ben_gk, w_btzeilenugb_verbauung where bildtaf_bteart = 'U' and bildtaf_hg = '01' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_btnr = btzeilenuv_btnr and btzeilenuv_marke_tps = '&MARKE&' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and hgfg_bereich = 'KONZERN' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2
```

## RETRIEVE_SPRACHEN

- Type: SELECT
- Tables: w_publben, w_ben_gk
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select substr(publben_bezeichnung, 1, 2) SpracheISO, substr(publben_bezeichnung, 3, 2) SpracheRegISO, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'T' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Benennung
```

## LOAD_TECHNISCHE_LITERATUR

- Type: SELECT
- Tables: w_bildtaf, w_btzeilen, w_teil, w_ben_gk, w_btzeilen_verbauung, w_btzeilenugb, w_btzeilenugb_verbauung
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, teil_ist_eba istEBA, benennung.ben_text Benennung, kommentar.ben_text Kommentar, teil_benennzus Zusatz, btzeilen_eins Einsatz, btzeilen_auslf Auslauf, teil_mam MAM,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_bildtaf inner join w_btzeilen on (bildtaf_btnr = btzeilen_btnr and btzeilen_bildposnr <> '--') inner join w_teil on (btzeilen_sachnr = teil_sachnr) inner join w_ben_gk benennung on (teil_textcode = benennung.ben_textcode and benennung.ben_iso = '&ISO&' and benennung.ben_regiso = '&REGISO&') left join w_ben_gk kommentar on (teil_textcode_kom = kommentar.ben_textcode and kommentar.ben_iso = '&ISO&' and kommentar.ben_regiso = '&REGISO&') inner join w_btzeilen_verbauung on (btzeilen_btnr = btzeilenv_btnr and  btzeilen_pos = btzeilenv_pos and btzeilenv_mospid = &MOSP&) where bildtaf_hg = '01' and bildtaf_fg = '&FG&' and bildtaf_bteart = 'G' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') &TLSPRACHE_STMT_GEB& union select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, teil_ist_eba istEBA, benennung.ben_text Benennung, kommentar.ben_text Kommentar, teil_benennzus Zusatz, btzeilenu_eins Einsatz, btzeilenu_ausl Auslauf, teil_mam MAM,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_bildtaf inner join w_btzeilenugb on (bildtaf_btnr = btzeilenu_btnr and btzeilenu_bildposnr <> '--') inner join w_teil on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk benennung on (teil_textcode = benennung.ben_textcode and benennung.ben_iso = '&ISO&' and benennung.ben_regiso = '&REGISO&') left join w_ben_gk kommentar on (teil_textcode_kom = kommentar.ben_textcode and kommentar.ben_iso = '&ISO&' and kommentar.ben_regiso = '&REGISO&') inner join w_btzeilenugb_verbauung on (btzeilenu_btnr = btzeilenuv_btnr and  btzeilenu_pos = btzeilenuv_pos and btzeilenuv_marke_tps = '&MARKE&') where bildtaf_hg = '01' and bildtaf_fg = '&FG&' and bildtaf_bteart = 'U' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') &TLSPRACHE_STMT_UGB& order by Benennung asc, Kommentar asc, Einsatz desc
```

## TL_SPRACHE_STMT_GEB

- Type: SELECT
- Tables: w_tl_sprache_bnb
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
and btzeilen_bildposnr = (select tlsb_bildnummer from w_tl_sprache_bnb where tlsb_iso = '&ISO_TL&' and tlsb_regiso = '&REGISO_TL&')
```

## TL_SPRACHE_STMT_UGB

- Type: SELECT
- Tables: w_tl_sprache_bnb
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
and btzeilenu_bildposnr = (select tlsb_bildnummer from w_tl_sprache_bnb where tlsb_iso = '&ISO_TL&' and tlsb_regiso = '&REGISO_TL&')
```
