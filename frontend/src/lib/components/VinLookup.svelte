<script lang="ts">
  import { goto } from '$app/navigation';
  import { getVehicleByVin } from '$lib/api';
  import type { VehicleIdentification } from '$lib/api/types';
  import { myVehicles } from '$lib/stores/myVehicles';

  let vin = $state('');
  let loading = $state(false);
  let error = $state('');
  let result = $state<VehicleIdentification | null>(null);

  const extractLast7 = (input: string): string => {
    const cleaned = input.replace(/\s/g, '').toUpperCase();
    if (cleaned.length >= 17) {
      return cleaned.slice(-7);
    }
    return cleaned;
  };

  const lookup = async () => {
    if (!vin.trim()) return;
    
    loading = true;
    error = '';
    result = null;

    try {
      const searchVin = extractLast7(vin);
      result = await getVehicleByVin(searchVin);
    } catch (e) {
      error = e instanceof Error ? e.message : 'Failed to lookup VIN';
    } finally {
      loading = false;
    }
  };

  // Normalize datum from YYYYMMDD to YYYY-MM-DD
  const normalizeDatum = (raw: string): string => {
    if (raw.length === 8) {
      return `${raw.slice(0, 4)}-${raw.slice(4, 6)}-${raw.slice(6, 8)}`;
    }
    return raw;
  };

  const datum = $derived(result ? normalizeDatum(result.produktionsdatum || '9999-12-31') : '');
  const catalogUrl = $derived(result ? `/vehicles/${result.modellspalte}?datum=${datum}` : '');
  const isSaved = $derived(result ? $myVehicles.some((v) => v.mospId === result.modellspalte) : false);

  const openCatalog = () => {
    if (!catalogUrl) return;
    goto(catalogUrl);
  };

  const saveVehicle = () => {
    if (!result) return;

    myVehicles.add({
      mospId: result.modellspalte,
      datum,
      label: `${result.baureihe} ${result.modell} ${datum}`.trim(),
      series: result.baureihe,
      model: result.modell,
      region: result.region || 'ECE',
      addedAt: Date.now()
    });
  };
</script>

<div class="space-y-4">
  <div class="flex gap-2">
    <input
      type="text"
      bind:value={vin}
      placeholder="Enter VIN (full or last 7 digits)"
      class="flex-1 rounded-lg border border-slate-200 bg-white px-4 py-2 text-slate-700 shadow-sm transition focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
      onkeydown={(e) => e.key === 'Enter' && lookup()}
    />
    <button
      onclick={lookup}
      disabled={loading || !vin.trim()}
      class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50"
    >
      {loading ? 'Searching...' : 'Lookup'}
    </button>
  </div>

  {#if error}
    <div class="rounded-lg bg-red-50 p-4 text-sm text-red-700 dark:bg-red-900/20 dark:text-red-400">
      {error}
    </div>
  {/if}

  {#if result}
    <div class="rounded-lg border border-slate-200 bg-white p-4 dark:border-slate-700 dark:bg-slate-900">
      <h3 class="font-semibold text-slate-900 dark:text-white">Vehicle Found</h3>
      <dl class="mt-2 grid grid-cols-2 gap-2 text-sm">
        <dt class="text-slate-500 dark:text-slate-400">Series</dt>
        <dd class="text-slate-900 dark:text-white">{result.baureihe}</dd>
        <dt class="text-slate-500 dark:text-slate-400">Model</dt>
        <dd class="text-slate-900 dark:text-white">{result.modell}</dd>
        {#if datum}
          <dt class="text-slate-500 dark:text-slate-400">Production Date</dt>
          <dd class="text-slate-900 dark:text-white">{datum}</dd>
        {/if}
        {#if result.region}
          <dt class="text-slate-500 dark:text-slate-400">Region</dt>
          <dd class="text-slate-900 dark:text-white">{result.region}</dd>
        {/if}
      </dl>
      <div class="mt-4 flex gap-2">
        <button
          onclick={openCatalog}
          class="flex-1 rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-blue-700"
        >
          Open Catalog
        </button>
        <button
          onclick={saveVehicle}
          disabled={isSaved}
          class="rounded-lg border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 shadow-sm transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50 dark:border-slate-600 dark:bg-slate-800 dark:text-slate-200 dark:hover:bg-slate-700"
        >
          {isSaved ? '✓ Saved' : 'Save'}
        </button>
      </div>
    </div>
  {/if}
</div>
