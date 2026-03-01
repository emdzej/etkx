<script lang="ts">
  type Option = { value: string; label: string };
  
  let {
    label,
    options = [],
    value = $bindable(''),
    disabled = false,
    loading = false,
    placeholder = 'Select...',
    onchange
  } = $props<{
    label: string;
    options: Option[];
    value?: string;
    disabled?: boolean;
    loading?: boolean;
    placeholder?: string;
    onchange?: (value: string) => void;
  }>();

  const handleChange = (e: Event) => {
    const select = e.target as HTMLSelectElement;
    value = select.value;
    onchange?.(value);
  };
  
  const id = `select-${label.toLowerCase().replace(/\s+/g, '-')}`;
</script>

<div class="flex flex-col gap-1">
  <label for={id} class="text-sm font-medium text-slate-600 dark:text-slate-400">{label}</label>
  <select
    {id}
    class="rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm transition focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 disabled:cursor-not-allowed disabled:opacity-50 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-200"
    {disabled}
    bind:value
    onchange={handleChange}
  >
    <option value="">{loading ? 'Loading...' : placeholder}</option>
    {#each options as opt (opt.value)}
      <option value={opt.value}>{opt.label}</option>
    {/each}
  </select>
</div>
