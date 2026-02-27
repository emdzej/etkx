<script>
  const { items = [], title = '', emptyMessage = 'No items available.' } = $props();
</script>

<section class="group-grid">
  {#if title}
    <h2>{title}</h2>
  {/if}

  {#if items.length === 0}
    <p class="empty">{emptyMessage}</p>
  {:else}
    <div class="grid">
      {#each items as item}
        <a class="card" href={item.href} aria-label={item.label}>
          <div class="thumb">
            {#if item.imageUrl}
              <img src={item.imageUrl} alt={item.label} />
            {:else}
              <div class="placeholder">{item.code}</div>
            {/if}
          </div>
          <div class="meta">
            <span class="code">{item.code}</span>
            <span class="label">{item.label}</span>
          </div>
        </a>
      {/each}
    </div>
  {/if}
</section>

<style>
  .group-grid h2 {
    font-size: 1.25rem;
    font-weight: 600;
    margin-bottom: 1rem;
  }

  .grid {
    display: grid;
    gap: 1rem;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }

  .card {
    display: grid;
    gap: 0.75rem;
    padding: 1rem;
    background: #fff;
    border-radius: 12px;
    border: 1px solid #e2e8f0;
    text-decoration: none;
    color: inherit;
    transition: transform 0.15s ease, box-shadow 0.15s ease;
  }

  .card:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
  }

  .thumb {
    height: 96px;
    display: grid;
    place-items: center;
    background: #f8fafc;
    border-radius: 10px;
    overflow: hidden;
  }

  img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
  }

  .placeholder {
    font-weight: 600;
    font-size: 1.25rem;
    color: #475569;
  }

  .meta {
    display: grid;
    gap: 0.25rem;
  }

  .code {
    font-size: 0.875rem;
    color: #64748b;
  }

  .label {
    font-weight: 600;
  }

  .empty {
    color: #64748b;
  }
</style>
