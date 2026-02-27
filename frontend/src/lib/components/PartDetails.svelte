<script>
  import SupersessionTable from '$lib/components/SupersessionTable.svelte';
  import SupersessionTree from '$lib/components/SupersessionTree.svelte';
  import { addPart } from '$lib/stores/PartsListStore';

  const { details, usage = [], supersession } = $props();

  let quantity = $state(1);
  let view = $state('table');
</script>

<section class="details">
  <div class="details-header">
    <h2>Part {details.sachnr}</h2>
    <div class="actions">
      <input type="number" min="1" bind:value={quantity} />
      <button type="button" on:click={() => addPart(details.sachnr, quantity)}>
        Add to list
      </button>
    </div>
  </div>
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

<section class="supersession">
  <div class="supersession-header">
    <h3>Supersession</h3>
    <div class="view-toggle">
      <button
        type="button"
        class:active={view === 'table'}
        on:click={() => (view = 'table')}
      >
        Table
      </button>
      <button
        type="button"
        class:active={view === 'tree'}
        on:click={() => (view = 'tree')}
      >
        Tree
      </button>
    </div>
  </div>
  {#if view === 'tree'}
    <SupersessionTree supersession={supersession} />
  {:else}
    <SupersessionTable supersession={supersession} />
  {/if}
</section>

<style>
  .details {
    margin-bottom: 2rem;
  }

  .details-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
    flex-wrap: wrap;
  }

  .actions {
    display: flex;
    gap: 0.5rem;
    align-items: center;
  }

  .actions input {
    width: 4.5rem;
    padding: 0.35rem 0.5rem;
  }

  .actions button {
    padding: 0.5rem 0.85rem;
    border-radius: 999px;
    border: 1px solid #cbd5f5;
    background: #fff;
    cursor: pointer;
  }

  .supersession {
    margin-top: 2rem;
    display: grid;
    gap: 0.75rem;
  }

  .supersession-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
  }

  .view-toggle {
    display: flex;
    gap: 0.5rem;
  }

  .view-toggle button {
    padding: 0.35rem 0.75rem;
    border-radius: 999px;
    border: 1px solid #cbd5f5;
    background: #fff;
    cursor: pointer;
  }

  .view-toggle button.active {
    background: #0f172a;
    color: #fff;
    border-color: #0f172a;
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
