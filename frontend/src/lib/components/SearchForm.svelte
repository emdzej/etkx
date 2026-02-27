<script>
  const { onSearch } = $props();

  let query = $state('');
  let mainGroup = $state('');
  let subGroup = $state('');

  const submit = (event: SubmitEvent) => {
    event.preventDefault();
    onSearch?.({
      query: query.trim(),
      hg: mainGroup.trim(),
      fg: subGroup.trim()
    });
  };
</script>

<form class="search-form" on:submit={submit}>
  <label>
    Part number
    <input type="text" bind:value={query} placeholder="e.g. 1234567" />
  </label>
  <label>
    Main group (HG)
    <input type="text" bind:value={mainGroup} placeholder="e.g. 31" />
  </label>
  <label>
    Sub group (FG)
    <input type="text" bind:value={subGroup} placeholder="e.g. 15" />
  </label>
  <button type="submit">Search</button>
</form>

<style>
  .search-form {
    display: grid;
    gap: 0.75rem;
    max-width: 30rem;
  }

  label {
    display: grid;
    gap: 0.35rem;
    font-weight: 600;
  }

  input {
    padding: 0.5rem 0.75rem;
    border-radius: 6px;
    border: 1px solid #d0d7de;
  }

  button {
    justify-self: start;
    padding: 0.55rem 1.25rem;
    border-radius: 6px;
    border: none;
    background: #2563eb;
    color: #fff;
    cursor: pointer;
  }
</style>
