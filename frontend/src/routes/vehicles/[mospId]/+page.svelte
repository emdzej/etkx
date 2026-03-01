<script lang="ts">
  import { page } from '$app/stores';
  import { getMainGroups, type MainGroup } from '$lib/api';
  import GroupCard from '$lib/components/GroupCard.svelte';

  const DEFAULT_ISO = 'EN';

  const mospId = $derived($page.params.mospId);

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
</script>

<svelte:head>
  <title>Catalog | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div>
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Parts Catalog</h1>
    <p class="mt-1 text-slate-600 dark:text-slate-400">
      Vehicle: <span class="font-mono">{mospId}</span>
    </p>
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
    <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {#each groups as group}
        <GroupCard
          code={group.hg}
          name={group.name}
          thumbnailId={group.thumbnailId}
          href={`/vehicles/${mospId}/groups/${group.hg}`}
        />
      {/each}
    </div>
  {/if}
</div>
