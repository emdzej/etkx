<script lang="ts">
  import { page } from '$app/stores';
  import { getMainGroups, type MainGroup } from '$lib/api';
  import Breadcrumb from '$lib/components/Breadcrumb.svelte';
  import GroupCard from '$lib/components/GroupCard.svelte';
  import { myVehicles } from '$lib/stores/myVehicles';
  import { type Brand, type ProductType, type CatalogScope, brandLabels } from '$lib/types/catalog';

  const DEFAULT_ISO = 'EN';

  const brand = $derived($page.params.brand as Brand);
  const productType = $derived($page.params.productType as ProductType);
  const catalogScope = $derived($page.params.catalogScope as CatalogScope);
  const mospId = $derived($page.params.mospId);
  const datum = $derived($page.url.searchParams.get('datum') || '');
  const datumParam = $derived(datum ? `?datum=${datum}` : '');
  const basePath = $derived(`/${brand}/${productType}/${catalogScope}/vehicles/${mospId}`);

  // Get vehicle name from saved vehicles or fallback to mospId
  const savedVehicle = $derived($myVehicles.find((v) => v.mospId === mospId));
  const vehicleName = $derived(savedVehicle?.label || mospId);

  let groups = $state<MainGroup[]>([]);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadGroups = async () => {
    if (!mospId) {
      groups = [];
      return;
    }
    loading = true;
    errorMessage = null;
    try {
      groups = await getMainGroups(mospId, DEFAULT_ISO);
    } catch (e) {
      console.error('Failed to load main groups:', e);
      errorMessage = 'Failed to load main groups.';
      groups = [];
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    void loadGroups();
  });

  const crumbs = $derived([
    { label: brandLabels[brand], href: `/${brand}/${productType}/${catalogScope}/vehicles` },
    { label: vehicleName }
  ]);
</script>

<svelte:head>
  <title>Catalog | {brandLabels[brand]} ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div>
    <Breadcrumb {crumbs} />
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Parts Catalog</h1>
    <p class="mt-1 text-slate-600 dark:text-slate-400">{vehicleName}</p>
  </div>

  {#if errorMessage}
    <p class="text-sm text-red-600 dark:text-red-400">{errorMessage}</p>
  {/if}

  {#if loading}
    <p class="text-sm text-slate-500 dark:text-slate-400">Loading groups...</p>
  {:else if groups.length === 0}
    <div class="rounded-lg border border-slate-200 bg-slate-50 p-8 text-center dark:border-slate-700 dark:bg-slate-900">
      <p class="text-slate-500 dark:text-slate-400">No groups found for this vehicle.</p>
    </div>
  {:else}
    <div class="grid gap-3 grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6">
      {#each groups as group}
        <GroupCard
          code={group.hg}
          name={group.name}
          thumbnailId={group.thumbnailId}
          href={`${basePath}/groups/${group.hg}${datumParam}`}
        />
      {/each}
    </div>
  {/if}
</div>
