import type {
  DiagramDetails,
  Baujahr,
  DiagramLines,
  DiagramSummary,
  Getriebe,
  Karosserie,
  Lenkung,
  MainGroup,
  Modell,
  MospId,
  PartByNumber,
  PartReplacement,
  SimpleReplacement,
  PartSearchLine,
  PartUsageVehicle,
  Region,
  Series,
  SeriesParams,
  SubGroup,
  TypesParams,
  VehicleIdentification,
  VehicleType,
  Zulassungsmonat
} from './types';

const DEFAULT_ISO = 'EN';
const DEFAULT_REGISO = 'US';

const API_BASE_URL =
  (typeof import.meta !== 'undefined' && import.meta.env?.PUBLIC_API_BASE_URL) ||
  (typeof import.meta !== 'undefined' && import.meta.env?.VITE_API_BASE_URL) ||
  (typeof import.meta !== 'undefined' && import.meta.env?.API_BASE_URL) ||
  'http://localhost:8080';

type QueryValue = string | number | boolean | null | undefined;
type QueryParam = QueryValue | QueryValue[];

type QueryParams = Record<string, QueryParam>;

const appendQueryParams = (url: URL, params?: QueryParams) => {
  if (!params) {
    return;
  }
  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === null) {
      return;
    }
    if (Array.isArray(value)) {
      value.forEach((entry) => {
        if (entry === undefined || entry === null) {
          return;
        }
        url.searchParams.append(key, String(entry));
      });
      return;
    }
    url.searchParams.append(key, String(value));
  });
};

const withLanguage = (params: QueryParams = {}, iso?: string, regiso?: string): QueryParams => ({
  ...params,
  iso: iso ?? params.iso ?? DEFAULT_ISO,
  regiso: regiso ?? params.regiso ?? DEFAULT_REGISO
});

const request = async <T>(path: string, params?: QueryParams, init?: RequestInit): Promise<T> => {
  const url = new URL(path, API_BASE_URL);
  appendQueryParams(url, params);

  const response = await fetch(url.toString(), {
    ...init,
    headers: {
      Accept: 'application/json',
      ...(init?.headers ?? {})
    }
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(
      `API request failed (${response.status} ${response.statusText})${message ? `: ${message}` : ''}`
    );
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
};

export const getVehicleByVin = async (
  vin: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<VehicleIdentification> => {
  const results = await request<VehicleIdentification[]>(
    '/api/vehicles/identify',
    withLanguage({ vin }, iso, regiso)
  );
  if (results.length === 0) {
    throw new Error(`Vehicle not found for VIN ${vin}`);
  }
  return results[0];
};

export const getSeries = (params: SeriesParams): Promise<Series[]> =>
  request('/api/vehicles/series', withLanguage(params, params.iso, params.regiso));

export const getTypes = (params: TypesParams): Promise<VehicleType[]> =>
  request('/api/vehicles/types', withLanguage(params, params.iso, params.regiso));

export const getBodies = (
  baureihe: string,
  katalogumfang: string,
  regionen: string[],
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO,
  lenkung?: string
): Promise<Karosserie[]> =>
  request(
    '/api/vehicles/bodies',
    withLanguage({ baureihe, katalogumfang, regionen, lenkung }, iso, regiso)
  );

export const getModels = (
  baureihe: string,
  katalogumfang: string,
  regionen: string[],
  karosserie?: string,
  lenkung?: string
): Promise<Modell[]> =>
  request('/api/vehicles/models', { baureihe, katalogumfang, regionen, karosserie, lenkung });

export const getRegions = (
  baureihe: string,
  katalogumfang: string,
  modell: string,
  regionen: string[],
  karosserie?: string,
  lenkung?: string
): Promise<Region[]> =>
  request('/api/vehicles/regions', {
    baureihe,
    katalogumfang,
    modell,
    regionen,
    karosserie,
    lenkung
  });

export const getSteerings = (
  baureihe: string,
  katalogumfang: string,
  karosserie: string,
  modell: string,
  region: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<Lenkung[]> =>
  request(
    '/api/vehicles/steerings',
    withLanguage({ baureihe, katalogumfang, karosserie, modell, region }, iso, regiso)
  );

export const getTransmissions = (
  baureihe: string,
  katalogumfang: string,
  karosserie: string,
  modell: string,
  region: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO,
  lenkung?: string
): Promise<Getriebe[]> =>
  request(
    '/api/vehicles/transmissions',
    withLanguage({ baureihe, katalogumfang, karosserie, modell, region, lenkung }, iso, regiso)
  );

export const getYears = (
  baureihe: string,
  katalogumfang: string,
  modell: string,
  region: string,
  karosserie?: string,
  lenkung?: string,
  getriebe?: string
): Promise<Baujahr[]> =>
  request('/api/vehicles/years', {
    baureihe,
    katalogumfang,
    modell,
    region,
    karosserie,
    lenkung,
    getriebe
  });

export const getMonths = (
  baureihe: string,
  katalogumfang: string,
  modell: string,
  region: string,
  baujahr: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO,
  karosserie?: string,
  lenkung?: string,
  getriebe?: string
): Promise<Zulassungsmonat[]> =>
  request(
    '/api/vehicles/months',
    withLanguage({ baureihe, katalogumfang, modell, region, baujahr, karosserie, lenkung, getriebe }, iso, regiso)
  );

export const resolveMospId = (
  baureihe: string,
  modell: string,
  region: string,
  karosserie?: string,
  produktart?: string
): Promise<MospId[]> =>
  request('/api/vehicles/resolve', { baureihe, modell, region, karosserie, produktart });

export const searchParts = (
  mospId: string,
  designation: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<PartSearchLine[]> =>
  request('/api/parts/search/vehicle', { mosp: mospId, query: designation, iso, regiso });

export const searchByPartNumber = (
  partNumber: string,
  iso: string = DEFAULT_ISO
): Promise<PartByNumber[]> => request('/api/parts/search/by-number', { partNumber, iso });

export const getPartReplacements = (
  partNumber: string,
  iso: string = DEFAULT_ISO
): Promise<PartReplacement[]> =>
  request(`/api/parts/${partNumber}/replacements`, { iso });

export const getPartSupersession = (
  partNumber: string,
  iso: string = DEFAULT_ISO
): Promise<SimpleReplacement[]> =>
  request(`/api/parts/${partNumber}/supersession`, { iso });

export const getPartVehicleUsage = (
  partNumber: string,
  mospId: string | null,
  iso: string = DEFAULT_ISO
): Promise<PartUsageVehicle[]> =>
  request(`/api/parts/${partNumber}/usage/simple`, {
    ...(mospId && { mospId }),
    iso
  });

export const getPartDiagrams = (
  partNumber: string,
  mospId: string
): Promise<{ btnr: string; name: string }[]> =>
  request(`/api/diagrams/part/${partNumber}`, { mospId });

export const getDiagramDetails = (btnr: string): Promise<DiagramDetails> =>
  request(`/api/diagrams/${btnr}`);

export const getDiagramLines = (
  btnr: string,
  mospId: string,
  iso: string = DEFAULT_ISO,
  datum?: string
): Promise<DiagramLines> => request(`/api/diagrams/${btnr}/lines`, { mospId, iso, datum });

export const getDiagramImageUrl = (btnr: string): string =>
  new URL(`/api/diagrams/${btnr}/image`, API_BASE_URL).toString();

export const getMainGroups = (
  mospId: string,
  iso: string = DEFAULT_ISO
): Promise<MainGroup[]> => request('/api/catalog/groups', { mospId, iso });

export const getSubGroups = (
  mospId: string,
  hg: string,
  iso: string = DEFAULT_ISO
): Promise<SubGroup[]> => request(`/api/catalog/groups/${hg}/subgroups`, { mospId, iso });

export const getDiagrams = (
  mospId: string,
  hg: string,
  fg: string,
  iso: string = DEFAULT_ISO
): Promise<DiagramSummary[]> => request('/api/catalog/diagrams', { mospId, hg, fg, iso });

export { API_BASE_URL, DEFAULT_ISO, DEFAULT_REGISO };
