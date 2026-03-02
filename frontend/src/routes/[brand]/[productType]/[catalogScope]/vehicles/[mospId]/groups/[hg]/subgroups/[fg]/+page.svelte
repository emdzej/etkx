<script lang="ts">
  import { page } from '$app/stores';
  import { getDiagrams, getSubGroups, getMainGroups, type DiagramSummary, type SubGroup, type MainGroup } from '$lib/api';
  import Breadcrumb from '$lib/components/Breadcrumb.svelte';
  import SubgroupCard from '$lib/components/SubgroupCard.svelte';
  import { myVehicles } from '$lib/stores/myVehicles';
  import { type Brand, type ProductType, type CatalogScope, brandLabels } from '$lib/types/catalog';

  const DEFAULT_ISO = 'EN';

  const brand = $derived($page.params.brand as Brand);
  const productType = $derived($page.params.productType as ProductType);
  const catalogScope = $derived($page.params.catalogScope as CatalogScope);
  const mospId = $derived($page.params.mospId);
  const hg = $derived($page.params.hg);
  const fg = $derived($page.params.fg);
  const datum = $derived($page.url.searchParams.get('datum') || '');
  const datumParam = $derived(datum ? `?datum=${datum}` : '');
  const basePath = $derived(`/${brand}/${productType}/${catalogScope}/vehicles/${mospId}`);

  // Get vehicle name from saved vehicles
  const savedVehicle = $derived($myVehicles.find((v) => v.mospId === mospId));
  const vehicleName = $derived(savedVehicle?.label || mospId);

  let diagrams = $state<DiagramSummary[]>([]);
  let mainGroup = $state<MainGroup | null>(null);
  let subGroup = $state<SubGroup | null>(null);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadData = async () => {
    if (!mospId || !hg || !fg) {
      diagrams = [];
      return;
    }
    loading = true;
    errorMessage = null;
    try {
      const [diagramsData, subgroupsData, mainGroups] = await Promise.all([
        getDiagrams(mospId, hg, fg, DEFAULT_ISO),
        getSubGroups(mospId, hg, DEFAULT_ISO),
        getMainGroups(mospId, DEFAULT_ISO)
      ]);
      diagrams = diagramsData;
      mainGroup = mainGroups.find((g) => g.hg === hg) || null;
      subGroup = subgroupsData.find((s) => s.fg === fg) || null;
    } catch (e) {
      console.error('Failed to load diagrams:', e);
      errorMessage = 'Failed to load diagrams.';
      diagrams = [];
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    void loadData();
  });

  const groupName = $derived(mainGroup?.name || `Group ${hg}`);
  const subgroupName = $derived(subGroup?.name || `Subgroup ${fg}`);

  const crumbs = $derived([
    { label: brandLabels[brand], href: `/${brand}/${productType}/${catalogScope}/vehicles` },
    { label: vehicleName, href: basePath },
    { label: `${hg} ${groupName}`, href: `${basePath}/groups/${hg}` },
    { label: `${fg} ${subgroupName}` }
  ]);
</script>

<svelte:head>
  <title>{subgroupName} | {brandLabels[brand]} ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div>
    <Breadcrumb {crumbs} />
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">{hg}-{fg} {subgroupName}</h1>
    <p class="mt-1 text-slate-600 dark:text-slate-400">{vehicleName}</p>
  </div>

  {#if errorMessage}
    <p class="text-sm text-red-600 dark:text-red-400">{errorMessage}</p>
  {/if}

  {#if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Loading diagrams...</p>
  {:else if diagrams.length === 0}
    <div class="rounded-lg border border-slate-200 bg-slate-50 p-8 text-center dark:border-slate-700 dark:bg-slate-900">
      <p class="text-slate-500 dark:text-slate-400">No diagrams found for this subgroup.</p>
    </div>
  {:else}
    <div class="grid gap-3 grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6">
      {#each diagrams as diagram}
        <SubgroupCard
          code={diagram.btnr}
          name={diagram.name}
          thumbnailId={diagram.grafikId}
          href={`${basePath}/groups/${hg}/subgroups/${fg}/diagrams/${diagram.btnr}${datumParam}`}
        />
      {/each}
    </div>
  {/if}
</div>
