<script lang="ts">
  import { availableLanguages, language, theme } from '$lib/stores';

  export let open = false;

  const getLanguageKey = (lang: { iso: string; regiso: string }) => `${lang.iso}-${lang.regiso}`;

  $: selectedLanguage = getLanguageKey($language);

  const close = () => {
    open = false;
  };

  const handleLanguageChange = (event: Event) => {
    const value = (event.currentTarget as HTMLSelectElement).value;
    const nextLanguage = availableLanguages.find((lang) => getLanguageKey(lang) === value);

    if (nextLanguage) {
      language.set(nextLanguage);
    }
  };

  const handleThemeToggle = (event: Event) => {
    const checked = (event.currentTarget as HTMLInputElement).checked;
    theme.set(checked ? 'dark' : 'light');
  };
</script>

{#if open}
  <div class="fixed inset-0 z-40 bg-black/50" onclick={close}></div>
{/if}

<div
  class="fixed right-0 top-0 z-50 h-full w-80 border-l border-slate-200 bg-white shadow-xl transition-transform duration-300 transform dark:border-slate-800 dark:bg-slate-900"
  class:translate-x-0={open}
  class:translate-x-full={!open}
  class:pointer-events-none={!open}
>
  <div class="flex items-center justify-between border-b border-slate-200 p-4 dark:border-slate-800">
    <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Settings</h2>
    <button
      type="button"
      class="inline-flex h-8 w-8 items-center justify-center rounded-full text-slate-500 transition hover:bg-slate-100 hover:text-slate-900 dark:text-slate-300 dark:hover:bg-slate-800 dark:hover:text-white"
      onclick={close}
      aria-label="Close settings"
    >
      ✕
    </button>
  </div>

  <div class="space-y-6 p-4">
    <div>
      <label class="mb-2 block text-sm font-medium text-slate-700 dark:text-slate-200">
        Language
      </label>
      <select
        class="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm transition focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
        value={selectedLanguage}
        onchange={handleLanguageChange}
      >
        {#each availableLanguages as lang}
          <option value={getLanguageKey(lang)}>{lang.name}</option>
        {/each}
      </select>
    </div>

    <div class="flex items-center justify-between">
      <span class="text-sm font-medium text-slate-700 dark:text-slate-200">Dark mode</span>
      <input
        type="checkbox"
        class="h-4 w-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500 dark:border-slate-600 dark:bg-slate-900 dark:checked:bg-blue-500"
        checked={$theme === 'dark'}
        onchange={handleThemeToggle}
      />
    </div>
  </div>
</div>
