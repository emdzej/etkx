<script>
  const { parts = [] } = $props();
</script>

<div class="parts">
  <h3>Parts list</h3>
  {#if parts.length === 0}
    <p class="empty">No parts found for this diagram.</p>
  {:else}
    <table>
      <thead>
        <tr>
          <th>Nr</th>
          <th>Part number</th>
          <th>Description</th>
          <th>Qty</th>
        </tr>
      </thead>
      <tbody>
        {#each parts as part}
          <tr>
            <td>{part.pos ?? '—'}</td>
            <td>{part.sachnr ?? '—'}</td>
            <td>{part.name ?? '—'}</td>
            <td>{part.quantity ?? '—'}</td>
          </tr>
        {/each}
      </tbody>
    </table>
  {/if}
</div>

<style>
  .parts {
    display: grid;
    gap: 0.75rem;
  }

  h3 {
    font-size: 1.1rem;
    font-weight: 600;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid #e2e8f0;
  }

  th,
  td {
    padding: 0.75rem 0.85rem;
    text-align: left;
    border-bottom: 1px solid #e2e8f0;
  }

  th {
    font-size: 0.85rem;
    text-transform: uppercase;
    letter-spacing: 0.04em;
    color: #64748b;
  }

  tbody tr:last-child td {
    border-bottom: none;
  }

  .empty {
    color: #64748b;
  }
</style>
