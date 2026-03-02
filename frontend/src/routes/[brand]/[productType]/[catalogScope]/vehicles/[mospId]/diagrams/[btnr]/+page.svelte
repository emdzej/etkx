<script lang="ts">
  import { page } from '$app/stores';
  import { getDiagramDetails, getDiagramLines, type DiagramDetails, type DiagramLine } from '$lib/api';
  import Breadcrumb from '$lib/components/Breadcrumb.svelte';
  import DiagramViewer from '$lib/components/DiagramViewer.svelte';
  import PartsTable from '$lib/components/PartsTable.svelte';
  import { myVehicles } from '$lib/stores/myVehicles';
  import { type Brand, type ProductType, type CatalogScope, brandLabels } from '$lib/types/catalog';

  const DEFAULT_ISO = 'EN';

  const brand = $derived($page.params.brand as Brand);
  const productType = $derived($page.params.productType as ProductType);
  const catalogScope = $derived($page.params.catalogScope as CatalogScope);
  const mospId = $derived($page.params.mospId ?? '');
  const btnr = $derived($page.params.btnr ?? '');
  const datum = $derived($page.url.searchParams.get('datum') || '');
  const basePath = $derived(`/${brand}/${productType}/${catalogScope}/vehicles/${mospId}`);
  
  const currentVehicle = $derived($myVehicles.find((vehicle) => vehicle.mospId === mospId));
  const vehicleDatum = $derived(currentVehicle?.datum || datum);
  const vehicleName = $derived(currentVehicle?.label || mospId);
  const vehicleInfo = $derived({
    mospId,
    name: vehicleName,
    datum: vehicleDatum || undefined
  });

  let details = $state<DiagramDetails | null>(null);
  let lines = $state<DiagramLine[]>([]);
  let highlightedNr = $state<string | null>(null);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadDiagram = async () => {
    if (!mospId || !btnr) {
      details = null;
      lines = [];
      return;
    }

    loading = true;
    errorMessage = null;
    try {
      const [diagramDetails, diagramLines] = await Promise.all([
        getDiagramDetails(btnr),
        getDiagramLines(btnr, mospId, DEFAULT_ISO, vehicleDatum || undefined)
      ]);
      details = diagramDetails;
      lines = diagramLines.vehicleLines;
    } catch (error) {
      console.error('Failed to load diagram data:', error);
      errorMessage = 'Failed to load diagram data.';
      details = null;
      lines = [];
    } finally {
      loading = false;
    }
  };

  const handleHighlight = (event: CustomEvent<{ bildnummer: string | null }>) => {
    highlightedNr = event.detail.bildnummer;
  };

  $effect(() => {
    void loadDiagram();
  });

  const diagramName = $derived(details?.name || `Diagram ${btnr}`);

  const crumbs = $derived([
    { label: brandLabels[brand], href: `/${brand}/${productType}/${catalogScope}/vehicles` },
    { label: vehicleName, href: basePath },
    { label: diagramName }
  ]);
</script>

<svelte:head>
  <title>{diagramName} | {brandLabels[brand]} ETKx</title>
</svelte:head>

<div class="mx-auto max-w-6xl space-y-6">
  <div>
    <Breadcrumb {crumbs} />
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">{btnr} {diagramName}</h1>
    <p class="mt-1 text-slate-600 dark:text-slate-400">{vehicleName}</p>
  </div>

  {#if errorMessage}
    <p class="text-sm text-red-600 dark:text-red-400">{errorMessage}</p>
  {/if}

  {#if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Loading diagram...</p>
  {:else}
    <div class="space-y-6">
      <DiagramViewer
        btnr={btnr}
        hotspots={details?.hotspots ?? []}
        highlightedNr={highlightedNr}
        on:highlight={handleHighlight}
      />
      <PartsTable
        lines={lines}
        highlightedNr={highlightedNr}
        vehicle={vehicleInfo}
        btnr={btnr}
        on:highlight={handleHighlight}
      />
    </div>
  {/if}
</div>
