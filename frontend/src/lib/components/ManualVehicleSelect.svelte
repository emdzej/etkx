<script lang="ts">
  import { goto } from '$app/navigation';
  import { getSeries } from '$lib/api';
  import type { Series } from '$lib/api/types';
  import { myVehicles } from '$lib/stores/myVehicles';
  import CascadingSelect from './CascadingSelect.svelte';

  let product = $state('P');
  let series = $state('');
  let seriesOptions = $state<{ value: string; label: string }[]>([]);
  let loadingSeries = $state(false);

  const loadSeries = async () => {
    loadingSeries = true;
    try {
      const result = await getSeries({
        marke: 'BMW',
        produktart: product,
        katalogumfang: 'BE',
        regionen: ['ECE']
      });
      seriesOptions = result.map(s => ({
        value: s.baureihe,
        label: s.extBaureihe || s.baureihe
      }));
    } catch (e) {
      console.error('Failed to load series:', e);
    } finally {
      loadingSeries = false;
    }
  };

  $effect(() => {
    loadSeries();
  });

  const handleSeriesChange = (value: string) => {
    series = value;
  };

  const saveAndNavigate = () => {
    if (!series) return;
    
    const label = seriesOptions.find(s => s.value === series)?.label || series;
    
    myVehicles.add({
      mospId: series,
      label: `BMW ${label}`,
      series: series,
      model: label,
      region: 'ECE',
      addedAt: Date.now()
    });

    goto(`/vehicles/${series}`);
  };
</script>

<div class="space-y-4">
  <div class="grid gap-4 sm:grid-cols-2">
    <CascadingSelect
      label="Product"
      options={[
        { value: 'P', label: 'Car' },
        { value: 'M', label: 'Motorcycle' }
      ]}
      bind:value={product}
      onchange={loadSeries}
    />

    <CascadingSelect
      label="Series"
      options={seriesOptions}
      bind:value={series}
      loading={loadingSeries}
      disabled={loadingSeries}
      placeholder="Select series..."
      onchange={handleSeriesChange}
    />
  </div>

  <p class="text-sm text-slate-500 dark:text-slate-400">
    Note: Full cascading selection (body, model, region, etc.) will be available in a future update.
  </p>

  {#if series}
    <button
      onclick={saveAndNavigate}
      class="rounded-lg bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-green-700"
    >
      Save & Open Catalog
    </button>
  {/if}
</div>
