export interface PartsList {
  id: string;
  name: string;
  createdAt: string;
  updatedAt: string;
  notes?: string;
  items: PartsListItem[];
}

export interface PartsListItem {
  id: string;
  partNumber: string;
  // sachnr (7 digits)
  fullPartNumber: string;
  // HG+UG+sachnr (11 digits)
  name: string;
  quantity: number;
  addedAt: string;
  vehicle?: {
    mospId: string;
    name: string;
    datum?: string;
  };
  diagramRef?: {
    btnr: string;
    bildnummer: string;
  };
}
