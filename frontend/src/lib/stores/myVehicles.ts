import { browser } from '$app/environment';
import { writable } from 'svelte/store';
import type { Brand, ProductType, CatalogScope } from '$lib/types/catalog';

export type MyVehicle = {
  mospId: string;
  datum: string;
  brand?: Brand;
  productType?: ProductType;
  catalogScope?: CatalogScope;
  label: string;
  series: string;
  model: string;
  region: string;
  addedAt: number;
};

const STORAGE_KEY = 'etkx.myVehicles';

const readInitial = (): MyVehicle[] => {
  if (!browser) {
    return [];
  }

  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return [];
  }

  try {
    const parsed = JSON.parse(raw) as MyVehicle[];
    return parsed.map((vehicle) => ({
      ...vehicle,
      datum: vehicle.datum || '9999-12-31',
      // Default to BMW/car/current for old entries
      brand: vehicle.brand || 'bmw',
      productType: vehicle.productType || 'car',
      catalogScope: vehicle.catalogScope || 'current'
    }));
  } catch {
    return [];
  }
};

const store = writable<MyVehicle[]>(readInitial());

if (browser) {
  store.subscribe((value) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
  });
}

const add = (vehicle: MyVehicle) => {
  store.update((items) => {
    const existing = items.find((item) => item.mospId === vehicle.mospId);
    if (existing) {
      return items.map((item) => (item.mospId === vehicle.mospId ? vehicle : item));
    }
    return [...items, vehicle].sort((a, b) => b.addedAt - a.addedAt);
  });
};

const remove = (mospId: string) => {
  store.update((items) => items.filter((item) => item.mospId !== mospId));
};

const clear = () => {
  store.set([]);
};

const exportData = (): string => {
  let data: MyVehicle[] = [];
  store.subscribe((v) => (data = v))();
  return JSON.stringify(data, null, 2);
};

const importData = (json: string, replace: boolean = false): { success: boolean; count: number; error?: string } => {
  try {
    const parsed = JSON.parse(json) as MyVehicle[];
    if (!Array.isArray(parsed)) {
      return { success: false, count: 0, error: 'Invalid format: expected array' };
    }
    
    const validVehicles = parsed.filter(
      (v) => v.mospId && v.label && typeof v.mospId === 'string'
    ).map((v) => ({
      ...v,
      datum: v.datum || '9999-12-31',
      brand: v.brand || 'bmw',
      productType: v.productType || 'car',
      catalogScope: v.catalogScope || 'current',
      addedAt: v.addedAt || Date.now()
    }));

    if (replace) {
      store.set(validVehicles);
    } else {
      store.update((existing) => {
        const merged = [...existing];
        for (const vehicle of validVehicles) {
          const idx = merged.findIndex((v) => v.mospId === vehicle.mospId);
          if (idx >= 0) {
            merged[idx] = vehicle;
          } else {
            merged.push(vehicle);
          }
        }
        return merged.sort((a, b) => b.addedAt - a.addedAt);
      });
    }

    return { success: true, count: validVehicles.length };
  } catch (e) {
    return { success: false, count: 0, error: e instanceof Error ? e.message : 'Parse error' };
  }
};

export const myVehicles = {
  subscribe: store.subscribe,
  set: store.set,
  update: store.update,
  add,
  remove,
  clear,
  exportData,
  importData
};
