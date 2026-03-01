<script lang="ts">
  import { page } from '$app/stores';
  import { getSubGroups, type SubGroup } from '$lib/api';
  import SubgroupCard from '$lib/components/SubgroupCard.svelte';

  const DEFAULT_ISO = 'EN';

  const mospId = $derived($page.params.mospId);
  const hg = $derived($page.params.hg);

  let subgroups = $state<SubGroup[]>([]);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadSubgroups = async () => {
    if (!mospId || !hg) {
      subgroups = [];
      return;
    }
    loading = true;
    errorMessage = null;
    try {
      subgroups = await getSubGroups(mospId, hg, DEFAULT_ISO);
    } catch (e) {
      console.error('Failed to load subgroups:', e);
      errorMessage = 'Failed to load subgroups.';
      subgroups = [];
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    void loadSubgroups();
  });
</script>

<svelte:head>
  <title>Groups | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div class="space-y-1">
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Main group {hg}</h1>
    <p class="text-slate-600 dark:text-slate-400">Vehicle: <span class="font-mono">{mospId}</span></p>
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
          href={`/vehicles/${mospId}/diagrams/${subgroup.btnr}`}
        />
      {/each}
    </div>
  {/if}
</div>
