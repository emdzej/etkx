<script lang="ts">
  import SearchInput from './SearchInput.svelte';
  import SettingsPanel from './SettingsPanel.svelte';
  import VehicleDropdown from './VehicleDropdown.svelte';

  let settingsOpen = false;
</script>

<header
  class="sticky top-0 z-40 border-b border-slate-200 bg-white/90 backdrop-blur dark:border-slate-800 dark:bg-slate-950/90"
>
  <div class="mx-auto flex w-full max-w-7xl flex-wrap items-center gap-3 px-4 py-3 md:px-6">
    <a
      href="/"
      class="text-lg font-semibold text-slate-900 transition hover:text-blue-600 dark:text-white dark:hover:text-blue-400"
    >
      BMW ETKx
    </a>

    <nav class="flex items-center gap-3 text-sm font-semibold text-slate-600 dark:text-slate-300">
      <a
        href="/lists"
        class="rounded-lg px-2 py-1 transition hover:text-blue-600 dark:hover:text-blue-400"
      >
        Lists
      </a>
    </nav>

    <div class="flex flex-1 flex-wrap items-center gap-3">
      <VehicleDropdown />
      <div class="min-w-[220px] flex-1">
        <SearchInput />
      </div>
    </div>

    <button
      type="button"
      class="inline-flex items-center justify-center rounded-lg border border-slate-200 bg-white p-2 text-slate-600 shadow-sm transition hover:border-slate-300 hover:text-slate-900 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:hover:border-slate-600"
      aria-label="Settings"
      onclick={() => (settingsOpen = true)}
    >
      <svg
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="h-4 w-4"
      >
        <circle cx="12" cy="12" r="3" />
        <path
          d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09a1.65 1.65 0 0 0 1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9c0 .7.4 1.34 1.02 1.63.32.15.66.24 1.03.24H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"
        />
      </svg>
    </button>
  </div>
</header>

<SettingsPanel bind:open={settingsOpen} />
