<script lang="ts">
  import { availableLanguages, language, theme } from '$lib/stores';
  import { myVehicles } from '$lib/stores/myVehicles';

  interface Props {
    open: boolean;
  }

  let { open = $bindable(false) }: Props = $props();
  let fileInput: HTMLInputElement | null = $state(null);
  let importMessage = $state<{ type: 'success' | 'error'; text: string } | null>(null);

  const selectedLanguage = $derived(
    `${$language.iso}-${$language.regiso}`
  );

  const close = () => {
    open = false;
  };

  const handleLanguageChange = (event: Event) => {
    const value = (event.currentTarget as HTMLSelectElement).value;
    const nextLanguage = availableLanguages.find((lang) => `${lang.iso}-${lang.regiso}` === value);

    if (nextLanguage) {
      language.set(nextLanguage);
    }
  };

  const handleThemeToggle = (event: Event) => {
    const checked = (event.currentTarget as HTMLInputElement).checked;
    theme.set(checked ? 'dark' : 'light');
  };

  const handleExport = () => {
    const data = myVehicles.exportData();
    const blob = new Blob([data], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `etkx-vehicles-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleImport = async (event: Event) => {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    try {
      const text = await file.text();
      const result = myVehicles.importData(text, false);
      if (result.success) {
        importMessage = { type: 'success', text: `Imported ${result.count} vehicle(s)` };
      } else {
        importMessage = { type: 'error', text: result.error || 'Import failed' };
      }
    } catch {
      importMessage = { type: 'error', text: 'Failed to read file' };
    }

    input.value = '';
    setTimeout(() => { importMessage = null; }, 3000);
  };

  const handleClearAll = () => {
    if (confirm('Remove all saved vehicles? This cannot be undone.')) {
      myVehicles.clear();
      importMessage = { type: 'success', text: 'All vehicles removed' };
      setTimeout(() => { importMessage = null; }, 3000);
    }
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
          <option value={`${lang.iso}-${lang.regiso}`}>{lang.name}</option>
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

    <!-- My Vehicles -->
    <div class="border-t border-slate-200 pt-4 dark:border-slate-700">
      <h3 class="mb-3 text-sm font-medium text-slate-700 dark:text-slate-200">
        My Vehicles ({$myVehicles.length})
      </h3>
      
      <div class="flex flex-wrap gap-2">
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-1.5 text-xs font-medium text-white transition hover:bg-blue-700 disabled:opacity-50"
          onclick={handleExport}
          disabled={$myVehicles.length === 0}
        >
          Export
        </button>

        <button
          type="button"
          class="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-medium text-slate-700 transition hover:bg-slate-50 dark:border-slate-600 dark:bg-slate-800 dark:text-slate-200 dark:hover:bg-slate-700"
          onclick={() => fileInput?.click()}
        >
          Import
        </button>

        <button
          type="button"
          class="rounded-lg border border-red-300 bg-white px-3 py-1.5 text-xs font-medium text-red-600 transition hover:bg-red-50 dark:border-red-800 dark:bg-slate-800 dark:text-red-400 dark:hover:bg-red-900/20 disabled:opacity-50"
          onclick={handleClearAll}
          disabled={$myVehicles.length === 0}
        >
          Clear All
        </button>
      </div>

      <input
        type="file"
        accept=".json,application/json"
        class="hidden"
        bind:this={fileInput}
        onchange={handleImport}
      />

      {#if importMessage}
        <div
          class="mt-2 rounded-lg px-3 py-1.5 text-xs {importMessage.type === 'success'
            ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400'
            : 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400'}"
        >
          {importMessage.text}
        </div>
      {/if}
    </div>
  </div>
</div>
