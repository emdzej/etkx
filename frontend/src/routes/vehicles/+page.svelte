<script>
  import VehicleSelector from '$lib/components/VehicleSelector.svelte';

  let selectedVehicle = $state(null);

  const handleSelect = (vehicle) => {
    selectedVehicle = vehicle;
  };
</script>

<svelte:head>
  <title>Vehicle selection</title>
</svelte:head>

<h1>Vehicle selection</h1>

<VehicleSelector onSelect={handleSelect} />

{#if selectedVehicle}
  <section class="vehicle-card">
    <h2>Selected vehicle</h2>
    <dl>
      <div>
        <dt>MOSPID</dt>
        <dd>{selectedVehicle.mospid}</dd>
      </div>
      <div>
        <dt>Series</dt>
        <dd>{selectedVehicle.series ?? '—'}</dd>
      </div>
      <div>
        <dt>Body</dt>
        <dd>{selectedVehicle.body ?? '—'}</dd>
      </div>
      <div>
        <dt>Engine</dt>
        <dd>{selectedVehicle.engine ?? '—'}</dd>
      </div>
      <div>
        <dt>Steering</dt>
        <dd>{selectedVehicle.steering ?? '—'}</dd>
      </div>
      <div>
        <dt>Transmission</dt>
        <dd>{selectedVehicle.transmission ?? '—'}</dd>
      </div>
    </dl>
    <a class="link" href={`/vehicles/${selectedVehicle.mospid}`}>Open details</a>
  </section>
{/if}

<style>
  .vehicle-card {
    margin-top: 2rem;
    padding: 1.5rem;
    border-radius: 12px;
    border: 1px solid #e2e8f0;
    background: #f8fafc;
    max-width: 32rem;
  }

  dl {
    display: grid;
    gap: 0.75rem;
    margin: 1rem 0 1.5rem;
  }

  dt {
    font-weight: 600;
    color: #0f172a;
  }

  dd {
    margin: 0.2rem 0 0;
    color: #334155;
  }

  .link {
    color: #2563eb;
    font-weight: 600;
  }
</style>
