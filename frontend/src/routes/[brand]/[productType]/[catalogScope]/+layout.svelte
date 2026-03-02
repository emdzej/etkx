<script lang="ts">
  import { page } from '$app/stores';
  import { goto } from '$app/navigation';
  import {
    type Brand,
    type ProductType,
    type CatalogScope,
    isValidBrand,
    isValidProductType,
    isValidCatalogScope
  } from '$lib/types/catalog';

  interface Props {
    children: import('svelte').Snippet;
  }

  const { children }: Props = $props();

  const brand = $derived($page.params.brand as Brand);
  const productType = $derived($page.params.productType as ProductType);
  const catalogScope = $derived($page.params.catalogScope as CatalogScope);

  // Validate params and redirect if invalid
  $effect(() => {
    if (!isValidBrand(brand) || !isValidProductType(productType) || !isValidCatalogScope(catalogScope)) {
      goto('/bmw/car/current/vehicles');
    }
  });
</script>

{#if isValidBrand(brand) && isValidProductType(productType) && isValidCatalogScope(catalogScope)}
  {@render children()}
{/if}
