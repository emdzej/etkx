import { browser } from '$app/environment';
import { get, writable } from 'svelte/store';

export type PartsListItem = {
  sachnr: string;
  quantity: number;
};

const STORAGE_KEY = 'etkx.partsList';

const loadInitial = (): PartsListItem[] => {
  if (!browser) return [];
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw) as PartsListItem[];
    if (!Array.isArray(parsed)) return [];
    return parsed
      .filter((item) => item?.sachnr)
      .map((item) => ({
        sachnr: String(item.sachnr),
        quantity: Number(item.quantity ?? 1)
      }));
  } catch {
    return [];
  }
};

const partsListStore = writable<PartsListItem[]>(loadInitial());
const { subscribe, set, update } = partsListStore;

const persist = (items: PartsListItem[]) => {
  if (!browser) return;
  localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
};

export const partsList = { subscribe };

export const addPart = (sachnr: string, quantity = 1) => {
  if (!sachnr) return;
  update((items) => {
    const normalized = String(sachnr);
    const existing = items.find((item) => item.sachnr === normalized);
    let next: PartsListItem[];
    if (existing) {
      next = items.map((item) =>
        item.sachnr === normalized
          ? { ...item, quantity: Math.max(1, item.quantity + quantity) }
          : item
      );
    } else {
      next = [...items, { sachnr: normalized, quantity: Math.max(1, quantity) }];
    }
    persist(next);
    return next;
  });
};

export const updateQuantity = (sachnr: string, quantity: number) => {
  update((items) => {
    const normalized = String(sachnr);
    const next = items.map((item) =>
      item.sachnr === normalized ? { ...item, quantity: Math.max(1, quantity) } : item
    );
    persist(next);
    return next;
  });
};

export const removePart = (sachnr: string) => {
  update((items) => {
    const normalized = String(sachnr);
    const next = items.filter((item) => item.sachnr !== normalized);
    persist(next);
    return next;
  });
};

export const clearList = () => {
  set([]);
  persist([]);
};

export const exportList = (format: 'csv' | 'json' = 'csv') => {
  const items = get(partsListStore);
  if (format === 'json') {
    return JSON.stringify(items, null, 2);
  }
  const lines = ['sachnr,quantity'];
  for (const item of items) {
    lines.push(`${item.sachnr},${item.quantity}`);
  }
  return lines.join('\n');
};
