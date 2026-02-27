<script lang="ts">
  import '../app.css';
</script>

<div class="min-h-screen flex flex-col">
  <header class="bg-white border-b">
    <div class="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">ETKX</h1>
      <nav class="flex gap-4 text-sm text-gray-600">
        <a class="hover:text-gray-900" href="/">Home</a>
        <a class="hover:text-gray-900" href="/parts">Parts</a>
        <a class="hover:text-gray-900" href="/vehicles">Vehicles</a>
        <a class="hover:text-gray-900" href="/diagrams">Diagrams</a>
        <a class="hover:text-gray-900" href="/graphics">Graphics</a>
      </nav>
    </div>
  </header>

  <main class="flex-1">
    <div class="max-w-6xl mx-auto px-6 py-8">
      <slot />
    </div>
  </main>
</div>
