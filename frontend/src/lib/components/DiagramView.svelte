<script>
  const { imageUrl, title = '' } = $props();
  let zoom = $state(1);

  const zoomIn = () => {
    zoom = Math.min(2.5, Number((zoom + 0.2).toFixed(1)));
  };

  const zoomOut = () => {
    zoom = Math.max(1, Number((zoom - 0.2).toFixed(1)));
  };
</script>

<div class="diagram">
  <div class="header">
    <h3>{title}</h3>
    <div class="controls">
      <button type="button" on:click={zoomOut} disabled={zoom <= 1}>-</button>
      <span>{zoom}×</span>
      <button type="button" on:click={zoomIn} disabled={zoom >= 2.5}>+</button>
    </div>
  </div>

  {#if imageUrl}
    <div class="viewport">
      <img src={imageUrl} alt={title} style={`transform: scale(${zoom})`} />
    </div>
  {:else}
    <p class="empty">No diagram image available.</p>
  {/if}
</div>

<style>
  .diagram {
    display: grid;
    gap: 0.75rem;
    background: #fff;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    padding: 1rem;
  }

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
  }

  h3 {
    font-size: 1.1rem;
    font-weight: 600;
  }

  .controls {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
  }

  button {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    border: 1px solid #cbd5f5;
    background: #f8fafc;
    font-weight: 600;
  }

  button:disabled {
    opacity: 0.5;
  }

  .viewport {
    max-height: 520px;
    overflow: auto;
    display: grid;
    place-items: center;
    background: #f8fafc;
    border-radius: 12px;
    padding: 1rem;
  }

  img {
    max-width: 100%;
    height: auto;
    transform-origin: center;
  }

  .empty {
    color: #64748b;
  }
</style>
