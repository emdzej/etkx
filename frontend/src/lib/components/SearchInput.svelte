<script lang="ts">
  import { goto } from '$app/navigation';

  let {
    value = $bindable(''),
    placeholder = 'Search parts...',
    oninput,
    onsubmit
  } = $props<{
    value?: string;
    placeholder?: string;
    oninput?: (value: string) => void;
    onsubmit?: (value: string) => void;
  }>();

  const handleSubmit = (event: SubmitEvent) => {
    event.preventDefault();
    const trimmed = value.trim();
    if (!trimmed) {
      return;
    }
    if (onsubmit) {
      onsubmit(trimmed);
      return;
    }
    void goto(`/search?q=${encodeURIComponent(trimmed)}`);
  };

  const handleInput = (event: Event) => {
    const input = event.target as HTMLInputElement;
    value = input.value;
    oninput?.(value);
  };
</script>

<form class="relative w-full" onsubmit={handleSubmit}>
  <span class="pointer-events-none absolute left-3 top-1/2 -translate-y-0.5 text-slate-400">
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      stroke-width="2"
      stroke-linecap="round"
      stroke-linejoin="round"
      class="h-4 w-4"
    >
      <circle cx="11" cy="11" r="8" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
  </span>
  <input
    type="search"
    {placeholder}
    class="w-full rounded-lg border border-slate-200 bg-white py-2 pl-10 pr-3 text-sm text-slate-700 shadow-sm transition focus:border-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:placeholder:text-slate-400 dark:focus:border-blue-500 dark:focus:ring-blue-500/40"
    bind:value
    oninput={handleInput}
  />
</form>
