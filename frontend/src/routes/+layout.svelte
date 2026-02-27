<script>
  import '../app.css';
</script>

<div class="min-h-screen">
  <header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-5xl items-center justify-between px-6 py-4">
      <div class="flex items-center gap-4">
        <h1 class="text-lg font-semibold">ETKx</h1>
        <nav class="flex items-center gap-3 text-sm">
          <a href="/">Home</a>
          <a href="/list">Parts list</a>
        </nav>
      </div>
      <span class="text-sm text-slate-500">Phase 1 Foundation</span>
    </div>
  </header>

  <main class="mx-auto max-w-5xl px-6 py-10">
    <slot />
  </main>
</div>
