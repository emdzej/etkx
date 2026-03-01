<script lang="ts">
  import { activeListId, createList, partsLists, setActiveList } from '$lib/stores/partsLists';

  export let onSelect: (listId: string) => void;

  let isOpen = $state(false);
  let isCreating = $state(false);
  let newListName = $state('');

  const activeList = $derived(
    activeListId ? partsLists.find((list) => list.id === activeListId) : null
  );

  const toggle = () => {
    isOpen = !isOpen;
    if (!isOpen) {
      isCreating = false;
    }
  };

  const selectList = (listId: string) => {
    setActiveList(listId);
    onSelect(listId);
    isOpen = false;
    isCreating = false;
  };

  const startCreate = () => {
    isCreating = true;
    newListName = '';
  };

  const cancelCreate = () => {
    isCreating = false;
    newListName = '';
  };

  const handleCreate = () => {
    const name = newListName.trim();
    if (!name) {
      return;
    }

    const list = createList(name);
    setActiveList(list.id);
    onSelect(list.id);
    isOpen = false;
    isCreating = false;
    newListName = '';
  };

  const handleInputKeydown = (event: KeyboardEvent) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      handleCreate();
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      cancelCreate();
    }
  };
</script>

<div class="relative">
  <button
    type="button"
    class="inline-flex w-full items-center justify-between gap-2 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm font-medium text-slate-700 shadow-sm transition hover:border-slate-300 hover:text-slate-900 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:hover:border-slate-600"
    onclick={toggle}
  >
    <span class="truncate">
      {#if activeList}
        {activeList.name}
      {:else}
        Select list
      {/if}
    </span>
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
      class="absolute left-0 z-20 mt-2 w-full rounded-xl border border-slate-200 bg-white p-2 shadow-lg dark:border-slate-700 dark:bg-slate-900"
    >
      {#if partsLists.length === 0}
        <div class="px-3 py-2 text-sm text-slate-500 dark:text-slate-400">
          No lists yet.
        </div>
      {:else}
        <ul class="max-h-56 overflow-auto">
          {#each partsLists as list (list.id)}
            <li>
              <button
                type="button"
                class="flex w-full items-center justify-between rounded-lg px-3 py-2 text-left text-sm font-medium transition hover:bg-slate-100 dark:hover:bg-slate-800 {activeListId === list.id
                  ? 'bg-blue-50 text-blue-700 dark:bg-blue-900/40 dark:text-blue-200'
                  : 'text-slate-700 dark:text-slate-100'}"
                onclick={() => selectList(list.id)}
              >
                <span class="truncate">{list.name}</span>
                {#if activeListId === list.id}
                  <span class="text-[10px] font-semibold uppercase tracking-wide">Active</span>
                {/if}
              </button>
            </li>
          {/each}
        </ul>
      {/if}

      <div class="mt-2 border-t border-slate-200 pt-2 dark:border-slate-700">
        {#if isCreating}
          <div class="space-y-2">
            <input
              type="text"
              class="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm transition focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
              placeholder="List name"
              bind:value={newListName}
              onkeydown={handleInputKeydown}
              autofocus
            />
            <div class="flex items-center justify-end gap-2">
              <button
                type="button"
                class="rounded-lg px-3 py-1.5 text-xs font-semibold text-slate-500 transition hover:text-slate-700 dark:text-slate-300 dark:hover:text-white"
                onclick={cancelCreate}
              >
                Cancel
              </button>
              <button
                type="button"
                class="rounded-lg bg-blue-600 px-3 py-1.5 text-xs font-semibold text-white shadow-sm transition hover:bg-blue-500"
                onclick={handleCreate}
              >
                Create
              </button>
            </div>
          </div>
        {:else}
          <button
            type="button"
            class="flex w-full items-center justify-center rounded-lg px-3 py-2 text-sm font-medium text-blue-600 transition hover:bg-blue-50 dark:text-blue-400 dark:hover:bg-slate-800"
            onclick={startCreate}
          >
            + Create new list
          </button>
        {/if}
      </div>
    </div>
  {/if}
</div>
