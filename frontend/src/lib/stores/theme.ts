import { browser } from '$app/environment';
import { writable } from 'svelte/store';

const STORAGE_KEY = 'etkx.theme';

type Theme = 'light' | 'dark' | 'system';

const getInitial = (): Theme => {
  if (!browser) {
    return 'system';
  }

  return (localStorage.getItem(STORAGE_KEY) as Theme) || 'system';
};

export const theme = writable<Theme>(getInitial());

if (browser) {
  theme.subscribe((value) => {
    localStorage.setItem(STORAGE_KEY, value);

    if (
      value === 'dark' ||
      (value === 'system' && window.matchMedia('(prefers-color-scheme: dark)').matches)
    ) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  });
}

export type { Theme };
