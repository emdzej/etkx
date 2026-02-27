<script>
  import { onMount } from 'svelte';
  import { getBodies, getModels, getSeries } from '$lib/api';

  const { onSelect } = $props();

  let produktart = $state('P');
  let series = $state('');
  let body = $state('');
  let model = $state('');

  let seriesOptions = $state([]);
  let bodyOptions = $state([]);
  let modelOptions = $state([]);
  let loading = $state(false);
  let error = $state('');

  const loadSeries = async () => {
    loading = true;
    error = '';
    try {
      seriesOptions = await getSeries(produktart);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load series';
      seriesOptions = [];
    } finally {
      loading = false;
    }
  };

  const loadBodies = async () => {
    if (!series) {
      bodyOptions = [];
      modelOptions = [];
      body = '';
      model = '';
      return;
    }

    loading = true;
    error = '';
    try {
      bodyOptions = await getBodies(series);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load bodies';
      bodyOptions = [];
    } finally {
      loading = false;
    }
  };

  const loadModels = async () => {
    if (!series || !body) {
      modelOptions = [];
      model = '';
      return;
    }

    loading = true;
    error = '';
    try {
      modelOptions = await getModels(series, body);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load models';
      modelOptions = [];
    } finally {
      loading = false;
    }
  };

  const selectModel = () => {
    const selected = modelOptions.find((option) => String(option.mospid) === model);
    onSelect?.(selected ?? null);
  };

  onMount(async () => {
    await loadSeries();
  });

  const handleProduktartChange = async (event) => {
    produktart = event.target.value;
    series = '';
    body = '';
    model = '';
    seriesOptions = [];
    bodyOptions = [];
    modelOptions = [];
    await loadSeries();
  };

  const handleSeriesChange = async (event) => {
    series = event.target.value;
    body = '';
    model = '';
    bodyOptions = [];
    modelOptions = [];
    await loadBodies();
  };

  const handleBodyChange = async (event) => {
    body = event.target.value;
    model = '';
    modelOptions = [];
    await loadModels();
  };

  const handleModelChange = (event) => {
    model = event.target.value;
    selectModel();
  };
</script>

<div class="selector">
  <label>
    Product type
    <select bind:value={produktart} on:change={handleProduktartChange}>
      <option value="P">Passenger cars</option>
      <option value="M">Motorcycles</option>
    </select>
  </label>

  <label>
    Series
    <select bind:value={series} on:change={handleSeriesChange} disabled={seriesOptions.length === 0}>
      <option value="">Select series</option>
      {#each seriesOptions as option}
        <option value={option.series}>
          {option.series}{option.name ? ` — ${option.name}` : ''}
        </option>
      {/each}
    </select>
  </label>

  <label>
    Body
    <select bind:value={body} on:change={handleBodyChange} disabled={!series || bodyOptions.length === 0}>
      <option value="">Select body</option>
      {#each bodyOptions as option}
        <option value={option.body}>{option.body}</option>
      {/each}
    </select>
  </label>

  <label>
    Model
    <select bind:value={model} on:change={handleModelChange} disabled={!body || modelOptions.length === 0}>
      <option value="">Select model</option>
      {#each modelOptions as option}
        <option value={option.mospid}>
          {option.engine ?? 'Unknown engine'} (MOSPID {option.mospid})
        </option>
      {/each}
    </select>
  </label>

  {#if loading}
    <p class="status">Loading...</p>
  {:else if error}
    <p class="error">{error}</p>
  {/if}
</div>

<style>
  .selector {
    display: grid;
    gap: 0.9rem;
    max-width: 32rem;
  }

  label {
    display: grid;
    gap: 0.35rem;
    font-weight: 600;
  }

  select {
    padding: 0.5rem 0.75rem;
    border-radius: 6px;
    border: 1px solid #d0d7de;
    background: #fff;
  }

  .status {
    color: #0f172a;
  }

  .error {
    color: #b91c1c;
  }
</style>
