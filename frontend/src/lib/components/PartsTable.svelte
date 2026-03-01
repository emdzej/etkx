<script lang="ts">
  import { createEventDispatcher } from 'svelte';
  import { goto } from '$app/navigation';
  import type { DiagramLine } from '$lib/api';

  export let lines: DiagramLine[] = [];
  export let highlightedNr: string | null = null;

  const dispatch = createEventDispatcher<{ highlight: { bildnummer: string | null } }>();

  const setHighlight = (bildnummer: string | null) => {
    dispatch('highlight', { bildnummer });
  };

  const handlePartClick = (partNr: string, event: MouseEvent) => {
    event.stopPropagation();
    void goto(`/parts/${partNr}`);
  };
</script>

<div class="overflow-hidden rounded-lg border border-slate-200 dark:border-slate-700">
  <div class="max-h-[640px] overflow-auto">
    <table class="min-w-full text-left text-sm">
      <thead class="sticky top-0 bg-slate-100 text-xs uppercase text-slate-500 dark:bg-slate-900 dark:text-slate-400">
        <tr>
          <th class="px-3 py-2">Nr</th>
          <th class="px-3 py-2">Description</th>
          <th class="px-3 py-2">Supplement</th>
          <th class="px-3 py-2">Qty</th>
          <th class="px-3 py-2">From</th>
          <th class="px-3 py-2">To</th>
          <th class="px-3 py-2">Part Number</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-slate-200 text-slate-700 dark:divide-slate-800 dark:text-slate-200">
        {#if lines.length === 0}
          <tr>
            <td class="px-3 py-4 text-center text-slate-500 dark:text-slate-400" colspan="7">
              No parts found for this diagram.
            </td>
          </tr>
        {:else}
          {#each lines as line}
            <tr
              class="cursor-pointer odd:bg-slate-50 hover:bg-blue-50 dark:odd:bg-slate-900/40 dark:hover:bg-blue-900/30 {highlightedNr === line.bildnummer
                ? 'bg-blue-100 dark:bg-blue-900/60'
                : ''}"
              on:mouseenter={() => setHighlight(line.bildnummer)}
              on:mouseleave={() => setHighlight(null)}
            >
              <td class="px-3 py-2 font-mono text-xs text-slate-500 dark:text-slate-400">
                {line.bildnummer}
              </td>
              <td class="px-3 py-2 font-medium text-slate-800 dark:text-slate-100">
                {line.teilBenennung}
              </td>
              <td class="px-3 py-2 text-slate-500 dark:text-slate-400">{line.teilZusatz}</td>
              <td class="px-3 py-2">{line.menge}</td>
              <td class="px-3 py-2">{line.einsatz}</td>
              <td class="px-3 py-2">{line.auslauf}</td>
              <td class="px-3 py-2">
                <button
                  class="font-mono text-xs text-blue-600 hover:underline dark:text-blue-400"
                  on:click={(event) => handlePartClick(line.teilSachnummer, event)}
                >
                  {line.teilSachnummer}
                </button>
              </td>
            </tr>
          {/each}
        {/if}
      </tbody>
    </table>
  </div>
</div>
