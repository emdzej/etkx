<script lang="ts">
  import { page } from '$app/stores';
  import { DEFAULT_ISO, searchByPartNumber, searchParts, type PartByNumber, type PartSearchLine } from '$lib/api';
  import PartCard from '$lib/components/PartCard.svelte';
  import SearchInput from '$lib/components/SearchInput.svelte';
  import { myVehicles } from '$lib/stores/myVehicles';

  type PartSearchResult = PartSearchLine | PartByNumber;

  let query = $state('');
  let results = $state<PartSearchResult[]>([]);
  let loading = $state(false);
  let pageIndex = $state(0);
  const pageSize = 20;

  let debounceTimer: ReturnType<typeof setTimeout> | null = null;
  let requestId = 0;

  const mospId = $derived($page.url.searchParams.get('mospId') ?? $myVehicles[0]?.mospId ?? '');
  const isPartNumber = (value: string) => /^\d/.test(value) && !value.includes(' ');

  $effect(() => {
    const urlQuery = $page.url.searchParams.get('q') ?? '';
    if (urlQuery !== query) {
      query = urlQuery;
    }
  });

  $effect(() => {
    if (debounceTimer) {
      clearTimeout(debounceTimer);
    }

    const trimmed = query.trim();
    const partNumberQuery = isPartNumber(trimmed);

    if (trimmed.length < 3 || (!mospId && !partNumberQuery)) {
      results = [];
      pageIndex = 0;
      loading = false;
      return;
    }

    loading = true;
    const currentRequest = ++requestId;

    debounceTimer = setTimeout(async () => {
      try {
        const data = partNumberQuery
          ? await searchByPartNumber(trimmed, DEFAULT_ISO)
          : await searchParts(mospId, trimmed, DEFAULT_ISO);
        if (currentRequest !== requestId) {
          return;
        }
        results = data;
        pageIndex = 0;
      } catch (e) {
        console.error('Failed to search parts:', e);
        if (currentRequest === requestId) {
          results = [];
        }
      } finally {
        if (currentRequest === requestId) {
          loading = false;
        }
      }
    }, 300);
  });

  const totalPages = $derived(Math.max(1, Math.ceil(results.length / pageSize)));
  const pagedResults = $derived(
    results.slice(pageIndex * pageSize, pageIndex * pageSize + pageSize)
  );

  const prevPage = () => {
    pageIndex = Math.max(0, pageIndex - 1);
  };

  const nextPage = () => {
    pageIndex = Math.min(totalPages - 1, pageIndex + 1);
  };
</script>

<svelte:head>
  <title>Part Search | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-6xl space-y-6">
  <div class="space-y-2">
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Part Search</h1>
    <p class="text-sm text-slate-500 dark:text-slate-400">
      {#if mospId}
        Searching within vehicle <span class="font-mono">{mospId}</span>.
      {:else}
        Search by part number (e.g. 11127...) or select a vehicle to search by description.
      {/if}
    </p>
  </div>

  <div class="max-w-2xl">
    <SearchInput bind:value={query} placeholder="Search by part number or description..." />
  </div>

  {#if query.trim().length < 3}
    <p class="text-sm text-slate-500 dark:text-slate-400">
      Enter at least 3 characters to start searching.
    </p>
  {:else if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Searching parts...</p>
  {:else if !mospId && !isPartNumber(query.trim())}
    <p class="text-sm text-slate-500 dark:text-slate-400">
      Select a vehicle to search by description, or enter a part number.
    </p>
  {:else if results.length === 0}
    <div class="rounded-lg border border-slate-200 bg-slate-50 p-8 text-center dark:border-slate-700 dark:bg-slate-900">
      <p class="text-slate-500 dark:text-slate-400">No parts found for "{query.trim()}".</p>
    </div>
  {:else}
    <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {#each pagedResults as part, i (('sachnummer' in part ? part.sachnummer : part.sachnr) + '-' + i)}
        <PartCard {part} {mospId} />
      {/each}
    </div>

    <div class="flex items-center justify-between gap-4 pt-4">
      <button
        type="button"
        class="rounded-lg border border-slate-200 px-3 py-2 text-sm font-medium text-slate-600 transition hover:border-slate-300 hover:text-slate-900 disabled:cursor-not-allowed disabled:opacity-50 dark:border-slate-700 dark:text-slate-200 dark:hover:border-slate-600"
        onclick={prevPage}
        disabled={pageIndex === 0}
      >
        Previous
      </button>
      <span class="text-sm text-slate-500 dark:text-slate-400">
        Page {pageIndex + 1} of {totalPages}
      </span>
      <button
        type="button"
        class="rounded-lg border border-slate-200 px-3 py-2 text-sm font-medium text-slate-600 transition hover:border-slate-300 hover:text-slate-900 disabled:cursor-not-allowed disabled:opacity-50 dark:border-slate-700 dark:text-slate-200 dark:hover:border-slate-600"
        onclick={nextPage}
        disabled={pageIndex + 1 >= totalPages}
      >
        Next
      </button>
    </div>
  {/if}
</div>
