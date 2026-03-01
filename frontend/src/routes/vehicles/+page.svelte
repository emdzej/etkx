<script lang="ts">
  import VinLookup from '$lib/components/VinLookup.svelte';
  import ManualVehicleSelect from '$lib/components/ManualVehicleSelect.svelte';

  let activeTab = $state<'vin' | 'manual'>('vin');
</script>

<svelte:head>
  <title>Select Vehicle | BMW ETKx</title>
</svelte:head>

<div class="mx-auto max-w-2xl">
  <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Select Vehicle</h1>
  <p class="mt-1 text-slate-600 dark:text-slate-400">
    Look up by VIN or manually select your BMW vehicle.
  </p>

  <!-- Tabs -->
  <div class="mt-6 border-b border-slate-200 dark:border-slate-700">
    <nav class="-mb-px flex gap-4">
      <button
        onclick={() => (activeTab = 'vin')}
        class="border-b-2 px-1 pb-3 text-sm font-medium transition {activeTab === 'vin'
          ? 'border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400'
          : 'border-transparent text-slate-500 hover:border-slate-300 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300'}"
      >
        VIN Lookup
      </button>
      <button
        onclick={() => (activeTab = 'manual')}
        class="border-b-2 px-1 pb-3 text-sm font-medium transition {activeTab === 'manual'
          ? 'border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400'
          : 'border-transparent text-slate-500 hover:border-slate-300 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300'}"
      >
        Manual Selection
      </button>
    </nav>
  </div>

  <!-- Tab Content -->
  <div class="mt-6">
    {#if activeTab === 'vin'}
      <VinLookup />
    {:else}
      <ManualVehicleSelect />
    {/if}
  </div>
</div>
