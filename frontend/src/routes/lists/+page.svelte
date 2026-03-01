<script lang="ts">
  import { goto } from '$app/navigation';
  import { activeListId, createList, deleteList, partsLists, setActiveList } from '$lib/stores/partsLists';

  let isCreating = $state(false);
  let newListName = $state('');

  const formatDate = (value: string) => {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return '—';
    }

    return new Intl.DateTimeFormat(undefined, {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    }).format(date);
  };

  const startCreate = () => {
    isCreating = true;
    newListName = '';
  };

  const cancelCreate = () => {
    isCreating = false;
    newListName = '';
  };

  const handleCreate = async () => {
    const name = newListName.trim();
    if (!name) {
      return;
    }

    const list = createList(name);
    setActiveList(list.id);
    isCreating = false;
    newListName = '';
    await goto(`/lists/${list.id}`);
  };

  const handleCreateKeydown = (event: KeyboardEvent) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      handleCreate();
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      cancelCreate();
    }
  };

  const handleDelete = (event: Event, listId: string) => {
    event.stopPropagation();
    const confirmed = window.confirm('Delete this list? This cannot be undone.');
    if (!confirmed) {
      return;
    }

    deleteList(listId);
  };

  const openList = (listId: string) => {
    goto(`/lists/${listId}`);
  };
</script>

<section class="mx-auto w-full max-w-7xl px-4 py-8 md:px-6">
  <header class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
    <div>
      <p class="text-xs font-semibold uppercase tracking-wide text-blue-600 dark:text-blue-400">Parts lists</p>
      <h1 class="mt-1 text-2xl font-semibold text-slate-900 dark:text-white">My Parts Lists</h1>
    </div>

    <div class="flex flex-col items-start gap-2 sm:flex-row sm:items-center">
      <button
        type="button"
        class="inline-flex items-center justify-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-500"
        onclick={startCreate}
      >
        + Create New List
      </button>
    </div>
  </header>

  {#if isCreating}
    <div
      class="mt-6 flex flex-col gap-3 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm dark:border-slate-700 dark:bg-slate-900 sm:flex-row sm:items-center"
    >
      <div class="flex-1">
        <label class="mb-2 block text-sm font-medium text-slate-700 dark:text-slate-200">
          List name
        </label>
        <input
          type="text"
          class="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm transition focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
          placeholder="e.g. Brake service"
          bind:value={newListName}
          onkeydown={handleCreateKeydown}
          autofocus
        />
      </div>
      <div class="flex items-center gap-2">
        <button
          type="button"
          class="rounded-lg px-3 py-2 text-sm font-semibold text-slate-500 transition hover:text-slate-700 dark:text-slate-300 dark:hover:text-white"
          onclick={cancelCreate}
        >
          Cancel
        </button>
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-500"
          onclick={handleCreate}
        >
          Create list
        </button>
      </div>
    </div>
  {/if}

  {#if partsLists.length === 0}
    <div
      class="mt-8 rounded-2xl border border-dashed border-slate-200 bg-slate-50 p-8 text-center text-slate-600 dark:border-slate-700 dark:bg-slate-900/40 dark:text-slate-300"
    >
      <p class="text-lg font-semibold">No parts lists yet.</p>
      <p class="mt-2 text-sm">Create your first list to start collecting parts.</p>
    </div>
  {:else}
    <div class="mt-8 grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
      {#each partsLists as list (list.id)}
        <button
          type="button"
          class="group flex h-full w-full flex-col gap-4 rounded-2xl border border-slate-200 bg-white p-5 text-left shadow-sm transition hover:-translate-y-0.5 hover:border-blue-200 hover:shadow-md dark:border-slate-700 dark:bg-slate-900 dark:hover:border-blue-500/40 {activeListId === list.id
            ? 'ring-2 ring-blue-200 dark:ring-blue-500/40'
            : ''}"
          onclick={() => openList(list.id)}
        >
          <div class="flex items-start justify-between gap-4">
            <div>
              <h2 class="text-lg font-semibold text-slate-900 dark:text-white">{list.name}</h2>
              <p class="mt-1 text-sm text-slate-500 dark:text-slate-400">
                {list.items.length} {list.items.length === 1 ? 'item' : 'items'}
              </p>
            </div>
            {#if activeListId === list.id}
              <span
                class="rounded-full bg-blue-100 px-2.5 py-1 text-[10px] font-semibold uppercase tracking-wide text-blue-700 dark:bg-blue-500/20 dark:text-blue-200"
              >
                Active
              </span>
            {/if}
          </div>

          <div class="flex flex-wrap items-center gap-3 text-xs text-slate-500 dark:text-slate-400">
            <span>Created {formatDate(list.createdAt)}</span>
            <span class="h-1 w-1 rounded-full bg-slate-300 dark:bg-slate-600"></span>
            <span>Updated {formatDate(list.updatedAt)}</span>
          </div>

          <div class="mt-auto flex items-center justify-between">
            <span class="text-xs font-semibold text-blue-600 transition group-hover:text-blue-500 dark:text-blue-400">
              View list →
            </span>
            <button
              type="button"
              class="inline-flex items-center gap-1 rounded-lg border border-red-200 px-2.5 py-1.5 text-xs font-semibold text-red-600 transition hover:bg-red-50 dark:border-red-900/40 dark:text-red-400 dark:hover:bg-red-950/40"
              onclick={(event) => handleDelete(event, list.id)}
            >
              Delete
            </button>
          </div>
        </button>
      {/each}
    </div>
  {/if}
</section>
