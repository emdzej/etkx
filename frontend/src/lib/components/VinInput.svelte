<script>
  import { decodeVin } from '$lib/api';

  const { onDecode } = $props();

  let vin = $state('');
  let loading = $state(false);
  let error = $state('');

  const submit = async (event) => {
    event.preventDefault();
    if (!vin.trim()) return;

    loading = true;
    error = '';
    try {
      const vehicle = await decodeVin(vin.trim());
      onDecode?.(vehicle);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to decode VIN';
      onDecode?.(null);
    } finally {
      loading = false;
    }
  };
</script>

<form class="vin-form" on:submit={submit}>
  <label>
    VIN
    <input type="text" bind:value={vin} placeholder="Enter VIN" />
  </label>
  <button type="submit" disabled={loading}>Decode</button>
  {#if loading}
    <p class="status">Decoding...</p>
  {:else if error}
    <p class="error">{error}</p>
  {/if}
</form>

<style>
  .vin-form {
    display: grid;
    gap: 0.75rem;
    max-width: 30rem;
  }

  label {
    display: grid;
    gap: 0.35rem;
    font-weight: 600;
  }

  input {
    padding: 0.5rem 0.75rem;
    border-radius: 6px;
    border: 1px solid #d0d7de;
  }

  button {
    justify-self: start;
    padding: 0.55rem 1.25rem;
    border-radius: 6px;
    border: none;
    background: #2563eb;
    color: #fff;
    cursor: pointer;
  }

  button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }

  .status {
    color: #0f172a;
  }

  .error {
    color: #b91c1c;
  }
</style>
