<script lang="ts">
  import AddToListModal from '$lib/components/AddToListModal.svelte';
  import type { PartsListItem } from '$lib/types/partsList';

  export let partNumber: string;
  export let fullPartNumber: string;
  export let partName: string;
  export let defaultQuantity: number;
  export let vehicle: PartsListItem['vehicle'] = undefined;
  export let diagramRef: PartsListItem['diagramRef'] = undefined;

  let isOpen = $state(false);

  const openModal = () => {
    isOpen = true;
  };

  const closeModal = () => {
    isOpen = false;
  };
</script>

<button
  type="button"
  class="inline-flex h-9 w-9 items-center justify-center rounded-lg border border-slate-200 bg-white text-slate-600 shadow-sm transition hover:border-slate-300 hover:bg-slate-50 hover:text-slate-900 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200 dark:hover:border-slate-600"
  onclick={openModal}
  aria-label="Add to parts list"
>
  <span class="text-lg font-semibold">+</span>
</button>

<AddToListModal
  open={isOpen}
  {partNumber}
  {fullPartNumber}
  {partName}
  {defaultQuantity}
  {vehicle}
  {diagramRef}
  onClose={closeModal}
/>
