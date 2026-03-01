import { browser } from '$app/environment';
import type { PartsList, PartsListItem } from '$lib/types/partsList';

const STORAGE_KEY = 'etkx-parts-lists';
const ACTIVE_LIST_KEY = 'etkx-parts-lists.active';

const readInitial = (): PartsList[] => {
  if (!browser) {
    return [];
  }

  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return [];
  }

  try {
    const parsed = JSON.parse(raw) as PartsList[];
    return parsed.map((list) => ({
      ...list,
      items: list.items ?? []
    }));
  } catch {
    return [];
  }
};

const readActiveListId = (): string | null => {
  if (!browser) {
    return null;
  }

  const raw = localStorage.getItem(ACTIVE_LIST_KEY);
  if (!raw) {
    return null;
  }

  return raw;
};

export let partsLists = $state<PartsList[]>(readInitial());
export let activeListId = $state<string | null>(readActiveListId());

if (activeListId && !partsLists.some((list) => list.id === activeListId)) {
  activeListId = null;
}

$effect(() => {
  if (!browser) {
    return;
  }

  localStorage.setItem(STORAGE_KEY, JSON.stringify(partsLists));
});

$effect(() => {
  if (!browser) {
    return;
  }

  if (activeListId) {
    localStorage.setItem(ACTIVE_LIST_KEY, activeListId);
  } else {
    localStorage.removeItem(ACTIVE_LIST_KEY);
  }
});

export const createList = (name: string): PartsList => {
  const now = new Date().toISOString();
  const list: PartsList = {
    id: crypto.randomUUID(),
    name,
    createdAt: now,
    updatedAt: now,
    items: []
  };

  partsLists = [...partsLists, list];

  return list;
};

export const deleteList = (id: string): void => {
  partsLists = partsLists.filter((list) => list.id !== id);

  if (activeListId === id) {
    activeListId = null;
  }
};

export const getList = (id: string): PartsList | undefined => partsLists.find((list) => list.id === id);

const updateList = (listId: string, updater: (list: PartsList) => PartsList): void => {
  const list = partsLists.find((item) => item.id === listId);
  if (!list) {
    return;
  }

  const updated = updater(list);
  partsLists = partsLists.map((item) => (item.id === listId ? updated : item));
};

export const addItem = (listId: string, item: Omit<PartsListItem, 'id' | 'addedAt'>): void => {
  updateList(listId, (list) => {
    const now = new Date().toISOString();
    const existing = list.items.find((existingItem) => existingItem.partNumber === item.partNumber);

    if (existing) {
      return {
        ...list,
        updatedAt: now,
        items: list.items.map((existingItem) =>
          existingItem.id === existing.id
            ? {
                ...existingItem,
                quantity: existingItem.quantity + item.quantity
              }
            : existingItem
        )
      };
    }

    const newItem: PartsListItem = {
      ...item,
      id: crypto.randomUUID(),
      addedAt: now
    };

    return {
      ...list,
      updatedAt: now,
      items: [...list.items, newItem]
    };
  });
};

export const updateItemQuantity = (listId: string, itemId: string, quantity: number): void => {
  updateList(listId, (list) => {
    const now = new Date().toISOString();
    return {
      ...list,
      updatedAt: now,
      items: list.items.map((item) => (item.id === itemId ? { ...item, quantity } : item))
    };
  });
};

export const removeItem = (listId: string, itemId: string): void => {
  updateList(listId, (list) => {
    const now = new Date().toISOString();
    return {
      ...list,
      updatedAt: now,
      items: list.items.filter((item) => item.id !== itemId)
    };
  });
};

export const updateNotes = (listId: string, notes: string): void => {
  updateList(listId, (list) => ({
    ...list,
    updatedAt: new Date().toISOString(),
    notes
  }));
};

export const setActiveList = (id: string | null): void => {
  if (id && !partsLists.some((list) => list.id === id)) {
    return;
  }

  activeListId = id;
};

export const getActiveList = (): PartsList | undefined =>
  activeListId ? partsLists.find((list) => list.id === activeListId) : undefined;

export const getItemCount = (): number => {
  const list = getActiveList();
  if (!list) {
    return 0;
  }

  return list.items.reduce((total, item) => total + item.quantity, 0);
};
