// Context types for catalog scope
export type Brand = 'bmw' | 'mini' | 'rolls-royce';
export type ProductType = 'car' | 'motorcycle';
export type CatalogScope = 'current' | 'classic';

export interface CatalogContext {
  brand: Brand;
  productType: ProductType;
  catalogScope: CatalogScope;
}

// URL to API value mappings
export const brandToApi: Record<Brand, string> = {
  'bmw': 'BMW',
  'mini': 'Mini',
  'rolls-royce': 'Rolls-Royce'
};

export const productTypeToApi: Record<ProductType, string> = {
  'car': 'P',
  'motorcycle': 'M'
};

export const catalogScopeToApi: Record<CatalogScope, string> = {
  'current': 'BE',  // BE shows all (VT + ST)
  'classic': 'ST'   // ST shows only classic
};

// Display labels
export const brandLabels: Record<Brand, string> = {
  'bmw': 'BMW',
  'mini': 'MINI',
  'rolls-royce': 'Rolls-Royce'
};

export const productTypeLabels: Record<ProductType, string> = {
  'car': 'Car',
  'motorcycle': 'Motorcycle'
};

export const catalogScopeLabels: Record<CatalogScope, string> = {
  'current': 'Current',
  'classic': 'Classic'
};

// All valid values
export const brands: Brand[] = ['bmw', 'mini', 'rolls-royce'];
export const productTypes: ProductType[] = ['car', 'motorcycle'];
export const catalogScopes: CatalogScope[] = ['current', 'classic'];

// Validation
export const isValidBrand = (value: string): value is Brand => brands.includes(value as Brand);
export const isValidProductType = (value: string): value is ProductType => productTypes.includes(value as ProductType);
export const isValidCatalogScope = (value: string): value is CatalogScope => catalogScopes.includes(value as CatalogScope);
