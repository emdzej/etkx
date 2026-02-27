<script>
  import { searchParts } from '$lib/api';
  import SearchForm from '$lib/components/SearchForm.svelte';
  import PartList from '$lib/components/PartList.svelte';

  let results = $state([]);
  let loading = $state(false);
  let error = $state('');

  const handleSearch = async ({ query, hg, fg }) => {
    error = '';
    loading = true;
    try {
      results = await searchParts(query, hg, fg);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Search failed';
      results = [];
    } finally {
      loading = false;
    }
  };

  const hasResults = $derived(results.length > 0);
</script>

<svelte:head>
  <title>Part search</title>
</svelte:head>

<h1>Part search</h1>

<SearchForm onSearch={handleSearch} />

{#if loading}
  <p>Searching...</p>
{:else if error}
  <p class="error">{error}</p>
{:else if hasResults}
  <PartList parts={results} />
{/if}

<style>
  .error {
    color: #b91c1c;
  }
</style>
