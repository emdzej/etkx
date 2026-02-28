# ClientService servlet (dispatcher)

Source: decompiled classes from `~/Documents/etk/javaclient/tomcat/webapps/javaserver/WEB-INF/classes`.

## Entry points

`ClientService` extends `BaseServlet` and handles **both** `GET` and `POST` by delegating to `doService()`.

Before doing normal request handling it checks **overruling** query commands:

- `?command=getImage` → `sendImageToHttpStream()` (streams cached image bytes)
- `?command=showVersion` → `getVersionInformation()` (simple HTML page)
- `?Test=J` → `doTest()` (XML health check)

## Request parsing / protocol

The normal RPC protocol is **not XML**. It expects a Java-serialized object:

- **Content-Type** must be exactly:
  `application/x-javaserializedobject webetk.communication.transferables.Transferable`
- The request body is read via `ObjectInputStream`.
- It expects a `webetk.communication.transferables.Transferable` subclass and calls `Transferable.read(streamIn)`.

If the MIME type mismatches, the servlet throws `IOException` (“Illegal mime type in request”).

### Response format

For standard requests, the response is **also a serialized Transferable object**:

```java
ObjectOutputStream streamOut = new ObjectOutputStream(response.getOutputStream());
Transferable.send(streamOut, trf);
```

Exceptions during command execution are stored inside the Transferable (`trf.setError(...)`) and returned to the client.

Other response formats:

- `?Test=J` → XML:
  ```xml
  <etktest>
    <timestamp>...</timestamp>
    <status_webserver>OK</status_webserver>
    <status_database>OK|Error: ...</status_database>
    <memory_jvm>...</memory_jvm>
  </etktest>
  ```
- `?command=getImage` → binary image (content-type derived from image format)
- `?command=showVersion` → HTML

## Session + authentication flow

### Session handling (BaseServlet)

- Session is stored in HTTP session attribute **`sessInfo`**.
- `getSessionInfo(request)` returns `null` if no session.
- `createNewSession(request)` creates new HttpSession, instantiates `SessionInfo`, stores it in session.

### Login / session creation

`getCurrentSession(request, trf)` logic:

- If a session exists and command is **not** login (`commandID != 2`), it is reused.
- If **no session** exists and command is not login → `InvalidSessionException`.
- Login requests **must** be `TrfHashMap` and provide:
  - `firmaid`
  - `filialid`
  - `filialid_alt` (optional, for migration)
  - `username`
  - `pwd`
  - `us_filename` (optional, for US list creation)

Steps on successful login:

1. `UserInfo.checkUser(...)` validates user credentials.
2. `createNewSession()` → new `SessionInfo`.
3. Load user info and rights:
   - `sessInfo.loadUser(...)`
   - `sessInfo.retrieveUserRechte(...)`
   - delete expired part notes
4. If `filialid != filialid_alt`:
   - move saved lists (`moveTeilelisten`)
   - update default filial (`changeDefaultFiliale`)
5. Initialize external interfaces (`initInterfaces()`):
   - DMS (XML RPC) based on schema/server URLs
   - VIN spec interface (schema URL only, server URL null in code)
   - IGDOM flags
6. `createUSListen(sessInfo, us_filename)`
7. If user has brand **Rolls-Royce**, create RR SAP list.
8. Initialize central server / proxy settings via `CommController` (timeouts also configured).
9. `prepareUserInfo(...)` writes a `LoginInfo` object into the response transferables as key `webetkSession`.

## Command routing

Routing is **reflection-based** and determined by the Transferable class name:

```java
String trfName = trf.getClass().getName();
String cmdName = "webetk.communication.command." +
                 trfName.substring(trfName.lastIndexOf('.') + 1) +
                 "Command";

TransferableCommand cmd = (TransferableCommand)Class.forName(cmdName).newInstance();
cmd.setEnvironment(sessInfo);
cmd.setTransferable(trf);
cmd.execute();
```

### Command list (server side)

All available `webetk.communication.command.*Command` classes in the server classpath:

- `TrfAdminToolCommand`
- `TrfDateiCommand`
- `TrfFahrzeugIdCommand`
- `TrfHashMapCommand`
- `TrfImageCommand`
- `TrfMiscCommand`
- `TrfStringCommand`
- `TrfTeilelisteCommand`
- `TrfTeilesucheCommand`
- `TrfZubMarketingCommand`
- `TrfZubMiscCommand`
- `TrfZubSucheCommand`
- `TrfZubTechnischCommand`
- `TrfZubVerwaltungCommand`

Supported Transferable subclasses (request payloads):

- `TrfAdminTool`
- `TrfBinary`
- `TrfDatei`
- `TrfFahrzeugId`
- `TrfHashMap`
- `TrfImage`
- `TrfMisc`
- `TrfString`
- `TrfTeileliste`
- `TrfTeilesuche`
- `TrfZubKalkulation` *(no matching Command class found on server; would fail with ClassNotFound)*
- `TrfZubMarketing`
- `TrfZubMisc`
- `TrfZubSuche`
- `TrfZubTechnisch`
- `TrfZubVerwaltung`

> Note: If the command class is missing (ClassNotFound), the request fails with `CommandExecutionException`.

### Command IDs (special handling)

The following command IDs **do not require session validation** in `doService()`:

- `3`, `43`, `44`, `120`, `123`, `126`, `127`, `128`, `145`, `264`

Other special cases:

- `commandID == 2` → Login (session is created here)
- `commandID == 159` → `INSERT_US_LISTEN`
  - Must be `TrfHashMap`
  - Key: `us_filename`
  - Calls `createUSListen(sessInfo, us_filename)`

## Error handling

If the executed command sets a networking exception in the Transferable, `ClientService` maps it to localized user-friendly messages:

- `UnknownHostException`
- `ConnectException`
- `HttpConnection.ConnectionTimeoutException`
- `NoRouteToHostException`
- `BindException`

Messages are resolved via `MultiLingualMsg.getMsgText(...)` using the session language.

---

## Files referenced

- `webetk/javaserver/servlet/ClientService.java`
- `webetk/javaserver/servlet/BaseServlet.java`
- `webetk/communication/transferables/Transferable.java`
- `webetk/communication/command/TransferableCommand.java`
- `webetk/communication/command/AbstractTransferableCommand.java`
