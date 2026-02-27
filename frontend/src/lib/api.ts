export type PartSearchResult = {
  sachnr: string;
  name: string | null;
  mainGroup: string | null;
  subGroup: string | null;
};

export type PartDetails = {
  sachnr: string;
  name: string | null;
  mainGroup: string | null;
  subGroup: string | null;
  textCode: number | null;
  descriptionSuffix: string | null;
  partType: string | null;
};

export type PartUsage = {
  diagramNumber: string | null;
  position: string | null;
  mainGroup: string | null;
  subGroup: string | null;
};

export type Series = {
  series: string;
  name: string | null;
};

export type Body = {
  body: string;
};

export type Vehicle = {
  mospid: number;
  series: string | null;
  body: string | null;
  engine: string | null;
  steering: string | null;
  transmission: string | null;
};

const defaultFetcher = (...args: Parameters<typeof fetch>) => fetch(...args);

export async function searchParts(
  query: string,
  hg?: string,
  fg?: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<PartSearchResult[]> {
  const params = new URLSearchParams();
  if (query) params.set('q', query);
  if (hg) params.set('hg', hg);
  if (fg) params.set('fg', fg);

  const response = await fetcher(`/api/parts/search?${params.toString()}`);
  if (!response.ok) {
    throw new Error('Failed to search parts');
  }
  return response.json();
}

export async function getPartDetails(
  sachnr: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<PartDetails> {
  const response = await fetcher(`/api/parts/${encodeURIComponent(sachnr)}`);
  if (!response.ok) {
    throw new Error('Failed to load part details');
  }
  return response.json();
}

export async function getPartUsage(
  sachnr: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<PartUsage[]> {
  const response = await fetcher(`/api/parts/${encodeURIComponent(sachnr)}/usage`);
  if (!response.ok) {
    throw new Error('Failed to load part usage');
  }
  return response.json();
}

export async function getSeries(
  produktart: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<Series[]> {
  const params = new URLSearchParams();
  if (produktart) params.set('produktart', produktart);

  const response = await fetcher(`/api/vehicles/series?${params.toString()}`);
  if (!response.ok) {
    throw new Error('Failed to load series');
  }
  return response.json();
}

export async function getBodies(
  series: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<Body[]> {
  const response = await fetcher(`/api/vehicles/series/${encodeURIComponent(series)}/bodies`);
  if (!response.ok) {
    throw new Error('Failed to load bodies');
  }
  return response.json();
}

export async function getModels(
  series: string,
  body: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<Vehicle[]> {
  const response = await fetcher(
    `/api/vehicles/series/${encodeURIComponent(series)}/bodies/${encodeURIComponent(body)}/models`
  );
  if (!response.ok) {
    throw new Error('Failed to load models');
  }
  return response.json();
}

export async function getVehicle(
  mospid: number,
  fetcher: typeof fetch = defaultFetcher
): Promise<Vehicle> {
  const response = await fetcher(`/api/vehicles/${mospid}`);
  if (!response.ok) {
    throw new Error('Failed to load vehicle');
  }
  return response.json();
}

export async function decodeVin(
  vin: string,
  fetcher: typeof fetch = defaultFetcher
): Promise<Vehicle> {
  const response = await fetcher(`/api/vehicles/vin/${encodeURIComponent(vin)}`);
  if (!response.ok) {
    throw new Error('Failed to decode VIN');
  }
  return response.json();
}
