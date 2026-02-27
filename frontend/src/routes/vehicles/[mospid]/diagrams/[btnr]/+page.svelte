<script>
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { getDiagram } from '$lib/api';
  import DiagramView from '$lib/components/DiagramView.svelte';
  import DiagramPartsList from '$lib/components/DiagramPartsList.svelte';

  let diagram = $state(null);
  let loading = $state(false);
  let error = $state('');

  const btnr = $derived($page.params.btnr);

  const loadDiagram = async () => {
    if (!btnr) return;
    loading = true;
    error = '';
    try {
      diagram = await getDiagram(btnr);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load diagram';
      diagram = null;
    } finally {
      loading = false;
    }
  };

  onMount(loadDiagram);
</script>

<svelte:head>
  <title>Diagram {btnr}</title>
</svelte:head>

<h1>Diagram {btnr}</h1>

{#if loading}
  <p>Loading diagram...</p>
{:else if error}
  <p class="error">{error}</p>
{:else if diagram}
  <div class="layout">
    <DiagramView imageUrl={`/api/catalog/diagrams/${btnr}/image`} title={diagram.name ?? 'Diagram'} />
    <DiagramPartsList parts={diagram.parts ?? []} />
  </div>
{/if}

<style>
  .layout {
    display: grid;
    gap: 1.5rem;
    grid-template-columns: minmax(0, 1.2fr) minmax(0, 1fr);
  }

  .error {
    color: #b91c1c;
  }

  @media (max-width: 960px) {
    .layout {
      grid-template-columns: 1fr;
    }
  }
</style>
