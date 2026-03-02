<script lang="ts">
  import { myVehicles } from '$lib/stores/myVehicles';

  let isOpen = $state(false);

  const toggle = () => {
    isOpen = !isOpen;
  };

  const close = () => {
    isOpen = false;
  };

  const handleRemove = (mospId: string) => {
    myVehicles.remove(mospId);
  };
</script>

<div class="relative">
  <button
    type="button"
    class="inline-flex items-center gap-2 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm font-medium text-slate-700 shadow-sm transition hover:border-slate-300 hover:text-slate-900 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:hover:border-slate-600"
    onclick={toggle}
  >
    <span>🚗 My Vehicles</span>
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      stroke-width="2"
      stroke-linecap="round"
      stroke-linejoin="round"
      class="h-4 w-4"
    >
      <polyline points="6 9 12 15 18 9" />
    </svg>
  </button>

  {#if isOpen}
    <div
      class="absolute left-0 mt-2 w-64 rounded-xl border border-slate-200 bg-white p-2 shadow-lg dark:border-slate-700 dark:bg-slate-900"
    >
      {#if $myVehicles.length === 0}
        <div class="px-3 py-2 text-sm text-slate-500 dark:text-slate-400">No saved vehicles</div>
      {:else}
        <ul class="max-h-64 overflow-auto">
          {#each $myVehicles as vehicle (vehicle.mospId)}
            <li class="flex items-center justify-between gap-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800">
              <a
                href="/vehicles/{vehicle.mospId}{vehicle.datum ? `?datum=${vehicle.datum}` : ''}"
                class="flex-1 truncate px-3 py-2 text-sm text-slate-700 dark:text-slate-100"
                title={vehicle.label}
                onclick={close}
              >
                {vehicle.label}
              </a>
              <button
                type="button"
                class="mr-2 inline-flex h-6 w-6 items-center justify-center rounded-full text-slate-400 transition hover:bg-slate-200 hover:text-slate-700 dark:hover:bg-slate-700 dark:hover:text-slate-100"
                aria-label={`Remove ${vehicle.label}`}
                onclick={() => handleRemove(vehicle.mospId)}
              >
                ×
              </button>
            </li>
          {/each}
        </ul>
      {/if}
      <div class="mt-2 border-t border-slate-200 pt-2 dark:border-slate-700">
        <a
          href="/vehicles"
          class="block rounded-lg px-3 py-2 text-sm font-medium text-blue-600 hover:bg-blue-50 dark:text-blue-400 dark:hover:bg-slate-800"
          onclick={close}
        >
          Add vehicle...
        </a>
      </div>
    </div>
  {/if}
</div>
