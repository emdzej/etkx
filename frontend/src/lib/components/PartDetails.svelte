<script>
  const { details, usage = [] } = $props();
</script>

<section class="details">
  <h2>Part {details.sachnr}</h2>
  <dl>
    <div>
      <dt>Name</dt>
      <dd>{details.name ?? '-'}</dd>
    </div>
    <div>
      <dt>Main group</dt>
      <dd>{details.mainGroup ?? '-'}</dd>
    </div>
    <div>
      <dt>Sub group</dt>
      <dd>{details.subGroup ?? '-'}</dd>
    </div>
    <div>
      <dt>Text code</dt>
      <dd>{details.textCode ?? '-'}</dd>
    </div>
    <div>
      <dt>Description suffix</dt>
      <dd>{details.descriptionSuffix ?? '-'}</dd>
    </div>
    <div>
      <dt>Part type</dt>
      <dd>{details.partType ?? '-'}</dd>
    </div>
  </dl>
</section>

<section class="usage">
  <h3>Usage in diagrams</h3>
  {#if usage.length === 0}
    <p>No usage data found.</p>
  {:else}
    <table>
      <thead>
        <tr>
          <th>Diagram</th>
          <th>Position</th>
          <th>Main group</th>
          <th>Sub group</th>
        </tr>
      </thead>
      <tbody>
        {#each usage as entry}
          <tr>
            <td>{entry.diagramNumber ?? '-'}</td>
            <td>{entry.position ?? '-'}</td>
            <td>{entry.mainGroup ?? '-'}</td>
            <td>{entry.subGroup ?? '-'}</td>
          </tr>
        {/each}
      </tbody>
    </table>
  {/if}
</section>

<style>
  .details {
    margin-bottom: 2rem;
  }

  dl {
    display: grid;
    gap: 0.75rem;
  }

  dl div {
    display: grid;
    grid-template-columns: 12rem 1fr;
    gap: 0.5rem;
  }

  dt {
    font-weight: 600;
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
</style>
