<script>
  const { parts = [] } = $props();
</script>

{#if parts.length === 0}
  <p>No parts found.</p>
{:else}
  <table>
    <thead>
      <tr>
        <th>Part number</th>
        <th>Name</th>
        <th>Main group</th>
        <th>Sub group</th>
      </tr>
    </thead>
    <tbody>
      {#each parts as part}
        <tr>
          <td>
            <a href={`/parts/${part.sachnr}`}>{part.sachnr}</a>
          </td>
          <td>{part.name ?? '-'}</td>
          <td>{part.mainGroup ?? '-'}</td>
          <td>{part.subGroup ?? '-'}</td>
        </tr>
      {/each}
    </tbody>
  </table>
{/if}

<style>
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

  a {
    color: #2563eb;
    text-decoration: none;
  }
</style>
