# admintool SQL Queries

Total queries: 11

## Table Relationships (Co-occurrence)

- w_ben_gk ↔ w_publben

## LOAD_FIRMEN
**Description:** Retrieves data from w_firma and ordered by firma_bezeichnung. Used in the admintool module to support ETK workflows for company/branch settings.


- Type: SELECT
- Tables: w_firma
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: yes

```sql
select firma_id Id from w_firma order by firma_bezeichnung
```

## DELETE_PRICES
**Description:** Deletes records from w_preise. Used in the admintool module to support ETK workflows for pricing.


- Type: DELETE
- Tables: w_preise
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
delete from w_preise
```

## DELETE_PRICES_BY_FIRMA
**Description:** Deletes records from w_preise filtered by preise_firma. Used in the admintool module to support ETK workflows for pricing.


- Type: DELETE
- Tables: w_preise
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
delete from w_preise where preise_firma = '&FIRMA&'
```

## ERMITTLE_NUTZERTABELLEN
**Description:** Retrieves data from systable filtered by NOT. Used in the admintool module to support ETK workflows for systable data.


- Type: SELECT
- Tables: systable
- Tables not in schema: systable
- Parameterized: no
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
SELECT tname tabelle FROM systable WHERE tname NOT LIKE 'sys%' AND tname NOT LIKE '%id_seq%'
```

## ERMITTLE_NUTZERSEQUENZEN
**Description:** Retrieves data from systable filtered by NOT, tname. Used in the admintool module to support ETK workflows for systable data.


- Type: SELECT
- Tables: systable
- Tables not in schema: systable
- Parameterized: no
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
SELECT tname sequenz FROM systable WHERE tname NOT LIKE 'sys%' AND tname LIKE '%id_seq%'
```

## GET_COUNT_FIRMA_PREISE
**Description:** Retrieves data from w_preise filtered by preise_firma. Used in the admintool module to support ETK workflows for pricing.


- Type: SELECT
- Tables: w_preise
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select count(preise_firma) cnt from w_preise where preise_firma = '&FIRMA&'
```

## GET_DISTINCT_FIRMA_PREISE
**Description:** Retrieves data from w_preise. Used in the admintool module to support ETK workflows for pricing.


- Type: SELECT
- Tables: w_preise
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select distinct preise_firma FirmaId from w_preise
```

## INSERT_PRICES
**Description:** Inserts records in w_preise. Used in the admintool module to support ETK workflows for pricing.


- Type: INSERT
- Tables: w_preise
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
INSERT INTO w_preise (preise_firma,preise_sachnr,preise_evpreis,preise_nachbelastung,preise_rabattschluessel,preise_preisaenderung,preise_preis_kz,preise_sonderpreis,preise_sonderpreis_kz,preise_mwst,preise_mwst_code,preise_zolltarifnr,preise_nettopreis) VALUES (&preise_firma&,&preise_sachnr&,&preise_evpreis&,&preise_nachbelastung&,&preise_rabattschluessel&,&preise_preisaenderung&,&preise_preis_kz&,&preise_sonderpreis&,&preise_sonderpreis_kz&,&preise_mwst&,&preise_mwst_code&,&preise_zolltarifnr&,&preise_nettopreis&)
```

## UPDATE_PRICES
**Description:** Updates records in w_preise filtered by preise_firma, preise_sachnr. Used in the admintool module to support ETK workflows for pricing.


- Type: UPDATE
- Tables: w_preise
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: yes | Sorting: no

```sql
UPDATE w_preise SET preise_preis_kz = &preise_preis_kz&,preise_evpreis = &preise_evpreis&,preise_nachbelastung = &preise_nachbelastung&,preise_rabattschluessel = &preise_rabattschluessel&,preise_preisaenderung = &preise_preisaenderung&,preise_sonderpreis = &preise_sonderpreis&,preise_sonderpreis_kz = &preise_sonderpreis_kz&,preise_mwst = &preise_mwst&,preise_mwst_code = &preise_mwst_code&,preise_zolltarifnr = &preise_zolltarifnr&,preise_nettopreis = &preise_nettopreis& WHERE preise_firma = &preise_firma& and preise_sachnr = &preise_sachnr&
```

## LOAD_SPRACHEN
**Description:** Retrieves data from w_ben_gk, w_publben filtered by publben_art, ben_textcode, ben_iso, ben_regiso. Used in the admintool module to support ETK workflows for text/designations.


- Type: SELECT
- Tables: w_ben_gk, w_publben
- Parameterized: no
- Hardcoded literals: yes
- Joins: yes | Filters: yes | Sorting: no

```sql
select ben_iso ISO, ben_regiso RegISO, ben_text Benennung from w_ben_gk@etk_publ, w_publben@etk_publ where publben_art = 'S' and ben_textcode = publben_textcode and ben_iso = substr(publben_bezeichnung, 1, 2)  and ben_regiso = substr(publben_bezeichnung, 3, 2)
```

## LOAD_DBVERSIONSINFO
**Description:** Retrieves data from w_verwaltung. Used in the admintool module to support ETK workflows for verwaltung data.


- Type: SELECT
- Tables: w_verwaltung
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select verwaltung_info Info, verwaltung_wert Wert from w_verwaltung
```
