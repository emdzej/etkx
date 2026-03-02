import { browser } from '$app/environment';
import { writable, derived, get } from 'svelte/store';
import type { PartsList, PartsListItem } from '$lib/types/partsList';

const STORAGE_KEY = 'etkx-parts-lists';
const ACTIVE_LIST_KEY = 'etkx-parts-lists.active';

const readInitial = (): PartsList[] => {
  if (!browser) return [];
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw) as PartsList[];
    return parsed.map((list) => ({ ...list, items: list.items ?? [] }));
  } catch {
    return [];
  }
};

const readActiveListId = (): string | null => {
  if (!browser) return null;
  return localStorage.getItem(ACTIVE_LIST_KEY);
};

// Main stores
const listsStore = writable<PartsList[]>(readInitial());
const activeIdStore = writable<string | null>(readActiveListId());

// Persist on change
if (browser) {
  listsStore.subscribe((lists) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(lists));
  });
  activeIdStore.subscribe((id) => {
    if (id) {
      localStorage.setItem(ACTIVE_LIST_KEY, id);
    } else {
      localStorage.removeItem(ACTIVE_LIST_KEY);
    }
  });
}

// Derived values
export const partsLists = {
  subscribe: listsStore.subscribe,
  get length() { return get(listsStore).length; },
  find: (fn: (list: PartsList) => boolean) => get(listsStore).find(fn),
  some: (fn: (list: PartsList) => boolean) => get(listsStore).some(fn)
};

export const activeListId = {
  subscribe: activeIdStore.subscribe,
  get value() { return get(activeIdStore); }
};

// For reactive access in components, use $partsLists and $activeListId

export const createList = (name: string): PartsList => {
  const now = new Date().toISOString();
  const list: PartsList = {
    id: crypto.randomUUID(),
    name,
    createdAt: now,
    updatedAt: now,
    items: []
  };
  listsStore.update((lists) => [...lists, list]);
  return list;
};

export const deleteList = (id: string): void => {
  listsStore.update((lists) => lists.filter((list) => list.id !== id));
  activeIdStore.update((activeId) => (activeId === id ? null : activeId));
};

export const getList = (id: string): PartsList | undefined => {
  return get(listsStore).find((list) => list.id === id);
};

export const setActiveList = (id: string | null): void => {
  if (id && !get(listsStore).some((list) => list.id === id)) return;
  activeIdStore.set(id);
};

export const addItem = (listId: string, item: Omit<PartsListItem, 'id' | 'addedAt'>): void => {
  listsStore.update((lists) => {
    const list = lists.find((l) => l.id === listId);
    if (!list) return lists;

    const now = new Date().toISOString();
    const existing = list.items.find((i) => i.partNumber === item.partNumber);

    if (existing) {
      return lists.map((l) =>
        l.id === listId
          ? {
              ...l,
              updatedAt: now,
              items: l.items.map((i) =>
                i.id === existing.id ? { ...i, quantity: i.quantity + item.quantity } : i
              )
            }
          : l
      );
    }

    const newItem: PartsListItem = { ...item, id: crypto.randomUUID(), addedAt: now };
    return lists.map((l) =>
      l.id === listId ? { ...l, updatedAt: now, items: [...l.items, newItem] } : l
    );
  });
};

export const updateItemQuantity = (listId: string, itemId: string, quantity: number): void => {
  const now = new Date().toISOString();
  listsStore.update((lists) =>
    lists.map((l) =>
      l.id === listId
        ? { ...l, updatedAt: now, items: l.items.map((i) => (i.id === itemId ? { ...i, quantity } : i)) }
        : l
    )
  );
};

export const removeItem = (listId: string, itemId: string): void => {
  const now = new Date().toISOString();
  listsStore.update((lists) =>
    lists.map((l) =>
      l.id === listId ? { ...l, updatedAt: now, items: l.items.filter((i) => i.id !== itemId) } : l
    )
  );
};

export const updateNotes = (listId: string, notes: string): void => {
  listsStore.update((lists) =>
    lists.map((l) => (l.id === listId ? { ...l, updatedAt: new Date().toISOString(), notes } : l))
  );
};

export const getActiveList = (): PartsList | undefined => {
  const id = get(activeIdStore);
  return id ? get(listsStore).find((list) => list.id === id) : undefined;
};

export const getItemCount = (): number => {
  const list = getActiveList();
  return list ? list.items.reduce((total, item) => total + item.quantity, 0) : 0;
};
