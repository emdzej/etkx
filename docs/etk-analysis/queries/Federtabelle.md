# Federtabelle SQL Queries

Total queries: 6

## Table Relationships (Co-occurrence)

- w_sft_aspg ↔ w_teil
- w_sftfeder ↔ w_teil
- w_sfttyp ↔ w_ben_gk ↔ w_bed_sala ↔ w_bed ↔ w_sftsala ↔ w_sft
- w_sfttyp ↔ w_sft
- w_sfttyp ↔ w_sftsala ↔ w_sft

## LOAD_SALAS
**Description:** Retrieves data from w_sfttyp, w_ben_gk, w_bed_sala, w_bed, w_sftsala, w_sft filtered by sfttyp_typ, sfttyp_sftid, sft_sftid, sftsala_salaid, bedsala_id, bed_textcode, ben_iso, ben_regiso and ordered by Code, Benennung. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sfttyp, w_ben_gk, w_bed_sala, w_bed, w_sftsala, w_sft
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct sftsala_salaid Id, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung from w_sfttyp, w_ben_gk, w_bed_sala, w_bed, w_sftsala, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& and sft_sftid = sftsala_sftid and sftsala_salaid = bedsala_id and bedsala_id = bed_elemid and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Code, Benennung
```

## LOAD_PUNKTE_TYP
**Description:** Retrieves data from w_sfttyp, w_sft filtered by sfttyp_typ, sfttyp_sftid and ordered by FTId, Typ. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sfttyp, w_sft
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct sfttyp_sftid FTId, sfttyp_typ Typ, sfttyp_punkte_va_l GrundpunkteVA_links, sfttyp_punkte_va_r GrundpunkteVA_rechts, sfttyp_punkte_ha_l GrundpunkteHA_links, sfttyp_punkte_ha_r GrundpunkteHA_rechts, sftid_ist_aspg Sftid_Ist_Aspg from w_sfttyp, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& order by FTId, Typ
```

## LOAD_PUNKTE_SALA
**Description:** Retrieves data from w_sfttyp, w_sftsala, w_sft filtered by sfttyp_typ, sfttyp_sftid, sft_sftid, sftsala_salaid and ordered by FTId, SalaId. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sfttyp, w_sftsala, w_sft
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: yes

```sql
select distinct sftsala_sftid FTId, sftsala_salaid SalaId, sftsala_va_punkte_l PunkteVA_links, sftsala_va_punkte_r PunkteVA_rechts, sftsala_ha_punkte_l PunkteHA_links, sftsala_ha_punkte_r PunkteHA_rechts from w_sfttyp, w_sftsala, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& and sft_sftid = sftsala_sftid and sftsala_salaid in (&SALAIDS&) order by FTId, SalaId
```

## LOAD_FEDERN_LINKS
**Description:** Retrieves data from w_sftfeder, w_teil filtered by sftfeder_sftid, sftfeder_kz_vh, sftfeder_punkte_von, sftfeder_punkte_bis, teil_sachnr. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sftfeder, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_l Teilenummer   ,          teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_sftfeder, w_teil where sftfeder_sftid = &FTID& and sftfeder_kz_vh = '&ART&' and sftfeder_punkte_von <= &PUNKTE& and sftfeder_punkte_bis >= &PUNKTE& and teil_sachnr = sftfeder_sachnr_l
```

## LOAD_FEDERN_RECHTS
**Description:** Retrieves data from w_sftfeder, w_teil filtered by sftfeder_sftid, sftfeder_kz_vh, sftfeder_punkte_von, sftfeder_punkte_bis, teil_sachnr. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sftfeder, w_teil
- Parameterized: yes
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_r Teilenummer   ,          teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_sftfeder, w_teil where sftfeder_sftid = &FTID& and sftfeder_kz_vh = '&ART&' and sftfeder_punkte_von <= &PUNKTE& and sftfeder_punkte_bis >= &PUNKTE& and teil_sachnr = sftfeder_sachnr_r
```

## LOAD_ASPG_KIT
**Description:** Retrieves data from w_sft_aspg, w_teil filtered by sftaspg_sftid, teil_sachnr. Used in the Federtabelle module to support ETK workflows for spring table.


- Type: SELECT
- Tables: w_sft_aspg, w_teil
- Parameterized: yes
- Hardcoded literals: no
- Joins: yes | Filters: yes | Sorting: no

```sql
select teil_hauptgr || teil_untergrup || sftaspg_sachnr_pg Teilenummer, sftaspg_btnr BTNr, sftaspg_mospid MospId, sftaspg_achse Achse, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant from w_sft_aspg, w_teil where sftaspg_sftid = &FTID& and teil_sachnr = sftaspg_sachnr_pg
```
