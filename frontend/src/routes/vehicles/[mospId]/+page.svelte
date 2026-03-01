<script lang="ts">
  import { page } from '$app/stores';

  const mospId = $derived($page.params.mospId);
</script>

<svelte:head>
  <title>Catalog | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-4xl">
  <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Parts Catalog</h1>
  <p class="mt-1 text-slate-600 dark:text-slate-400">
    Vehicle: <span class="font-mono">{mospId}</span>
  </p>
  
  <div class="mt-8 rounded-lg border border-slate-200 bg-slate-50 p-8 text-center dark:border-slate-700 dark:bg-slate-900">
    <p class="text-slate-500 dark:text-slate-400">
      Catalog navigation coming in issue #78
    </p>
  </div>
</div>
