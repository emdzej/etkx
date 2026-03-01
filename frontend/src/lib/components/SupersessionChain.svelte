<script lang="ts">
  import type { PartReplacement } from '$lib/api';

  type ReplacementNode = {
    partNumber: string;
    description: string;
    supplement: string;
    type: string;
  };

  let {
    current,
    replacements = []
  } = $props<{
    current: string;
    replacements?: PartReplacement[];
  }>();

  const previousParts = $derived<ReplacementNode[]>(
    replacements
      .filter((item: PartReplacement) => item.sachnummer === current)
      .map((item: PartReplacement) => ({
        partNumber: item.sachnummerAlt,
        description: item.benennung,
        supplement: item.zusatz,
        type: item.at
      }))
  );

  const nextParts = $derived<ReplacementNode[]>(
    replacements
      .filter((item: PartReplacement) => item.sachnummerAlt === current)
      .map((item: PartReplacement) => ({
        partNumber: item.sachnummer,
        description: item.benennung,
        supplement: item.zusatz,
        type: item.at
      }))
  );

  const hasData = $derived(previousParts.length > 0 || nextParts.length > 0);
</script>

<div class="space-y-3">
  {#if !hasData}
    <p class="text-sm text-slate-500 dark:text-slate-400">No supersession data for this part.</p>
  {:else}
    <div class="flex flex-wrap items-center gap-2">
      {#each previousParts as part (part.partNumber)}
        <a
          href={`/parts/${part.partNumber}`}
          class="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-xs font-semibold text-slate-700 shadow-sm transition hover:border-blue-300 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
        >
          <div class="text-[11px] uppercase text-slate-400">{part.type}</div>
          <div>{part.partNumber}</div>
          {#if part.description}
            <div class="text-[11px] font-normal text-slate-500 dark:text-slate-400">{part.description}</div>
          {/if}
        </a>
        <span class="text-slate-400">→</span>
      {/each}

      <div class="rounded-lg border border-blue-200 bg-blue-50 px-3 py-2 text-xs font-semibold text-blue-700 shadow-sm dark:border-blue-600/40 dark:bg-blue-500/10 dark:text-blue-200">
        <div class="text-[11px] uppercase text-blue-400">Current</div>
        <div>{current}</div>
      </div>

      {#each nextParts as part (part.partNumber)}
        <span class="text-slate-400">→</span>
        <a
          href={`/parts/${part.partNumber}`}
          class="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-xs font-semibold text-slate-700 shadow-sm transition hover:border-blue-300 hover:text-blue-600 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
        >
          <div class="text-[11px] uppercase text-slate-400">{part.type}</div>
          <div>{part.partNumber}</div>
          {#if part.description}
            <div class="text-[11px] font-normal text-slate-500 dark:text-slate-400">{part.description}</div>
          {/if}
        </a>
      {/each}
    </div>
  {/if}
</div>
