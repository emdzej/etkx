<script lang="ts">
  import { goto } from '$app/navigation';
  import { page } from '$app/stores';
  import {
    type Brand,
    type ProductType,
    type CatalogScope,
    brands,
    productTypes,
    catalogScopes,
    brandLabels,
    productTypeLabels,
    catalogScopeLabels,
    isValidBrand,
    isValidProductType,
    isValidCatalogScope
  } from '$lib/types/catalog';

  const brand = $derived(
    isValidBrand($page.params.brand) ? ($page.params.brand as Brand) : 'bmw'
  );
  const productType = $derived(
    isValidProductType($page.params.productType) ? ($page.params.productType as ProductType) : 'car'
  );
  const catalogScope = $derived(
    isValidCatalogScope($page.params.catalogScope) ? ($page.params.catalogScope as CatalogScope) : 'current'
  );

  // Check if we're in a scoped route
  const isInScopedRoute = $derived(
    $page.params.brand && $page.params.productType && $page.params.catalogScope
  );

  const navigate = (newBrand: Brand, newProductType: ProductType, newCatalogScope: CatalogScope) => {
    goto(`/${newBrand}/${newProductType}/${newCatalogScope}/vehicles`);
  };

  const handleBrandChange = (event: Event) => {
    const value = (event.target as HTMLSelectElement).value as Brand;
    navigate(value, productType, catalogScope);
  };

  const handleProductTypeChange = (event: Event) => {
    const value = (event.target as HTMLSelectElement).value as ProductType;
    navigate(brand, value, catalogScope);
  };

  const handleCatalogScopeChange = (event: Event) => {
    const value = (event.target as HTMLSelectElement).value as CatalogScope;
    navigate(brand, productType, value);
  };
</script>

{#if isInScopedRoute}
  <div class="flex items-center gap-2">
    <select
      value={brand}
      onchange={handleBrandChange}
      class="rounded-lg border border-slate-200 bg-white px-2 py-1.5 text-sm font-medium text-slate-700 shadow-sm transition hover:border-slate-300 focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100"
    >
      {#each brands as b}
        <option value={b}>{brandLabels[b]}</option>
      {/each}
    </select>

    <select
      value={productType}
      onchange={handleProductTypeChange}
      class="rounded-lg border border-slate-200 bg-white px-2 py-1.5 text-sm font-medium text-slate-700 shadow-sm transition hover:border-slate-300 focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100"
    >
      {#each productTypes as pt}
        <option value={pt}>{productTypeLabels[pt]}</option>
      {/each}
    </select>

    <select
      value={catalogScope}
      onchange={handleCatalogScopeChange}
      class="rounded-lg border border-slate-200 bg-white px-2 py-1.5 text-sm font-medium text-slate-700 shadow-sm transition hover:border-slate-300 focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100"
    >
      {#each catalogScopes as cs}
        <option value={cs}>{catalogScopeLabels[cs]}</option>
      {/each}
    </select>
  </div>
{/if}
