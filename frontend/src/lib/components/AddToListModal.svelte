<script lang="ts">
  import PartsListDropdown from '$lib/components/PartsListDropdown.svelte';
  import { activeListId, addItem, partsLists } from '$lib/stores/partsLists';
  import type { PartsListItem } from '$lib/types/partsList';

  export let open: boolean;
  export let partNumber: string;
  export let fullPartNumber: string;
  export let partName: string;
  export let defaultQuantity: number;
  export let vehicle: PartsListItem['vehicle'] = undefined;
  export let diagramRef: PartsListItem['diagramRef'] = undefined;
  export let onClose: () => void;

  let selectedListId = $state<string | null>(null);
  let quantity = $state<number>(1);

  $effect(() => {
    if (open) {
      quantity = Math.max(1, defaultQuantity ?? 1);
      selectedListId = activeListId ?? null;
    }
  });

  const handleSelect = (listId: string) => {
    selectedListId = listId;
  };

  const handleQuantityInput = (event: Event) => {
    const value = Number((event.currentTarget as HTMLInputElement).value);
    quantity = Number.isFinite(value) && value > 0 ? value : 1;
  };

  const handleConfirm = () => {
    if (!selectedListId) {
      return;
    }

    addItem(selectedListId, {
      partNumber,
      fullPartNumber,
      name: partName,
      quantity,
      vehicle,
      diagramRef
    });

    onClose();
  };

  const hasLists = $derived(partsLists.length > 0);
  const confirmDisabled = $derived(!selectedListId || quantity <= 0);
</script>

{#if open}
  <div class="fixed inset-0 z-40 bg-black/60" onclick={onClose}></div>

  <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
    <div
      class="w-full max-w-lg rounded-2xl border border-slate-200 bg-white p-6 shadow-xl dark:border-slate-700 dark:bg-slate-900"
    >
      <div class="flex items-start justify-between gap-4">
        <div>
          <p class="text-xs font-semibold uppercase tracking-wide text-blue-600 dark:text-blue-400">
            Add to list
          </p>
          <h2 class="mt-1 text-lg font-semibold text-slate-900 dark:text-white">{partName}</h2>
          <div class="mt-1 text-xs text-slate-500 dark:text-slate-400">
            {partNumber} · {fullPartNumber}
          </div>
        </div>
        <button
          type="button"
          class="inline-flex h-8 w-8 items-center justify-center rounded-full text-slate-400 transition hover:bg-slate-100 hover:text-slate-700 dark:hover:bg-slate-800 dark:hover:text-white"
          onclick={onClose}
          aria-label="Close"
        >
          ✕
        </button>
      </div>

      <div class="mt-6 space-y-4">
        <div>
          <label class="mb-2 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Parts list
          </label>
          <PartsListDropdown onSelect={handleSelect} />
          {#if !hasLists}
            <p class="mt-2 text-xs text-slate-500 dark:text-slate-400">
              Create a list to start collecting parts.
            </p>
          {/if}
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Quantity
          </label>
          <input
            type="number"
            min="1"
            class="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm transition focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
            value={quantity}
            oninput={handleQuantityInput}
          />
        </div>

        <div class="flex items-center justify-end gap-2 pt-2">
          <button
            type="button"
            class="rounded-lg px-4 py-2 text-sm font-semibold text-slate-500 transition hover:text-slate-700 dark:text-slate-300 dark:hover:text-white"
            onclick={onClose}
          >
            Cancel
          </button>
          <button
            type="button"
            class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:bg-slate-300 dark:disabled:bg-slate-700"
            onclick={handleConfirm}
            disabled={confirmDisabled}
          >
            Add part
          </button>
        </div>
      </div>
    </div>
  </div>
{/if}
