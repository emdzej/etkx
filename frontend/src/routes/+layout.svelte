<script lang="ts">
  import '../app.css';
  import { onMount } from 'svelte';
  import Header from '$lib/components/Header.svelte';
  import { theme } from '$lib/stores';

  let { children } = $props();

  onMount(() => {
    const unsubscribe = theme.subscribe(() => undefined);
    return () => unsubscribe();
  });
</script>

<div class="min-h-screen bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-white">
  <Header />
  <main class="mx-auto w-full max-w-7xl px-4 py-6 md:px-6">
    {@render children()}
  </main>
</div>
