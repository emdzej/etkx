<script lang="ts">
  let { code, name, thumbnailId = null, href } = $props<{
    code: string;
    name: string;
    thumbnailId?: number | null;
    href: string;
  }>();

  const imageUrl = $derived(thumbnailId ? `/api/diagrams/graphics/${thumbnailId}/image` : null);
</script>

<a
  href={href}
  class="group flex h-full flex-col overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-md dark:border-slate-700 dark:bg-slate-900"
>
  <div class="aspect-[4/3] w-full bg-slate-100 p-2 dark:bg-slate-800">
    {#if imageUrl}
      <img src={imageUrl} alt={name} class="h-full w-full object-contain" loading="lazy" />
    {:else}
      <div class="flex h-full w-full items-center justify-center text-xs text-slate-400 dark:text-slate-500">
        No image
      </div>
    {/if}
  </div>
  <div class="flex flex-1 flex-col gap-0.5 p-2">
    <span class="text-xs font-medium text-slate-500 dark:text-slate-400">{code}</span>
    <span class="text-xs font-medium text-slate-900 dark:text-white">{name}</span>
  </div>
</a>
