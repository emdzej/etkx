import { browser } from '$app/environment';
import { writable } from 'svelte/store';

export type Language = {
  iso: string;
  regiso: string;
  name: string;
};

const STORAGE_KEY = 'etkx.language';
const DEFAULT_LANGUAGE: Language = { iso: 'EN', regiso: 'US', name: 'English (US)' };

export const availableLanguages: Language[] = [
  { iso: 'EN', regiso: 'US', name: 'English (US)' },
  { iso: 'EN', regiso: 'GB', name: 'English (UK)' },
  { iso: 'DE', regiso: 'DE', name: 'Deutsch' },
  { iso: 'PL', regiso: 'PL', name: 'Polski' },
  { iso: 'FR', regiso: 'FR', name: 'Français' },
  { iso: 'ES', regiso: 'ES', name: 'Español' },
  { iso: 'IT', regiso: 'IT', name: 'Italiano' }
];

const readInitial = (): Language => {
  if (!browser) {
    return DEFAULT_LANGUAGE;
  }

  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return DEFAULT_LANGUAGE;
  }

  try {
    return JSON.parse(raw) as Language;
  } catch {
    return DEFAULT_LANGUAGE;
  }
};

const store = writable<Language>(readInitial());

if (browser) {
  store.subscribe((value) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
  });
}

export const language = {
  subscribe: store.subscribe,
  set: store.set,
  update: store.update
};
