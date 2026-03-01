<script lang="ts">
  import type { PartSearchLine } from '$lib/api';

  let { part, mospId } = $props<{
    part: PartSearchLine;
    mospId?: string;
  }>();

  const href = $derived(
    mospId ? `/parts/${part.sachnummer}?mospId=${encodeURIComponent(mospId)}` : `/parts/${part.sachnummer}`
  );
</script>

<a
  href={href}
  class="flex h-full flex-col gap-3 rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md dark:border-slate-700 dark:bg-slate-900"
>
  <div class="flex items-start justify-between gap-2">
    <span class="text-sm font-semibold text-slate-900 dark:text-white">{part.sachnummer}</span>
    <div class="flex flex-wrap gap-1">
      <span class="rounded-full bg-slate-100 px-2 py-0.5 text-[10px] font-semibold uppercase text-slate-600 dark:bg-slate-800 dark:text-slate-300">
        HG {part.hauptgruppe}
      </span>
      <span class="rounded-full bg-slate-100 px-2 py-0.5 text-[10px] font-semibold uppercase text-slate-600 dark:bg-slate-800 dark:text-slate-300">
        UG {part.untergruppe}
      </span>
    </div>
  </div>
  <div>
    <p class="text-sm font-medium text-slate-800 dark:text-slate-200">{part.benennung}</p>
    {#if part.zusatz}
      <p class="mt-1 text-xs text-slate-500 dark:text-slate-400">{part.zusatz}</p>
    {/if}
  </div>
  <div class="mt-auto text-xs text-slate-500 dark:text-slate-400">
    Diagram: <span class="font-mono">{part.btNummer}</span> · Pos {part.pos}
  </div>
</a>
