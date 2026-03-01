<script lang="ts">
  import { createEventDispatcher, onMount } from 'svelte';
  import panzoom from 'panzoom';
  import { getDiagramImageUrl, type DiagramHotspot } from '$lib/api';

  export let btnr: string;
  export let hotspots: DiagramHotspot[] = [];
  export let highlightedNr: string | null = null;

  const dispatch = createEventDispatcher<{ highlight: { bildnummer: string | null } }>();

  let panzoomEl: HTMLDivElement;
  let imageEl: HTMLImageElement;
  let imageLoaded = false;
  let imageWidth = 0;
  let imageHeight = 0;
  let renderedWidth = 0;
  let renderedHeight = 0;

  const updateImageSize = () => {
    if (!imageEl) return;
    imageWidth = imageEl.naturalWidth || imageWidth;
    imageHeight = imageEl.naturalHeight || imageHeight;
    renderedWidth = imageEl.clientWidth || imageWidth;
    renderedHeight = imageEl.clientHeight || imageHeight;
  };

  const getBounds = (hotspot: DiagramHotspot) => {
    const left = Number(hotspot.topLeftX) || 0;
    const top = Number(hotspot.topLeftY) || 0;
    const right = Number(hotspot.bottomRightX) || 0;
    const bottom = Number(hotspot.bottomRightY) || 0;
    const scaleX = imageWidth ? renderedWidth / imageWidth : 1;
    const scaleY = imageHeight ? renderedHeight / imageHeight : 1;

    return {
      left: left * scaleX,
      top: top * scaleY,
      width: Math.max(0, (right - left) * scaleX),
      height: Math.max(0, (bottom - top) * scaleY)
    };
  };

  const setHighlight = (bildnummer: string | null) => {
    dispatch('highlight', { bildnummer });
  };

  onMount(() => {
    const instance = panzoom(panzoomEl, { maxZoom: 5, minZoom: 0.5 });
    const handleResize = () => updateImageSize();
    window.addEventListener('resize', handleResize);

    return () => {
      instance.dispose();
      window.removeEventListener('resize', handleResize);
    };
  });
</script>

<div
  class="relative min-h-[420px] w-full overflow-auto rounded-lg border border-slate-200 bg-slate-50 dark:border-slate-700 dark:bg-slate-900"
>
  <div
    bind:this={panzoomEl}
    class="relative origin-top-left"
  >
    <img
      bind:this={imageEl}
      src={getDiagramImageUrl(btnr)}
      alt={`Diagram ${btnr}`}
      class="block max-w-full select-none"
      on:load={() => {
        imageLoaded = true;
        updateImageSize();
      }}
      draggable="false"
    />

    {#if imageLoaded}
      {#each hotspots as hotspot}
        {@const bounds = getBounds(hotspot)}
        <button
          type="button"
          aria-label={`Hotspot ${hotspot.bildnummer}`}
          class="absolute cursor-pointer border border-transparent bg-blue-500/10 p-0 transition hover:border-blue-400 hover:bg-blue-400/20 {highlightedNr === hotspot.bildnummer
            ? 'border-blue-500 bg-blue-500/20'
            : ''}"
          style={`left:${bounds.left}px; top:${bounds.top}px; width:${bounds.width}px; height:${bounds.height}px;`}
          on:mouseenter={() => setHighlight(hotspot.bildnummer)}
          on:mouseleave={() => setHighlight(null)}
          on:focus={() => setHighlight(hotspot.bildnummer)}
          on:blur={() => setHighlight(null)}
          on:click={() => setHighlight(hotspot.bildnummer)}
        ></button>
      {/each}
    {/if}
  </div>
</div>
