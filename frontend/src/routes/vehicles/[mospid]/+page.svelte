<script>
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { getVehicle } from '$lib/api';

  let vehicle = $state(null);
  let loading = $state(false);
  let error = $state('');

  const mospid = $derived(Number($page.params.mospid));

  const loadVehicle = async () => {
    if (!mospid) return;
    loading = true;
    error = '';
    try {
      vehicle = await getVehicle(mospid);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load vehicle';
      vehicle = null;
    } finally {
      loading = false;
    }
  };

  onMount(loadVehicle);
</script>

<svelte:head>
  <title>Vehicle {mospid}</title>
</svelte:head>

<h1>Vehicle {mospid}</h1>

{#if loading}
  <p>Loading vehicle details...</p>
{:else if error}
  <p class="error">{error}</p>
{:else if vehicle}
  <section class="vehicle-card">
    <dl>
      <div>
        <dt>Series</dt>
        <dd>{vehicle.series ?? '—'}</dd>
      </div>
      <div>
        <dt>Body</dt>
        <dd>{vehicle.body ?? '—'}</dd>
      </div>
      <div>
        <dt>Engine</dt>
        <dd>{vehicle.engine ?? '—'}</dd>
      </div>
      <div>
        <dt>Steering</dt>
        <dd>{vehicle.steering ?? '—'}</dd>
      </div>
      <div>
        <dt>Transmission</dt>
        <dd>{vehicle.transmission ?? '—'}</dd>
      </div>
    </dl>
  </section>
{/if}

<style>
  .vehicle-card {
    margin-top: 1.5rem;
    padding: 1.5rem;
    border-radius: 12px;
    border: 1px solid #e2e8f0;
    background: #f8fafc;
    max-width: 32rem;
  }

  dl {
    display: grid;
    gap: 0.75rem;
  }

  dt {
    font-weight: 600;
    color: #0f172a;
  }

  dd {
    margin: 0.2rem 0 0;
    color: #334155;
  }

  .error {
    color: #b91c1c;
  }
</style>
