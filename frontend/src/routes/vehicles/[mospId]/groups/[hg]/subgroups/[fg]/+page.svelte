<script lang="ts">
  import { page } from '$app/stores';
  import { getDiagrams, type DiagramSummary } from '$lib/api';
  import SubgroupCard from '$lib/components/SubgroupCard.svelte';

  const DEFAULT_ISO = 'EN';

  const mospId = $derived($page.params.mospId);
  const hg = $derived($page.params.hg);
  const fg = $derived($page.params.fg);

  let diagrams = $state<DiagramSummary[]>([]);
  let loading = $state(false);
  let errorMessage = $state<string | null>(null);

  const loadDiagrams = async () => {
    if (!mospId || !hg || !fg) {
      diagrams = [];
      return;
    }
    loading = true;
    errorMessage = null;
    try {
      diagrams = await getDiagrams(mospId, hg, fg, DEFAULT_ISO);
    } catch (e) {
      console.error('Failed to load diagrams:', e);
      errorMessage = 'Failed to load diagrams.';
      diagrams = [];
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    void loadDiagrams();
  });
</script>

<svelte:head>
  <title>Diagrams | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-5xl space-y-6">
  <div class="space-y-1">
    <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Subgroup {hg}-{fg}</h1>
    <p class="text-slate-600 dark:text-slate-400">Vehicle: <span class="font-mono">{mospId}</span></p>
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
          href={`/vehicles/${mospId}/diagrams/${diagram.btnr}`}
        />
      {/each}
    </div>
  {/if}
</div>
