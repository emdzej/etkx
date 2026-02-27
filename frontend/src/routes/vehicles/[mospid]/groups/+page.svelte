<script>
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { getMainGroups } from '$lib/api';
  import GroupGrid from '$lib/components/GroupGrid.svelte';

  let groups = $state([]);
  let loading = $state(false);
  let error = $state('');

  const mospid = $derived($page.params.mospid);
  const produktart = $derived($page.url.searchParams.get('produktart') ?? 'P');

  const loadGroups = async () => {
    loading = true;
    error = '';
    try {
      groups = await getMainGroups(produktart);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load groups';
      groups = [];
    } finally {
      loading = false;
    }
  };

  onMount(loadGroups);
</script>

<svelte:head>
  <title>Main groups</title>
</svelte:head>

<h1>Main groups</h1>
<p class="subtitle">Produktart: {produktart}</p>

{#if loading}
  <p>Loading groups...</p>
{:else if error}
  <p class="error">{error}</p>
{:else}
  <GroupGrid
    title="Select a main group"
    items={groups.map((group) => ({
      code: group.hg,
      label: group.name ?? `Group ${group.hg}`,
      imageUrl: group.iconUrl,
      href: `/vehicles/${mospid}/groups/${group.hg}?produktart=${produktart}`
    }))}
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
