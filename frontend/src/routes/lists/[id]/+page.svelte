<script lang="ts">
  import { page } from '$app/stores';
  import { goto } from '$app/navigation';
  import { browser } from '$app/environment';
  import {
    partsLists,
    removeItem,
    setActiveList,
    updateItemQuantity,
    updateNotes
  } from '$lib/stores/partsLists';

  const listId = $derived($page.params.id ?? '');
  const list = $derived(partsLists.find((item) => item.id === listId));

  let notesDraft = $state('');
  let notesSaving = $state(false);
  let notesSavedAt = $state<string | null>(null);
  let copiedPart = $state<string | null>(null);

  let saveTimeout: ReturnType<typeof setTimeout> | null = null;
  let copiedTimeout: ReturnType<typeof setTimeout> | null = null;

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

  const scheduleNotesSave = () => {
    if (!list) {
      return;
    }

    if (saveTimeout) {
      clearTimeout(saveTimeout);
    }

    notesSaving = true;
    saveTimeout = setTimeout(() => {
      updateNotes(list.id, notesDraft);
      notesSaving = false;
      notesSavedAt = new Date().toLocaleTimeString();
    }, 600);
  };

  const handleNotesInput = (event: Event) => {
    const target = event.currentTarget as HTMLTextAreaElement;
    notesDraft = target.value;
    scheduleNotesSave();
  };

  const handleQuantityChange = (itemId: string, event: Event) => {
    if (!list) {
      return;
    }

    const target = event.currentTarget as HTMLInputElement;
    const value = Number.parseInt(target.value, 10);
    if (Number.isNaN(value) || value <= 0) {
      target.value = String(list.items.find((item) => item.id === itemId)?.quantity ?? 1);
      return;
    }

    updateItemQuantity(list.id, itemId, value);
  };

  const handleRemove = (itemId: string) => {
    if (!list) {
      return;
    }

    const confirmed = window.confirm('Remove this item from the list?');
    if (!confirmed) {
      return;
    }

    removeItem(list.id, itemId);
  };

  const handleCopy = async (value: string) => {
    if (!browser) {
      return;
    }

    try {
      await navigator.clipboard.writeText(value);
      copiedPart = value;
      if (copiedTimeout) {
        clearTimeout(copiedTimeout);
      }
      copiedTimeout = setTimeout(() => {
        copiedPart = null;
      }, 1500);
    } catch (error) {
      console.error('Failed to copy part number:', error);
    }
  };

  const handlePrint = () => {
    if (!browser) {
      return;
    }

    window.print();
  };

  const handleExport = () => {
    if (!browser || !list) {
      return;
    }

    const exportData = {
      exportedAt: new Date().toISOString(),
      version: '1.0',
      list
    };

    const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `${list.name.replace(/[^a-z0-9]/gi, '-')}-${Date.now()}.json`;
    anchor.click();
    URL.revokeObjectURL(url);
  };

  $effect(() => {
    if (listId) {
      setActiveList(listId);
    }
  });

  $effect(() => {
    notesDraft = list?.notes ?? '';
  });
</script>

<svelte:head>
  <title>{list ? `${list.name} · Parts List` : 'Parts List'} | BMW ETKx</title>
</svelte:head>

<section class="mx-auto w-full max-w-7xl space-y-8 px-4 py-8 md:px-6">
  {#if !list}
    <div class="rounded-2xl border border-dashed border-slate-200 bg-slate-50 p-8 text-center text-slate-600 dark:border-slate-700 dark:bg-slate-900/40 dark:text-slate-300">
      <p class="text-lg font-semibold">Parts list not found.</p>
      <p class="mt-2 text-sm">It may have been deleted or not yet created.</p>
      <button
        type="button"
        class="mt-4 inline-flex items-center justify-center rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-500"
        onclick={() => goto('/lists')}
      >
        Back to lists
      </button>
    </div>
  {:else}
    <header class="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
      <div>
        <a
          href="/lists"
          class="no-print inline-flex items-center text-sm font-semibold text-blue-600 transition hover:text-blue-500 dark:text-blue-400"
        >
          ← Back to lists
        </a>
        <h1 class="mt-2 text-2xl font-semibold text-slate-900 dark:text-white">{list.name}</h1>
        <p class="mt-1 text-sm text-slate-500 dark:text-slate-400">
          Updated {formatDate(list.updatedAt)} · {list.items.length} {list.items.length === 1 ? 'item' : 'items'}
        </p>
      </div>

      <div class="no-print flex flex-wrap items-center gap-2">
        <button
          type="button"
          class="inline-flex items-center justify-center gap-2 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-blue-200 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
          onclick={handlePrint}
        >
          Print
        </button>
        <button
          type="button"
          class="inline-flex items-center justify-center gap-2 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm font-semibold text-slate-500 shadow-sm transition hover:border-blue-200 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-300"
          onclick={handleExport}
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
            <polyline points="7 10 12 15 17 10" />
            <line x1="12" y1="15" x2="12" y2="3" />
          </svg>
          Export
        </button>
      </div>
    </header>

    {#if copiedPart}
      <div class="no-print rounded-lg border border-blue-200 bg-blue-50 px-3 py-2 text-sm text-blue-700 dark:border-blue-500/30 dark:bg-blue-500/10 dark:text-blue-200">
        Copied {copiedPart} to clipboard.
      </div>
    {/if}

    <section class="space-y-4">
      <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Items</h2>

      {#if list.items.length === 0}
        <div class="rounded-2xl border border-dashed border-slate-200 bg-slate-50 p-6 text-center text-slate-600 dark:border-slate-700 dark:bg-slate-900/40 dark:text-slate-300">
          <p class="text-sm">No parts added yet.</p>
        </div>
      {:else}
        <div class="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm dark:border-slate-700 dark:bg-slate-900">
          <table class="min-w-full divide-y divide-slate-200 text-sm dark:divide-slate-700">
            <thead class="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500 dark:bg-slate-800 dark:text-slate-400">
              <tr>
                <th class="px-4 py-3">Part Number</th>
                <th class="px-4 py-3">Name</th>
                <th class="px-4 py-3">Quantity</th>
                <th class="px-4 py-3">Vehicle</th>
                <th class="px-4 py-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200 dark:divide-slate-700">
              {#each list.items as item (item.id)}
                <tr>
                  <td class="px-4 py-3 font-mono text-sm text-blue-600 dark:text-blue-400">
                    <button
                      type="button"
                      class="no-print underline-offset-2 transition hover:underline"
                      onclick={() => handleCopy(item.fullPartNumber)}
                    >
                      {item.fullPartNumber}
                    </button>
                    <span class="print-only font-mono">{item.fullPartNumber}</span>
                  </td>
                  <td class="px-4 py-3 text-slate-700 dark:text-slate-200">{item.name}</td>
                  <td class="px-4 py-3">
                    <input
                      type="number"
                      min="1"
                      class="no-print w-20 rounded-lg border border-slate-200 bg-white px-2 py-1 text-sm text-slate-700 shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
                      value={item.quantity}
                      onchange={(event) => handleQuantityChange(item.id, event)}
                    />
                    <span class="print-only">{item.quantity}</span>
                  </td>
                  <td class="px-4 py-3 text-slate-600 dark:text-slate-300">
                    {item.vehicle?.name ?? '—'}
                  </td>
                  <td class="px-4 py-3 text-right">
                    <button
                      type="button"
                      class="no-print inline-flex items-center rounded-lg border border-red-200 px-2.5 py-1.5 text-xs font-semibold text-red-600 transition hover:bg-red-50 dark:border-red-900/40 dark:text-red-400 dark:hover:bg-red-950/40"
                      onclick={() => handleRemove(item.id)}
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              {/each}
            </tbody>
          </table>
        </div>
      {/if}
    </section>

    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-slate-900 dark:text-white">Notes</h2>
        {#if notesSaving}
          <span class="no-print text-xs text-slate-500 dark:text-slate-400">Saving...</span>
        {:else if notesSavedAt}
          <span class="no-print text-xs text-slate-500 dark:text-slate-400">Saved {notesSavedAt}</span>
        {/if}
      </div>
      <textarea
        class="no-print min-h-[140px] w-full rounded-2xl border border-slate-200 bg-white p-4 text-sm text-slate-700 shadow-sm transition focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:focus:border-blue-400 dark:focus:ring-blue-900/40"
        placeholder="Add notes about this list..."
        value={notesDraft}
        oninput={handleNotesInput}
      ></textarea>

      <div class="print-only whitespace-pre-wrap rounded-2xl border border-slate-200 bg-white p-4 text-sm text-slate-700">
        {notesDraft || 'No notes added.'}
      </div>
    </section>

    <section class="print-only text-xs text-slate-500">
      Printed on {new Date().toLocaleDateString()}
    </section>
  {/if}
</section>

<style>
  @media print {
    :global(body) {
      background: white;
    }

    .no-print {
      display: none !important;
    }

    .print-only {
      display: block !important;
    }

    table {
      page-break-inside: avoid;
    }
  }

  .print-only {
    display: none;
  }
</style>
