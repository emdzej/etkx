import { browser } from '$app/environment';
import { writable } from 'svelte/store';

export type HistoryItem = {
  type: 'part' | 'diagram';
  id: string;
  label: string;
  mospId: string;
  vehicleLabel: string;
  viewedAt: number;
};

const STORAGE_KEY = 'etkx.history';
const MAX_ITEMS = 20;

const readInitial = (): HistoryItem[] => {
  if (!browser) {
    return [];
  }

  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return [];
  }

  try {
    return JSON.parse(raw) as HistoryItem[];
  } catch {
    return [];
  }
};

const store = writable<HistoryItem[]>(readInitial());

if (browser) {
  store.subscribe((value) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
  });
}

const add = (item: HistoryItem) => {
  store.update((items) => {
    const filtered = items.filter(
      (existing) => !(existing.type === item.type && existing.id === item.id && existing.mospId === item.mospId)
    );
    return [item, ...filtered].slice(0, MAX_ITEMS);
  });
};

const clear = () => {
  store.set([]);
};

export const history = {
  subscribe: store.subscribe,
  set: store.set,
  update: store.update,
  add,
  clear
};
