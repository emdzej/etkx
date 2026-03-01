<script lang="ts">
  import type { PartUsageVehicle } from '$lib/api';

  let { usage = [] } = $props<{ usage?: PartUsageVehicle[] }>();
</script>

{#if usage.length === 0}
  <p class="text-sm text-slate-500 dark:text-slate-400">No vehicle usage data available.</p>
{:else}
  <div class="overflow-x-auto rounded-xl border border-slate-200 dark:border-slate-700">
    <table class="min-w-full divide-y divide-slate-200 text-sm dark:divide-slate-700">
      <thead class="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500 dark:bg-slate-900 dark:text-slate-400">
        <tr>
          <th class="px-4 py-3">Series</th>
          <th class="px-4 py-3">Model</th>
          <th class="px-4 py-3">Body</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-slate-100 bg-white dark:divide-slate-800 dark:bg-slate-950">
        {#each usage as entry (entry.baureihe + entry.modell + entry.karosserie)}
          <tr>
            <td class="px-4 py-3 font-medium text-slate-900 dark:text-slate-100">{entry.baureihe}</td>
            <td class="px-4 py-3 text-slate-700 dark:text-slate-200">{entry.modell}</td>
            <td class="px-4 py-3 text-slate-700 dark:text-slate-200">{entry.karosserie}</td>
          </tr>
        {/each}
      </tbody>
    </table>
  </div>
{/if}
