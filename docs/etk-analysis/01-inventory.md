# ETK Backend Component Inventory (decompiled WebETK)

Source: `/Users/emdzej/Documents/etk/decompiled/webetk/`

## 1) Directory structure (high level)

```
webetk/
  BooleanWrapper.java
  ConnectionPool.java
  DMSException.java
  DatabaseBusyException.java
  DuplicateValueException.java
  EmptyListException.java
  IntegerWrapper.java
  InvalidDataException.java
  InvalidInitializationException.java
  InvalidSessionException.java
  LanguageUtils.java
  ListLockedException.java
  NoBrandAuthorizationException.java
  NoDataFoundException.java
  NoHGFoundException.java
  ObjectLockedException.java
  SapException.java
  ServerConstants.java
  StringUtils.java
  ThreadSafeSimpleDateFormat.java
  UserHandlingException.java
  WebetkUtils.java
  app/                       # application/domain layer (business logic)
  communication/             # serialization / transport DTOs
  db/                        # database access modules (Transbase)
  framework/                 # server-side framework/session components
  interfaces/                # external interfaces (SWS)
  javaclient/                # Swing client UI & workflow
  resources/                 # localized/static resources
  xmlapi/                    # XML API constants/exceptions
```

## 2) Java packages & purpose

> Package names derived from `package` declarations in decompiled sources.

### Core / infrastructure
- **webetk** – core utilities, common exceptions, constants, connection pooling, helpers.
- **webetk.framework** – server-side session/user/BTE management (`ServerSessionInfo`, `ServerUserInfo`, `ServerBTEManager`).
- **webetk.communication** – transport/serialization layer (protocol objects, helpers).
- **webetk.communication.transferables** – DTOs sent over client/server communication.
- **webetk.communication.utils** – helper utilities for transport layer.
- **webetk.interfaces.sws** – SWS integration (parts/manager, JAXB-like factory).
- **webetk.resources** – resource bundles/static content.
- **webetk.xmlapi** – XML API constants/exceptions.

### Application layer (server-side)
- **webetk.app** – domain objects, search logic, global server state.
- **webetk.app.admintool** – admin tool server logic.
- **webetk.app.aspg** – AS/PG module logic (naming from sources).
- **webetk.app.asssuche** – ASS search module logic.
- **webetk.app.ausstattung** – equipment/options logic.
- **webetk.app.basesuche** – base/general search logic.
- **webetk.app.bteanzeige** – BTE (graphic group) display logic.
- **webetk.app.bteinfo** – BTE information logic.
- **webetk.app.cache** – caching layer.
- **webetk.app.changepoints** – change points / modification logic.
- **webetk.app.clientadmintool** – client admin functions.
- **webetk.app.erstbevorratung** – initial stocking module.
- **webetk.app.etktext** – ETK text handling.
- **webetk.app.federtabelle** – spring table module.
- **webetk.app.fuellmengen** – fluid fill quantities.
- **webetk.app.fzgid** – vehicle identification.
- **webetk.app.fzgsuche** – vehicle search.
- **webetk.app.fzgsuchespezifischvalueline** – vehicle search (specific/value line).
- **webetk.app.fzgumfang** – vehicle scope/coverage.
- **webetk.app.hilfe** – help/assistance content.
- **webetk.app.infotool** – infotool module.
- **webetk.app.interpretation** – interpretation module.
- **webetk.app.konfiguration** – configuration handling.
- **webetk.app.lagerzeit** – storage time/shelf‑life constraints.
- **webetk.app.newstexte** – news text module.
- **webetk.app.normteile** – standard parts.
- **webetk.app.notizuebersicht** – notes overview.
- **webetk.app.polstercode** – upholstery code module.
- **webetk.app.satzeinzelteile** – set/single parts module.
- **webetk.app.settings** – settings.
- **webetk.app.technischeliteratur** – technical literature.
- **webetk.app.teileersetzung** – part replacement.
- **webetk.app.teileinfo** – part information.
- **webetk.app.teileliste** – parts list (score lists, ordering, etc.).
- **webetk.app.teilevwdgben** – parts usage by designation (Benennung).
- **webetk.app.teilevwdgfzg** – parts usage by vehicle.
- **webetk.app.teilevwdgred** – parts usage (reduced view).
- **webetk.app.teilevwdgteil** – parts usage by part.
- **webetk.app.valueline** – Value Line module.
- **webetk.app.visualisierungteil** – part visualization.
- **webetk.app.zub** – accessories (Zubehör) module.
  - **webetk.app.zub.common** – common accessories logic.
  - **webetk.app.zub.marketing** – marketing accessories.
  - **webetk.app.zub.suche** – accessories search.
  - **webetk.app.zub.technisch** – technical accessories.
  - **webetk.app.zub.verwaltung** – accessories administration.

### Database access layer (Transbase)
- **webetk.db** – DB access base & helpers (e.g., `dbaccess`).
- **webetk.db.* (44 modules)** – see full list in section 4.

### Java client (Swing)
- **webetk.javaclient** – client launcher, common client utilities.
- **webetk.javaclient.communication.asap** – ASAP communication.
- **webetk.javaclient.framework** – client framework core.
- **webetk.javaclient.generictable** – generic table widgets.
- **webetk.javaclient.infotool** – infotool client.
- **webetk.javaclient.lookandfeel** – LAF customization.
- **webetk.javaclient.menuitem** – menu items/actions.
- **webetk.javaclient.score** – score list UI.
- **webetk.javaclient.utils** – client helpers.
- **webetk.javaclient.workflow** – client workflow/state machines.
- **webetk.javaclient.dialog.* (many)** – dialogs for BTE, vehicle search, admin, extras, help, etc. (see package list from source).

## 3) Servlet classes

No servlet classes were found in the decompiled tree:
- No `*Servlet` classes
- No classes extending `HttpServlet`
- No `doGet`/`doPost` methods

`ServerGlobalObjects` references `ServletContext`, suggesting WebETK runs under an embedded servlet container, but servlet endpoints are not present in this decompiled set.

## 4) Database modules (44) with brief descriptions

> Derived from `webetk.db.*` packages.

1. **webetk.db** – DB access base, common helper (`dbaccess`).
2. **webetk.db.admintool** – admin tool DB queries.
3. **webetk.db.aspg** – AS/PG DB queries.
4. **webetk.db.bedauswertung** – condition evaluation DB queries.
5. **webetk.db.bteanzeige** – BTE display DB queries.
6. **webetk.db.bteinfo** – BTE info DB queries.
7. **webetk.db.erstbevorratung** – initial stocking DB queries.
8. **webetk.db.etktext** – ETK text DB queries.
9. **webetk.db.federtabelle** – spring table DB queries.
10. **webetk.db.fuellmengen** – fluid fill quantities DB queries.
11. **webetk.db.fzgid** – vehicle identification DB queries.
12. **webetk.db.fzgumfang** – vehicle scope/coverage DB queries.
13. **webetk.db.hilfe** – help module DB queries.
14. **webetk.db.infotool** – infotool DB queries.
15. **webetk.db.interpretation** – interpretation DB queries.
16. **webetk.db.konfiguration** – configuration DB queries.
17. **webetk.db.lagerzeit** – storage time/shelf‑life DB queries.
18. **webetk.db.misc** – misc DB utilities.
19. **webetk.db.newstexte** – news text DB queries.
20. **webetk.db.normteile** – standard parts DB queries.
21. **webetk.db.notizuebersicht** – notes overview DB queries.
22. **webetk.db.polstercode** – upholstery code DB queries.
23. **webetk.db.satzeinzelteile** – set/single parts DB queries.
24. **webetk.db.settings** – settings DB queries.
25. **webetk.db.technischeliteratur** – technical literature DB queries.
26. **webetk.db.teileersetzung** – part replacement DB queries.
27. **webetk.db.teileinfo** – part info DB queries.
28. **webetk.db.teileliste** – parts list DB queries.
29. **webetk.db.teilesucheallgemein** – general parts search DB queries.
30. **webetk.db.teilesucheass** – ASS parts search DB queries.
31. **webetk.db.teilesuchefzg** – vehicle-specific parts search DB queries.
32. **webetk.db.teilesuchespezifischvaluelinefzg** – specific/value‑line vehicle search DB queries.
33. **webetk.db.teilevwdgben** – parts usage by designation DB queries.
34. **webetk.db.teilevwdgfzg** – parts usage by vehicle DB queries.
35. **webetk.db.teilevwdgred** – parts usage (reduced) DB queries.
36. **webetk.db.teilevwdgteil** – parts usage by part DB queries.
37. **webetk.db.valueline** – Value Line DB queries.
38. **webetk.db.visualisierungteil** – part visualization DB queries.
39. **webetk.db.zub** – accessories DB queries.
40. **webetk.db.zub.marketing** – accessories marketing DB queries.
41. **webetk.db.zub.preise** – accessories pricing DB queries.
42. **webetk.db.zub.suche** – accessories search DB queries.
43. **webetk.db.zub.technisch** – accessories technical DB queries.
44. **webetk.db.zub.verwaltung** – accessories administration DB queries.

## 5) Key app-layer components (not exhaustive)

- **webetk.app.ServerGlobalObjects** – central singleton for configuration loading, property handling, connection pool initialization, and global state (uses `ServletContext`).
- **webetk.ConnectionPool** – connection pooling for Transbase DBs.
- **webetk.WebetkUtils / StringUtils / LanguageUtils** – common utility helpers.
- **webetk.app.SearchBTETeile** – BTE part search logic.
- **webetk.app.HGFGs / FGs / FGCollection / FGData** – hierarchy/grouping of parts (HG/FG structure).
- **webetk.app.Bedingung / Bedingungsmenge / BTEBedingungsmenge / Bedingungswert** – condition evaluation model for applicability.
- **webetk.app.Teileliste / Teil / TeilComparator** – parts list core model.
- **webetk.app.ImageCache** – image caching for part diagrams.
- **webetk.app.SucheInfo** – search metadata.
- **webetk.framework.ServerSessionInfo / ServerUserInfo** – server session/user metadata.
- **webetk.db.dbaccess** – base DB access facade used across modules.

---

Notes:
- All package/structure names are derived from decompiled sources under `webetk`.
- Servlet endpoints are not present in this decompilation set; likely located in a different module or loaded at runtime by the embedded servlet container.
