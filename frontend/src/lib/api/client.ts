import type {
  DiagramDetails,
  DiagramLine,
  DiagramLinesResponse,
  MainGroup,
  PartReplacement,
  PartSearchResult,
  PartUsage,
  Series,
  SeriesParams,
  SubGroup,
  TypesParams,
  VehicleIdentification,
  VehicleType
} from './types';

const DEFAULT_ISO = 'EN';
const DEFAULT_REGISO = 'US';

const API_BASE_URL =
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

export const searchParts = (
  query: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<PartSearchResult[]> =>
  request('/api/parts/search/vehicle', withLanguage({ query }, iso, regiso));

export const getPartReplacements = (
  partNr: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<PartReplacement[]> =>
  request(`/api/parts/${partNr}/replacements`, withLanguage({}, iso, regiso));

export const getPartUsage = (
  partNr: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<PartUsage[]> =>
  request(`/api/parts/${partNr}/usage/parts`, withLanguage({}, iso, regiso));

export const getDiagram = (
  btnr: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<DiagramDetails> => request(`/api/diagrams/${btnr}`, withLanguage({}, iso, regiso));

export const getDiagramLines = async (
  btnr: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<DiagramLine[]> => {
  const response = await request<DiagramLinesResponse>(
    `/api/diagrams/${btnr}/lines`,
    withLanguage({}, iso, regiso)
  );
  return [...response.vehicleLines, ...response.ugbLines, ...response.cpLines];
};

export const getDiagramImage = (btnr: string): string =>
  new URL(`/api/diagrams/${btnr}/image`, API_BASE_URL).toString();

export const getMainGroups = (
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<MainGroup[]> => request('/api/parts/groups', withLanguage({}, iso, regiso));

export const getSubGroups = (
  hg: string,
  iso: string = DEFAULT_ISO,
  regiso: string = DEFAULT_REGISO
): Promise<SubGroup[]> => request(`/api/parts/groups/${hg}`, withLanguage({}, iso, regiso));

export { API_BASE_URL, DEFAULT_ISO, DEFAULT_REGISO };
