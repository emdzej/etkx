<script>
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { getSubGroups } from '$lib/api';
  import GroupGrid from '$lib/components/GroupGrid.svelte';

  let groups = $state([]);
  let loading = $state(false);
  let error = $state('');

  const mospid = $derived($page.params.mospid);
  const hg = $derived($page.params.hg);
  const produktart = $derived($page.url.searchParams.get('produktart') ?? 'P');

  const loadGroups = async () => {
    if (!hg) return;
    loading = true;
    error = '';
    try {
      groups = await getSubGroups(hg, produktart);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load sub groups';
      groups = [];
    } finally {
      loading = false;
    }
  };

  onMount(loadGroups);
</script>

<svelte:head>
  <title>Sub groups {hg}</title>
</svelte:head>

<h1>Sub groups for {hg}</h1>
<p class="subtitle">Produktart: {produktart}</p>

{#if loading}
  <p>Loading sub groups...</p>
{:else if error}
  <p class="error">{error}</p>
{:else}
  <GroupGrid
    title="Select a sub group"
    items={groups.map((group) => ({
      code: group.fg,
      label: group.name ?? `Group ${hg}.${group.fg}`,
      imageUrl: group.thumbnailUrl,
      href: group.diagramNumber
        ? `/vehicles/${mospid}/diagrams/${group.diagramNumber}?produktart=${produktart}`
        : '#'
    }))}
    emptyMessage="No sub groups available."
  />
{/if}

<style>
  .subtitle {
    color: #64748b;
    margin-bottom: 1.5rem;
  }

  .error {
    color: #b91c1c;
  }
</style>
