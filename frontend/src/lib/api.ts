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
