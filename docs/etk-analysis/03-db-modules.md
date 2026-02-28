# ETK DB Modules Catalog (decompiled)

Source: `/Users/emdzej/Documents/etk/decompiled/webetk/db/**/dbaccess.java`

This document catalogs **44 DB modules** found in the decompiled WebETK Java codebase. Each module exposes DB access methods grouped by functionality. The descriptions below are inferred from method names and SQL usage.

> **Legend (domain terms)**
> - **HG/FG/UGB/BTE**: ETK grouping hierarchy (Hauptgruppe/Funktionsgruppe/Untergruppe/Bildtafel)
> - **SALA**: vehicle equipment code
> - **BNB**: accessory/option bundle
> - **MospId**: model-specific identifier

---

## Functional groups (overview)

- **Administration & configuration**: `admintool`, `konfiguration`, `settings`
- **Vehicle identification & scope**: `fzgid`, `fzgumfang`
- **Parts catalog / BTE display**: `bteanzeige`, `bteinfo`, `bedauswertung`, `etktext`, `valueline`, `visualisierungteil`, `teileinfo`
- **Parts search**: `teilesucheallgemein`, `teilesucheass`, `teilesuchefzg`, `teilesuchespezifischvaluelinefzg`
- **Parts usage / replacement**: `teilevwdgben`, `teilevwdgfzg`, `teilevwdgred`, `teilevwdgteil`, `teileersetzung`
- **Lists & ordering**: `teileliste`
- **Notes/news/help/info**: `notizuebersicht`, `newstexte`, `hilfe`, `infotool`, `misc`
- **Special catalogs**: `normteile`, `satzeinzelteile`, `polstercode`, `federtabelle`, `fuellmengen`, `technischeliteratur`, `erstbevorratung`, `lagerzeit`, `interpretation`, `aspg`
- **Accessories (Zubehör)**: `zub`, `zub/marketing`, `zub/preise`, `zub/suche`, `zub/technisch`, `zub/verwaltung`

---

## Module catalog

### admintool
- **Purpose:** Admin/maintenance tasks (prices, table locks, cleanup of user tables/sequences).
- **Key methods:** `isFirmaIdTheOnlyOneInPreise`, `lockTable`, `unlockTable`, `loeschePreise`, `erzeugePreis`, `loescheNutzertabellen`, `loescheNutzersequenzen`.
- **Entities:** Firma (company), Preise (prices), DB tables/sequences.

### aspg
- **Purpose:** ASPG catalog (parts and connectors).
- **Key methods:** `loadAspgTeile`, `loadStecker`.
- **Entities:** ASPG parts, connectors (Stecker).

### bedauswertung
- **Purpose:** BTE evaluation with changepoints and conditions (SALA/AFL/text).
- **Key methods:** `loadBTE`, `loadBTEChangepoints`, `loadBTEBedingungenSala`, `loadBTEBedingungenAFL`, `loadBTEBedingungenText`.
- **Entities:** BTE, changepoints, conditions.

### bteanzeige
- **Purpose:** BTE display data (hotspots, lines, comments, conditions, references).
- **Key methods:** `loadHotspots`, `loadZeilenFzg`, `loadZeilenUgb`, `loadKommentareFzg`, `loadKommentareUgb`, `loadBedingungenFzg`, `loadBTVerweiseFzg`, `loadBTVerweiseUgb`, `loadJaNein`, `loadAZeichen`, `ermittleTCKezBzglProddatum`.
- **Entities:** BTE, hotspots, lines, comments, conditions, cross-references.

### bteinfo
- **Purpose:** BTE metadata and comments.
- **Key methods:** `loadBTEInfo`, `loadBTEKommentar`.
- **Entities:** BTE info/comment.

### erstbevorratung
- **Purpose:** Initial stocking / first supply view by groups and parts.
- **Key methods:** `selectHGs`, `findHG`, `selectTeile`.
- **Entities:** HG, parts.

### etktext
- **Purpose:** ETK text resources (general texts and comment texts).
- **Key methods:** `loadETKTexte`, `loadETKTexteZuKommentaren`.
- **Entities:** ETK text, comments.

### federtabelle
- **Purpose:** Spring table lookup (SALA/points/types/kit).
- **Key methods:** `loadSalas`, `loadSalaPunkte`, `loadTypPunkte`, `loadFeder`, `loadAspgKit`.
- **Entities:** SALA, spring types/points, kits.

### fuellmengen
- **Purpose:** Fluid fill quantities.
- **Key methods:** `retrieveFuellmengen`, `concatCollection`.
- **Entities:** fill quantities, fluid types.

### fzgid
- **Purpose:** Vehicle identification by VIN/type and model attributes.
- **Key methods:** `selectBauarten`, `selectBaureihen`, `selectKarosserien`, `selectModelle`, `selectRegionen`, `selectLenkungen`, `selectGetriebe`, `selectBaujahre`, `selectZulassungsmonate`, `selectModellspalte`, `selectFzgIdDatenByFgstnr`, `selectFzgIdDatenByTyp`, `selectSalasZuFahrgestellnummer`, `getBedingungenZuCode`.
- **Entities:** Vehicle, VIN (Fgstnr), Bauart/Baureihe/Modell, Markt/Region, steering/transmission, SALA codes.

### fzgumfang
- **Purpose:** Vehicle scope/filters (market, steering, model hierarchy).
- **Key methods:** `selectRegionen`, `selectLenkungen`, `selectBauarten`, `selectBaureihen`, `selectKarosserien`, `selectModelle`, `selectModellspalten`.
- **Entities:** Vehicle scope filters.

### hilfe
- **Purpose:** Help/glossary resources and version info.
- **Key methods:** `retrieveVersionInfo`, `loadAbkuerzungen`, `loadBedeutungen`, `loadSalapas`.
- **Entities:** Abbreviations, meanings, SALA glossary.

### infotool
- **Purpose:** Tips, tricks, ticker text and read status.
- **Key methods:** `saveGelesen`, `loadTippsTricks`, `existTippsTricks`, `existTickertext`.
- **Entities:** Tips/tricks, ticker messages.

### interpretation
- **Purpose:** Interpretation/explanation for part numbers (brand mapping, entry points).
- **Key methods:** `getMarkenForSachnummer`, `loadInterpretationEinstieg`, `loadInterpretation`.
- **Entities:** Sachnummer, brand/marke.

### konfiguration
- **Purpose:** Multi-tenant configuration (firms, branches, users, permissions, languages, table config, login info, sender/recipient lists).
- **Key methods:** `loadFirmen`, `loadFilialen`, `checkUser`, `loadKonfiguration`, `saveKonfiguration`, `loadNutzer*`, `loadBerechtigungen`, `loadFunktionsrechte`, `storeNutzer`, `storeNutzerBerechtigungen`, `storeNutzerFunktionsrechte`, `changeDefaultFiliale`, `getLoggedInUsers`, `loadTableConfiguration`, `insertTableConfiguration`, `loadAbsenderUndEmpfaenger`.
- **Entities:** Firma/Filiale/Nutzer, permissions, languages, table configs.

### lagerzeit
- **Purpose:** Storage time / inventory-based part lookup by groups.
- **Key methods:** `selectHGs`, `findHG`, `selectTeile`.
- **Entities:** HG, parts.

### misc
- **Purpose:** Miscellaneous utilities (SOWU answers).
- **Key methods:** `loadSOWUAntwort`, `deleteSOWUAntwort`.
- **Entities:** SOWU/Q&A responses.

### newstexte
- **Purpose:** News texts with image handling.
- **Key methods:** `saveNewstext`, `loadNewstexte`, `deleteNewstext`, `updateNewstexte`, `saveImage`, `loadImage`.
- **Entities:** News, images.

### normteile
- **Purpose:** Standard parts catalog with graphics metadata.
- **Key methods:** `loadNormteileBenennungen`, `loadGrafikInfos`, `loadNormteile`.
- **Entities:** Standard parts, graphics.

### notizuebersicht
- **Purpose:** Notes overview (counts, list, min HG).
- **Key methods:** `selectAnzahlNotizen`, `selectNotizen`, `selectMinHG`.
- **Entities:** Notes, HG.

### polstercode
- **Purpose:** Upholstery code lookup.
- **Key methods:** `loadPolstercode`.
- **Entities:** Upholstery codes.

### satzeinzelteile
- **Purpose:** Sets and their single parts.
- **Key methods:** `loadHGs`, `loadSaetze`, `hatEinzelteile`, `loadEinzelteile`.
- **Entities:** Sets, single parts, HG.

### settings
- **Purpose:** User/app settings, markets, languages, rights.
- **Key methods:** `loadSettings`, `loadMarktId`, `loadIpacMarkt`, `updateMarktId`, `loadMaerkteEtkLokaleProdukte`, `loadEtkMarkt`, `loadUserWeitereMaerkte`, `loadUserRegionen`, `saveSettings`, `checkModellspalte`, `loadSprachen`, `loadKatalogausfuehrungen`, `loadUserRechte`, `loadUserBerechtigungen`, `deleteTeilenotizen`.
- **Entities:** User settings, market/region, language, permissions.

### technischeliteratur
- **Purpose:** Technical literature lookup by FG and language.
- **Key methods:** `selectFGs`, `selectSprachen`, `loadTechnischeLiteratur`.
- **Entities:** Technical literature, FG, languages.

### teileersetzung
- **Purpose:** Part replacement lists by group.
- **Key methods:** `selectHGs`, `findHG`, `selectTeile`.
- **Entities:** Parts replacements, HG.

### teileinfo
- **Purpose:** Part detail info (notes, service/product/REACH info, price loaded status).
- **Key methods:** `loadTeileinfo`, `loadNotiz`, `loadNotizenOthers`, `loadServiceinfo`, `loadProduktinfo`, `loadReachinfo`, `loadTeileclearing`, `saveNotiz`, `deleteNotiz`, `checkPreiseGeladen`, `loadTeilenummernErgebnisliste`.
- **Entities:** Part details, notes, service/product info.

### teileliste
- **Purpose:** Parts lists, orders, scoring lists, SAP integration, history tracking.
- **Key methods:** `storeTeileliste`, `insertJob`, `insertSrp`, `loadJobs`, `loadSrps`, `insertTeilInListe`, `updateTeilInListe`, `loadTeilelistenUebersicht`, `loadTeilelistePositionen`, `loadBestelllistePositionen`, `loadPreisInfo`, `writeSapDaten`, `loadSapAntwort`, `writeHistorie`, `getNextTeilelistenId`, `copyTeileliste`, `renameRrSapListe`.
- **Entities:** Parts list, order/bestellliste, jobs/SRP, SAP data, history.

### teilesucheallgemein
- **Purpose:** General parts search support (market name lookup).
- **Key methods:** `selectMarktBenennung`.
- **Entities:** Market.

### teilesucheass
- **Purpose:** Assisted parts search by name/term/number with HG/FG filters.
- **Key methods:** `selectHGFGs`, `selectHGs`, `selectFGs`, `searchBildtafel_*`, `searchSachnummern_*`, `checkBildtafel_HG_Grafisch`, `searchBildtafel_HG_Grafisch`.
- **Entities:** HG/FG, BTE, part numbers.

### teilesuchefzg
- **Purpose:** Vehicle-based parts search (with changepoints).
- **Key methods:** `selectHGs`, `selectHGFGs`, `selectFGs`, `getFGGrafMosp`, `getFGGraf`, `getChangepointsKB`, `searchBildtafel_*`, `searchSachnummer_*`.
- **Entities:** Vehicle, HG/FG, BTE, part numbers.

### teilesuchespezifischvaluelinefzg
- **Purpose:** Vehicle-specific ValueLine BTE search.
- **Key methods:** `selectBildtafelnValueline`.
- **Entities:** ValueLine BTE.

### teilevwdgben
- **Purpose:** Part usage by designation/description.
- **Key methods:** `loadBaureihen`, `loadBaureihenTNR`, `loadModellspalten`, `loadModellspaltenTNR`, `searchSachnummern_TVBenennung`, `searchBildtafel_SNrs`.
- **Entities:** Baureihen, model columns, part numbers.

### teilevwdgfzg
- **Purpose:** Part usage by vehicle selection.
- **Key methods:** `selectHGs`, `findHG`, `selectTeile`, `testTeilOnlyIn`.
- **Entities:** Vehicle usage, HG, parts.

### teilevwdgred
- **Purpose:** Part usage reduction (select usage list and single part).
- **Key methods:** `selectVerwendungen`, `selectTeil`.
- **Entities:** Usage list, part.

### teilevwdgteil
- **Purpose:** Part usage by part number (load part, model series/columns).
- **Key methods:** `loadTeil`, `loadBaureihen`, `loadModellspalten`.
- **Entities:** Part, Baureihen, model columns.

### valueline
- **Purpose:** ValueLine catalog (HGs, sets, BTE, part numbers).
- **Key methods:** `loadHGs`, `loadSaetze`, `loadBteBaureihen`, `loadBildtafeln`, `loadTeilenummern`.
- **Entities:** ValueLine BTE, sets, part numbers.

### visualisierungteil
- **Purpose:** Part visualization metadata (graphics info for UGB/GEB).
- **Key methods:** `retrieveVisualisierungsInfo`, `retrieveVisualisierungsInfoGeb`, `retrieveVisualisierungsInfoUgb`.
- **Entities:** Part visualization/graphics.

### zub
- **Purpose:** Generic accessory text lookup by ID.
- **Key methods:** `ermittleTextNachId`.
- **Entities:** Accessory texts.

### zub/marketing
- **Purpose:** Accessory marketing info (product names, graphics, options, variants).
- **Key methods:** `ermittleMarketingProduktZuBte`, `getMarketingInfos`, `setzeMarketingTexte`, `ermittleProduktnameFallback`, `ermittleGrafikenZuProdukt`, `ermittleVarianten_*`, `ermittleOptionaleProdukte`, `ermittleOptionaleMarketingInfos`, `ermittleBTEArt`, `ermittleBnbsZuBte`.
- **Entities:** Accessory products, marketing texts, graphics, variants.

### zub/preise
- **Purpose:** Accessory pricing and installation info.
- **Key methods:** `ladeTeileUndPreiseZuBte`, `ladeHgUg`, `ladePreiseZuTeilen`, `ladeEinbauinfos`, `getAufwand`.
- **Entities:** Accessory prices, installation info, parts.

### zub/suche
- **Purpose:** Accessory search (categories, product lists, graphics, vehicle selection, free-text search).
- **Key methods:** `ladeHauptKategorien`, `ladeUnterkategorien`, `ermittleBtesZuUnterkategorie`, `ermittleProduktListeZuBtNrn`, `ermittleGrafikenZuProdukt*`, `hole*Auswahl`, `holeFahrzeugDetailsAusVin`, `executeHitlistSearch*`, `verarbeiteFreitextsuche`, `sucheProdukteEtk`.
- **Entities:** Accessory categories, products, BTE, vehicle selection, hitlist search.

### zub/technisch
- **Purpose:** Accessory technical data (installation info, REFA work, conditions, variants).
- **Key methods:** `getTechnischesProduktFzg`, `getTechnischesProduktUgb`, `ermittleRefaArbeitBte/Bnb`, `ermittleExplosionsGrafik`, `loadZubBildtafelStammdaten`, `loadZubBildtafelBedingungen*`, `getEinbauInfo*`, `ermittleVerbauteVarianten`, `ermittleVerbauteAlternativen`, `isAktuell`.
- **Entities:** Accessory technical info, REFA work, installation info, variants.

### zub/verwaltung
- **Purpose:** Accessory sales management (requests, offers, configurations, customers, users).
- **Key methods:** `ladeAnfrage`, `ladeVorgang`, `ladeKonfiguration`, `speichereAngebot`, `speichereKonfiguration`, `speichereVorgang`, `speichereAnfrage`, `speichereKunde`, `speichereKundenfahrzeug`, `sucheAnfragen`, `sucheAngebote`, `storniereAngebot`, `setzeAngebotsStatus`, `hatUserAgbBestaetigt`, `setUserAgbBestaetigt`.
- **Entities:** Anfrage/Vorgang/Angebot (request/process/offer), configurations, customers, users, pricing/tax.

---

## Notes / next steps
- This catalog is based on **method and module names** in decompiled Java classes.
- If needed, map each module to **specific Transbase tables** using `docs/SCHEMA.md` and `src/decompiled/SQLStatementsTransbase.java` (future work).
