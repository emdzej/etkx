<script lang="ts">
  import { page } from '$app/stores';
  import { getSubGroups, getMainGroups, type SubGroup, type MainGroup } from '$lib/api';
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
  const datum = $derived($page.url.searchParams.get('datum') || '');
  const datumParam = $derived(datum ? `?datum=${datum}` : '');
  const basePath = $derived(`/${brand}/${productType}/${catalogScope}/vehicles/${mospId}`);

  // Get vehicle name from saved vehicles
  const savedVehicle = $derived($myVehicles.find((v) => v.mospId === mospId));
  const vehicleName = $derived(savedVehicle?.label || mospId);

  let subgroups = $state<SubGroup[]>([]);
  let mainGroup = $state<MainGroup | null>(null);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadData = async () => {
    if (!mospId || !hg) {
      subgroups = [];
      return;
    }
    loading = true;
    errorMessage = null;
    try {
      const [subgroupsData, mainGroups] = await Promise.all([
        getSubGroups(mospId, hg, DEFAULT_ISO),
        getMainGroups(mospId, DEFAULT_ISO)
      ]);
      subgroups = subgroupsData;
      mainGroup = mainGroups.find((g) => g.hg === hg) || null;
    } catch (e) {
      console.error('Failed to load subgroups:', e);
      errorMessage = 'Failed to load subgroups.';
      subgroups = [];
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    void loadData();
  });

  const groupName = $derived(mainGroup?.name || `Group ${hg}`);

  const crumbs = $derived([
    { label: brandLabels[brand], href: `/${brand}/${productType}/${catalogScope}/vehicles` },
    { label: vehicleName, href: basePath },
    { label: `${hg} ${groupName}` }
  ]);
</script>

<svelte:head>
  <title>{groupName} | {brandLabels[brand]} ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div>
    <Breadcrumb {crumbs} />
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">{hg} {groupName}</h1>
    <p class="mt-1 text-slate-600 dark:text-slate-400">{vehicleName}</p>
  </div>

  {#if errorMessage}
    <p class="text-sm text-red-600 dark:text-red-400">{errorMessage}</p>
  {/if}

  {#if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Loading subgroups...</p>
  {:else if subgroups.length === 0}
    <div class="rounded-lg border border-slate-200 bg-slate-50 p-8 text-center dark:border-slate-700 dark:bg-slate-900">
      <p class="text-slate-500 dark:text-slate-400">No subgroups found for this main group.</p>
    </div>
  {:else}
    <div class="grid gap-3 grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6">
      {#each subgroups as subgroup}
        <SubgroupCard
          code={subgroup.fg}
          name={subgroup.name}
          thumbnailId={subgroup.thumbnailId}
          href={`${basePath}/groups/${hg}/subgroups/${subgroup.fg}${datumParam}`}
        />
      {/each}
    </div>
  {/if}
</div>
