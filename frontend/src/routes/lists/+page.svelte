<script lang="ts">
  import { goto } from '$app/navigation';
  import {
    activeListId,
    addItem,
    createList,
    deleteList,
    partsLists,
    setActiveList,
    updateNotes
  } from '$lib/stores/partsLists';

  let isCreating = $state(false);
  let newListName = $state('');
  let importInput: HTMLInputElement | null = null;

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

  const handleImportClick = () => {
    importInput?.click();
  };

  const mergeNotes = (existingNotes: string | undefined, importedNotes: string | undefined): string => {
    const trimmedExisting = existingNotes?.trim() ?? '';
    const trimmedImported = importedNotes?.trim() ?? '';

    if (!trimmedImported) {
      return trimmedExisting;
    }

    if (!trimmedExisting) {
      return trimmedImported;
    }

    return `${trimmedExisting}\n\n---\nImported notes:\n${trimmedImported}`;
  };

  const importList = async (event: Event) => {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }

    try {
      const text = await file.text();
      const data = JSON.parse(text) as {
        version?: string;
        list?: {
          id?: string;
          name?: string;
          notes?: string;
          items?: Array<{
            partNumber?: string;
            fullPartNumber?: string;
            name?: string;
            quantity?: number;
            vehicle?: { name?: string } | null;
            diagramRef?: string | null;
          }>;
        };
      };

      if (!data.version || !data.list || !data.list.name || !Array.isArray(data.list.items)) {
        throw new Error('Invalid format');
      }

      const duplicateList = $partsLists.find(
        (list) => list.id === data.list?.id || list.name.toLowerCase() === data.list?.name?.toLowerCase()
      );

      if (duplicateList) {
        const shouldMerge = window.confirm(
          `A list named "${data.list.name}" already exists. Click OK to merge items into it, or Cancel to create a new list instead.`
        );

        if (shouldMerge) {
          for (const item of data.list.items) {
            if (!item.partNumber || !item.name) {
              continue;
            }

            addItem(duplicateList.id, {
              partNumber: item.partNumber,
              fullPartNumber: item.fullPartNumber ?? item.partNumber,
              name: item.name,
              quantity: Number.isFinite(item.quantity) && item.quantity ? item.quantity : 1,
              vehicle: item.vehicle ?? null,
              diagramRef: item.diagramRef ?? null
            });
          }

          const mergedNotes = mergeNotes(duplicateList.notes, data.list.notes);
          if (mergedNotes !== (duplicateList.notes ?? '')) {
            updateNotes(duplicateList.id, mergedNotes);
          }

          setActiveList(duplicateList.id);
          await goto(`/lists/${duplicateList.id}`);
          input.value = '';
          return;
        }
      }

      const newList = createList(data.list.name);

      if (data.list.notes) {
        updateNotes(newList.id, data.list.notes);
      }

      for (const item of data.list.items) {
        if (!item.partNumber || !item.name) {
          continue;
        }

        addItem(newList.id, {
          partNumber: item.partNumber,
          fullPartNumber: item.fullPartNumber ?? item.partNumber,
          name: item.name,
          quantity: Number.isFinite(item.quantity) && item.quantity ? item.quantity : 1,
          vehicle: item.vehicle ?? null,
          diagramRef: item.diagramRef ?? null
        });
      }

      setActiveList(newList.id);
      await goto(`/lists/${newList.id}`);
    } catch (error) {
      console.error('Failed to import list', error);
      window.alert('Failed to import: Invalid file format');
    }

    input.value = '';
  };

  const handleDelete = (event: Event, listId: string) => {
    event.stopPropagation();
    const confirmed = window.confirm('Delete this list? This cannot be undone.');
    if (!confirmed) {
      return;
    }

    deleteList(listId);
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
        class="inline-flex items-center justify-center gap-2 rounded-lg border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-blue-200 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
        onclick={handleImportClick}
      >
        <svg
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="h-4 w-4"
        >
          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
          <polyline points="17 8 12 3 7 8" />
          <line x1="12" y1="3" x2="12" y2="15" />
        </svg>
        Import
      </button>
      <button
        type="button"
        class="inline-flex items-center justify-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-500"
        onclick={startCreate}
      >
        + Create New List
      </button>
      <input
        type="file"
        accept="application/json,.json"
        class="hidden"
        bind:this={importInput}
        onchange={importList}
      />
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

  {#if $partsLists.length === 0}
    <div
      class="mt-8 rounded-2xl border border-dashed border-slate-200 bg-slate-50 p-8 text-center text-slate-600 dark:border-slate-700 dark:bg-slate-900/40 dark:text-slate-300"
    >
      <p class="text-lg font-semibold">No parts lists yet.</p>
      <p class="mt-2 text-sm">Create your first list to start collecting parts.</p>
    </div>
  {:else}
    <div class="mt-8 grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
      {#each $partsLists as list (list.id)}
        <div
          class="group flex h-full w-full flex-col gap-4 rounded-2xl border border-slate-200 bg-white p-5 text-left shadow-sm transition hover:-translate-y-0.5 hover:border-blue-200 hover:shadow-md dark:border-slate-700 dark:bg-slate-900 dark:hover:border-blue-500/40 {$activeListId === list.id
            ? 'ring-2 ring-blue-200 dark:ring-blue-500/40'
            : ''}"
        >
          <div class="flex items-start justify-between gap-4">
            <div>
              <h2 class="text-lg font-semibold text-slate-900 dark:text-white">{list.name}</h2>
              <p class="mt-1 text-sm text-slate-500 dark:text-slate-400">
                {list.items.length} {list.items.length === 1 ? 'item' : 'items'}
              </p>
            </div>
            {#if $activeListId === list.id}
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
            <a
              href="/lists/{list.id}"
              class="text-xs font-semibold text-blue-600 transition hover:text-blue-500 dark:text-blue-400"
            >
              View list →
            </a>
            <button
              type="button"
              class="inline-flex items-center gap-1 rounded-lg border border-red-200 px-2.5 py-1.5 text-xs font-semibold text-red-600 transition hover:bg-red-50 dark:border-red-900/40 dark:text-red-400 dark:hover:bg-red-950/40"
              onclick={(event) => handleDelete(event, list.id)}
            >
              Delete
            </button>
          </div>
        </div>
      {/each}
    </div>
  {/if}
</section>
