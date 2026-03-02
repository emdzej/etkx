<script lang="ts">
  import { page } from '$app/stores';

  interface Crumb {
    label: string;
    href?: string;
  }

  interface Props {
    crumbs: Crumb[];
  }

  const { crumbs }: Props = $props();

  // Preserve datum in URLs
  const datum = $derived($page.url.searchParams.get('datum'));
  const addDatum = (href: string) => datum ? `${href}?datum=${datum}` : href;
</script>

<nav aria-label="Breadcrumb" class="mb-4">
  <ol class="flex flex-wrap items-center gap-1 text-sm text-slate-500 dark:text-slate-400">
    {#each crumbs as crumb, i}
      {#if i > 0}
        <li class="select-none">/</li>
      {/if}
      <li>
        {#if crumb.href && i < crumbs.length - 1}
          <a
            href={addDatum(crumb.href)}
            class="hover:text-blue-600 dark:hover:text-blue-400 transition"
          >
            {crumb.label}
          </a>
        {:else}
          <span class="font-medium text-slate-700 dark:text-slate-200">{crumb.label}</span>
        {/if}
      </li>
    {/each}
  </ol>
</nav>
