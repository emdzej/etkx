# ETK client/server protocol (ETK Java client)

## Summary
ETK does **not** use XML/XStream for the primary client/server protocol. The Java client posts **Java‑serialized objects** (`webetk.communication.transferables.Transferable` and subclasses) to the server via HTTP POST. The server responds with the same serialized object (same `ID`) populated with response data.

Evidence:
- Client uses `ObjectOutputStream`/`ObjectInputStream` in `webetk.communication.CommController`.
- Content‑Type is `application/x-javaserializedobject webetk.communication.transferables.Transferable`.
- When an empty body is sent, the server throws `java.io.EOFException` while creating `ObjectInputStream` (see sample response in `samples/empty-body-response.html`).

> The only XML reference in decompiled sources is `webetk.xmlapi.XmlConstants` (`text/xml`). No XStream usage was found in the client codebase.

## Endpoint
The client reads `server.URL` from `javaclient.properties` and posts to it. In typical local installs it points at the embedded Tomcat endpoint:

```
http://localhost:1033/javaserver/ClientService
```

## Transport
- **Method:** `POST`
- **Content‑Type:** `application/x-javaserializedobject webetk.communication.transferables.Transferable`
- **Body:** Java `ObjectOutputStream` containing a `Transferable` instance
- **Response:** Java `ObjectInputStream` containing a `Transferable` (same `ID`)
- **Optional query params:** appended from `Transferable.serverParameter` (e.g. `PSESSID` for ASAP)

## Base request object (`Transferable`)
Defined in `webetk.communication.transferables.Transferable`:

| Field | Type | Notes |
|---|---|---|
| `commandID` | `int` | Service ID (see `Constants.Services`) |
| `exception` | `Exception` | Set by server on error |
| `ID` | `int` | Auto‑increment per process, used to match response |
| `serverParameter` | `HashMap<String,String>` | Added as URL query params |

### Client flow
1. Create a `Transferable` subclass (e.g. `TrfHashMap`, `TrfImage`).
2. Set request fields (often via `TrfHashMap#setValue`).
3. Set `commandID` via `doSyncComm(cmd)` or `doAsyncComm(cmd)`.
4. POST serialized object to server.
5. Read serialized response object.
6. Ensure IDs match; copy data into the original object.

## Core transferables
- **`TrfHashMap`** – key/value request & response payloads
- **`TrfString`** – string responses (e.g. server version)
- **`TrfBinary`** – raw byte data
- **`TrfImage`** – image metadata + binary data (`TrfBinary`)

## Query parameters (`serverParameter`)
`Transferable.setServerParameter()` appends URL query parameters. One common parameter:

| Param | Purpose | Usage |
|---|---|---|
| `PSESSID` | ASAP session ID | Added for certain ASAP calls (see `DlgFzgIdController.ladeGrafik`) |

## Key commands and parameters
Below are the most common / foundational service calls with parameters observed in the client code.

### LOGIN (service `2`)
**Request:** `TrfHashMap`

| Key | Type | Notes |
|---|---|---|
| `firmaid` | `String` | Company ID |
| `filialid` | `String` | Branch ID |
| `filialid_alt` | `String` | Previous branch ID |
| `username` | `String` | User ID |
| `pwd` | `String` | Password |
| `us_filename` | `String` | US dealer file name (from ETK data) |

**Response:** `TrfHashMap` with `webetkSession` → `LoginInfo`

### LOAD_FILIALEN (service `44`)
**Request:** `TrfHashMap`

| Key | Type |
|---|---|
| `firmaid` | `String` |

**Response:** `TrfHashMap` with indexed keys such as:
- `1filialid`, `1benennung`, `2filialid`, `2benennung`, ...

### EXIST_FIRMAID (service `126`)
**Request:** `TrfHashMap` with `id` (firma ID)

**Response:** `TrfHashMap` with `wert` = `Boolean.TRUE/FALSE`

### LOAD_PROGRAMM_VERSION (service `145`)
**Request:** `TrfString` (empty)

**Response:** `TrfString` with server version string

### LOAD_ETKTEXTE (service `266`)
**Request:** `TrfHashMap` (empty)

**Response:** `TrfHashMap` with `etktexte` → `ETKTexte`

### GET_IMAGE (service `4`)
**Request:** `TrfImage`

| Field | Type | Notes |
|---|---|---|
| `ImageID` | `String` | Image ID |
| `Timestamp` | `String` | Timestamp |
| `FormFactor` | `String` | e.g. `"T"` |
| `ImageFormat` | `String` | may be set/returned |

**Response:** `TrfImage` with binary `data` populated.

## HTTP headers
Only a single header is explicitly set by the client:

```
Content-type: application/x-javaserializedobject webetk.communication.transferables.Transferable
```

Tomcat replies with standard HTTP status codes; on malformed bodies it returns an HTML error report (`500` with EOFException).

## Samples
Because the payload is Java‑serialized binary, the files in `docs/etk-analysis/samples/` provide **pseudo‑XML** representations for documentation purposes:

- `login-request.xml`
- `login-response.xml`
- `get-image-request.xml`

Additionally, `empty-body-response.html` captures the server error when no serialized body is provided.
