<script>
  import PartDetails from '$lib/components/PartDetails.svelte';

  const { data } = $props();
</script>

<svelte:head>
  <title>Part {data.details.sachnr}</title>
</svelte:head>

<PartDetails details={data.details} usage={data.usage} />
