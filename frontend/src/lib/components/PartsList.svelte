<script>
  import { getPartDetails } from '$lib/api';
  import {
    partsList,
    clearList,
    exportList,
    removePart,
    updateQuantity
  } from '$lib/stores/PartsListStore';

  let details = $state({});
  let loading = $state(false);
  let error = $state('');
  let exportFormat = $state('csv');
  let exportContent = $state('');

  const loadDetails = async (items) => {
    if (!items.length) {
      details = {};
      return;
    }
    loading = true;
    error = '';
    try {
      const entries = await Promise.all(
        items.map(async (item) => {
          try {
            const detail = await getPartDetails(item.sachnr);
            return [item.sachnr, detail];
          } catch {
            return [item.sachnr, null];
          }
        })
      );
      details = Object.fromEntries(entries);
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load part details';
    } finally {
      loading = false;
    }
  };

  $effect(() => {
    loadDetails($partsList);
  });

  const handleExport = (format) => {
    exportFormat = format;
    exportContent = exportList(format);
  };
</script>

<section class="parts-list">
  <header>
    <div>
      <h2>Parts list</h2>
      <p>Keep a temporary list of parts for later reference.</p>
    </div>
    <div class="actions">
      <button type="button" on:click={() => clearList()}>Clear</button>
      <button type="button" on:click={() => handleExport('csv')}>Export CSV</button>
      <button type="button" on:click={() => handleExport('json')}>Export JSON</button>
    </div>
  </header>

  {#if $partsList.length === 0}
    <p class="empty">No parts in your list yet.</p>
  {:else}
    {#if loading}
      <p>Loading details...</p>
    {:else if error}
      <p class="error">{error}</p>
    {/if}
    <table>
      <thead>
        <tr>
          <th>Part number</th>
          <th>Description</th>
          <th>Quantity</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {#each $partsList as item}
          <tr>
            <td>{item.sachnr}</td>
            <td>{details[item.sachnr]?.name ?? '—'}</td>
            <td>
              <input
                type="number"
                min="1"
                value={item.quantity}
                on:change={(event) =>
                  updateQuantity(item.sachnr, Number(event.currentTarget.value) || 1)}
              />
            </td>
            <td>
              <button type="button" class="ghost" on:click={() => removePart(item.sachnr)}>
                Remove
              </button>
            </td>
          </tr>
        {/each}
      </tbody>
    </table>
  {/if}

  {#if exportContent}
    <div class="export">
      <h3>Export ({exportFormat.toUpperCase()})</h3>
      <textarea readonly rows="6">{exportContent}</textarea>
    </div>
  {/if}
</section>

<style>
  .parts-list {
    display: grid;
    gap: 1.5rem;
  }

  header {
    display: flex;
    justify-content: space-between;
    gap: 1rem;
    align-items: flex-start;
    flex-wrap: wrap;
  }

  .actions {
    display: flex;
    gap: 0.5rem;
    flex-wrap: wrap;
  }

  button {
    padding: 0.5rem 0.85rem;
    border-radius: 999px;
    border: 1px solid #cbd5f5;
    background: #fff;
    cursor: pointer;
  }

  button.ghost {
    border-color: transparent;
    color: #b91c1c;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    overflow: hidden;
  }

  th,
  td {
    padding: 0.75rem 0.85rem;
    text-align: left;
    border-bottom: 1px solid #e2e8f0;
  }

  tbody tr:last-child td {
    border-bottom: none;
  }

  input[type='number'] {
    width: 5rem;
    padding: 0.35rem 0.5rem;
  }

  .empty {
    color: #64748b;
  }

  .error {
    color: #b91c1c;
  }

  .export textarea {
    width: 100%;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    padding: 0.75rem;
  }
</style>
