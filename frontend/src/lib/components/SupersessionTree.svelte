<script>
  const { supersession } = $props();
</script>

<section class="supersession-tree">
  <h3>Supersession (tree)</h3>
  {#if !supersession?.chain?.length}
    <p class="empty">No supersession data found.</p>
  {:else}
    <ol>
      {#each supersession.chain as item}
        <li>
          <strong>{item.sachnr}</strong>
          {#if item.description}
            <span> — {item.description}</span>
          {/if}
          {#if item.expiration}
            <span class="meta"> (expires {item.expiration})</span>
          {/if}
        </li>
      {/each}
    </ol>
  {/if}
</section>

<style>
  .supersession-tree {
    display: grid;
    gap: 0.75rem;
  }

  ol {
    padding-left: 1.5rem;
    margin: 0;
  }

  li + li {
    margin-top: 0.35rem;
  }

  .meta {
    color: #64748b;
  }

  .empty {
    color: #64748b;
  }
</style>
