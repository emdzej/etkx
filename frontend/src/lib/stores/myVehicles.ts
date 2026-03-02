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

export const myVehicles = {
  subscribe: store.subscribe,
  set: store.set,
  update: store.update,
  add,
  remove,
  clear
};
