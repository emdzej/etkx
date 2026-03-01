<script lang="ts">
  import { page } from '$app/stores';
  import {
    DEFAULT_ISO,
    getPartDiagrams,
    getPartSupersession,
    getPartVehicleUsage,
    type PartUsageVehicle,
    type SimpleReplacement
  } from '$lib/api';
  import SupersessionChain from '$lib/components/SupersessionChain.svelte';
  import VehicleUsageList from '$lib/components/VehicleUsageList.svelte';
  import QuickAddButton from '$lib/components/QuickAddButton.svelte';
  import { myVehicles } from '$lib/stores/myVehicles';

  const rawPartNumber = $derived($page.params.nr ?? '');
  const partNumber = $derived(rawPartNumber.length > 7 ? rawPartNumber.slice(-7) : rawPartNumber);
  const fullPartNumber = $derived(rawPartNumber.length === 11 ? rawPartNumber : partNumber);
  const mospId = $derived($page.url.searchParams.get('mospId') ?? $myVehicles[0]?.mospId ?? '');

  let replacements = $state<SimpleReplacement[]>([]);
  let vehicleUsage = $state<PartUsageVehicle[]>([]);
  let diagrams = $state<{ btnr: string; name: string }[]>([]);

  let loading = $state(false);

  const matchedPart = $derived(
    replacements.find((item) => item.sachnummer === partNumber) ??
      replacements.find((item) => item.sachnummerAlt === partNumber)
  );

  const partName = $derived(matchedPart?.benennung ?? '');

  const partSupplement = $derived(matchedPart?.zusatz ?? '');

  const loadPartDetails = async () => {
    if (!partNumber) {
      return;
    }

    loading = true;

    const [replacementResult, usageResult, diagramResult] = await Promise.allSettled([
      getPartSupersession(partNumber, DEFAULT_ISO),
      mospId ? getPartVehicleUsage(partNumber, mospId || null, DEFAULT_ISO) : Promise.resolve([]),
      mospId ? getPartDiagrams(partNumber, mospId) : Promise.resolve([])
    ]);

    if (replacementResult.status === 'fulfilled') {
      replacements = replacementResult.value;
    } else {
      console.error('Failed to load supersession data:', replacementResult.reason);
      replacements = [];
    }

    if (usageResult.status === 'fulfilled') {
      vehicleUsage = usageResult.value;
    } else {
      console.error('Failed to load vehicle usage data:', usageResult.reason);
      vehicleUsage = [];
    }

    if (diagramResult.status === 'fulfilled') {
      diagrams = diagramResult.value;
    } else {
      console.error('Failed to load diagram data:', diagramResult.reason);
      diagrams = [];
    }

    loading = false;
  };

  $effect(() => {
    void loadPartDetails();
  });
</script>

<svelte:head>
  <title>Part {fullPartNumber} | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-8">
  <header class="space-y-3">
    <div class="flex flex-wrap items-start justify-between gap-3">
      <div class="space-y-1">
        <div class="text-sm text-slate-500 dark:text-slate-400">Part Number</div>
        <h1 class="text-3xl font-semibold text-slate-900 dark:text-white">{fullPartNumber}</h1>
        {#if partName}
          <p class="text-lg text-slate-700 dark:text-slate-300">
            {partName}
            {#if partSupplement}
              <span class="text-slate-500 dark:text-slate-400"> · {partSupplement}</span>
            {/if}
          </p>
        {:else}
          <p class="text-sm text-slate-500 dark:text-slate-400">Description unavailable.</p>
        {/if}
      </div>

      {#if partNumber}
        <div class="mt-1">
          <QuickAddButton
            partNumber={partNumber}
            fullPartNumber={fullPartNumber}
            partName={partName}
            defaultQuantity={1}
            vehicle={undefined}
            diagramRef={undefined}
          />
        </div>
      {/if}
    </div>
  </header>

  <!-- errors handled per-section -->

  {#if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Loading part data...</p>
  {:else}
    <section class="space-y-3">
      <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Supersession</h2>
      <SupersessionChain current={partNumber} replacements={replacements} />
    </section>

    <section class="space-y-3">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Vehicle Usage</h2>
        {#if !mospId}
          <span class="text-xs text-slate-500 dark:text-slate-400">Select a vehicle to load usage.</span>
        {/if}
      </div>
      <VehicleUsageList usage={vehicleUsage} />
    </section>

    <section class="space-y-3">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Diagrams</h2>
        {#if !mospId}
          <span class="text-xs text-slate-500 dark:text-slate-400">Select a vehicle to load diagrams.</span>
        {/if}
      </div>

      {#if diagrams.length === 0}
        <p class="text-sm text-slate-500 dark:text-slate-400">No diagrams available for this part.</p>
      {:else}
        <div class="grid gap-3 sm:grid-cols-2">
          {#each diagrams as diagram (diagram.btnr)}
            <a
              href={`/vehicles/${mospId}/diagrams/${diagram.btnr}`}
              class="rounded-xl border border-slate-200 bg-white p-4 text-sm text-slate-700 shadow-sm transition hover:-translate-y-0.5 hover:border-blue-300 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
            >
              <div class="text-xs font-semibold uppercase text-slate-400">Diagram {diagram.btnr}</div>
              <div class="mt-1 font-medium text-slate-900 dark:text-white">{diagram.name}</div>
            </a>
          {/each}
        </div>
      {/if}
    </section>
  {/if}
</div>
