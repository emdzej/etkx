<script>
  const { supersession } = $props();
</script>

<section class="supersession">
  <h3>Supersession (table)</h3>
  {#if !supersession?.chain?.length}
    <p class="empty">No supersession data found.</p>
  {:else}
    <table>
      <thead>
        <tr>
          <th>Nr</th>
          <th>Part number</th>
          <th>Description</th>
          <th>Expiration</th>
        </tr>
      </thead>
      <tbody>
        {#each supersession.chain as item}
          <tr>
            <td>{item.nr}</td>
            <td>{item.sachnr}</td>
            <td>{item.description ?? '—'}</td>
            <td>{item.expiration ?? '—'}</td>
          </tr>
        {/each}
      </tbody>
    </table>
  {/if}
</section>

<style>
  .supersession {
    display: grid;
    gap: 0.75rem;
  }

  table {
    width: 100%;
    border-collapse: collapse;
  }

  th,
  td {
    padding: 0.5rem 0.75rem;
    border-bottom: 1px solid #e5e7eb;
    text-align: left;
  }

  .empty {
    color: #64748b;
  }
</style>
