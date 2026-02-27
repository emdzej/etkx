<script>
  import VinInput from '$lib/components/VinInput.svelte';

  let vehicle = $state(null);

  const handleDecode = (decoded) => {
    vehicle = decoded;
  };
</script>

<svelte:head>
  <title>VIN decoder</title>
</svelte:head>

<h1>VIN decoder</h1>

<VinInput onDecode={handleDecode} />

{#if vehicle}
  <section class="vehicle-card">
    <h2>Decoded vehicle</h2>
    <dl>
      <div>
        <dt>MOSPID</dt>
        <dd>{vehicle.mospid}</dd>
      </div>
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
    </dl>
    <a class="link" href={`/vehicles/${vehicle.mospid}`}>Open details</a>
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
