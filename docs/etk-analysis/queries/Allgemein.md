# Allgemein SQL Queries

Total queries: 6

## LOAD_GRAFIK

- Type: SELECT
- Tables: w_grafik
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select grafik_blob Grafik, grafik_format Format, grafik_moddate ModStamp from w_grafik where grafik_grafikid = &GRAFIKID& and grafik_art = '&ART&'
```

## RETRIEVE_BMWSACHNUMMER_FOR_FREMDESACHNUMMER

- Type: SELECT
- Tables: w_fremdtl
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select distinct fremdtl_sachnr Sachnummer from w_fremdtl where fremdtl_fremdsnr = '&SACHNUMMER_FREMD&'
```

## RETRIEVE_URLS

- Type: SELECT
- Tables: w_url
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select url_url URL from w_url where upper(url_type)='&TYPE&' and upper(url_iso)='&ISO&' and upper(url_regiso)='&REGISO&' and upper(url_marke_tps)='&MARKE&'
```

## RETRIEVE_BMW_NETZ

- Type: SELECT
- Tables: w_netz
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select netz_netz Netz, netz_krit Krit from w_netz
```

## RETRIEVE_BMW_NETZURL

- Type: SELECT
- Tables: w_netzurl
- Parameterized: yes
- Hardcoded literals: yes
- Joins: no | Filters: yes | Sorting: no

```sql
select netzurl_url_asap AsapUrl, netzurl_asaptunnel AsapTunnel, netzurl_url_zr CentralURL, netzurl_url_dom_basic IGDOMBasicsUrl, netzurl_url_dom_options IGDOMOptionsUrl from w_netzurl where netzurl_netz = '&NETZ&' and netzurl_krit = '&KRIT&'
```

## RETRIEVE_BMW_PROXY

- Type: SELECT
- Tables: w_proxy
- Parameterized: no
- Hardcoded literals: no
- Joins: no | Filters: no | Sorting: no

```sql
select proxy_proxyname ProxyName, proxy_port Port, proxy_nutzername UserName, proxy_passwort Passwort, proxy_realm Realm, proxy_ntdomain NtHost, proxy_nthost NtDomain from w_proxy
```
