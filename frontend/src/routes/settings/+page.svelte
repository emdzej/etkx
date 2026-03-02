<script lang="ts">
  import { myVehicles } from '$lib/stores/myVehicles';

  let importMessage = $state<{ type: 'success' | 'error'; text: string } | null>(null);
  let fileInput: HTMLInputElement | null = $state(null);

  const handleExport = () => {
    const data = myVehicles.exportData();
    const blob = new Blob([data], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `etkx-vehicles-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleImport = async (event: Event) => {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    try {
      const text = await file.text();
      const result = myVehicles.importData(text, false);
      if (result.success) {
        importMessage = { type: 'success', text: `Imported ${result.count} vehicle(s)` };
      } else {
        importMessage = { type: 'error', text: result.error || 'Import failed' };
      }
    } catch (e) {
      importMessage = { type: 'error', text: 'Failed to read file' };
    }

    // Reset input
    input.value = '';
    
    // Clear message after 3s
    setTimeout(() => {
      importMessage = null;
    }, 3000);
  };

  const handleClearAll = () => {
    if (confirm('Remove all saved vehicles? This cannot be undone.')) {
      myVehicles.clear();
      importMessage = { type: 'success', text: 'All vehicles removed' };
      setTimeout(() => {
        importMessage = null;
      }, 3000);
    }
  };
</script>

<svelte:head>
  <title>Settings - ETKx</title>
</svelte:head>

<div class="mx-auto max-w-2xl px-4 py-8">
  <h1 class="mb-8 text-2xl font-bold text-slate-900 dark:text-white">Settings</h1>

  <!-- My Vehicles Section -->
  <section class="rounded-xl border border-slate-200 bg-white p-6 dark:border-slate-700 dark:bg-slate-900">
    <h2 class="mb-4 text-lg font-semibold text-slate-900 dark:text-white">My Vehicles</h2>
    
    <p class="mb-4 text-sm text-slate-600 dark:text-slate-400">
      You have <strong>{$myVehicles.length}</strong> saved vehicle(s).
    </p>

    <div class="flex flex-wrap gap-3">
      <button
        type="button"
        class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-700"
        onclick={handleExport}
        disabled={$myVehicles.length === 0}
      >
        Export JSON
      </button>

      <button
        type="button"
        class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50 dark:border-slate-600 dark:bg-slate-800 dark:text-slate-200 dark:hover:bg-slate-700"
        onclick={() => fileInput?.click()}
      >
        Import JSON
      </button>

      <button
        type="button"
        class="rounded-lg border border-red-300 bg-white px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50 dark:border-red-800 dark:bg-slate-800 dark:text-red-400 dark:hover:bg-red-900/20"
        onclick={handleClearAll}
        disabled={$myVehicles.length === 0}
      >
        Clear All
      </button>
    </div>

    <input
      type="file"
      accept=".json,application/json"
      class="hidden"
      bind:this={fileInput}
      onchange={handleImport}
    />

    {#if importMessage}
      <div
        class="mt-4 rounded-lg px-4 py-2 text-sm {importMessage.type === 'success'
          ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400'
          : 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400'}"
      >
        {importMessage.text}
      </div>
    {/if}
  </section>
</div>
