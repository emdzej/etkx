<script lang="ts">
  import { goto } from '$app/navigation';
  import {
    getBodies,
    getModels,
    getMonths,
    getRegions,
    getSeries,
    getSteerings,
    getTransmissions,
    getYears,
    resolveMospId
  } from '$lib/api';
  import { myVehicles } from '$lib/stores/myVehicles';
  import CascadingSelect from './CascadingSelect.svelte';

  const DEFAULT_BRAND = 'BMW';
  const DEFAULT_SCOPE = 'BE';
  const DEFAULT_REGIONS = ['ECE'];
  const DEFAULT_ISO = 'EN';
  const DEFAULT_REGISO = 'US';

  let product = $state('P');
  let series = $state('');
  let body = $state('');
  let model = $state('');
  let region = $state('');
  let steering = $state('');
  let transmission = $state('');
  let year = $state('');
  let month = $state('');
  let mospId = $state('');

  let seriesOptions = $state<{ value: string; label: string }[]>([]);
  let bodyOptions = $state<{ value: string; label: string }[]>([]);
  let modelOptions = $state<{ value: string; label: string }[]>([]);
  let regionOptions = $state<{ value: string; label: string }[]>([]);
  let steeringOptions = $state<{ value: string; label: string }[]>([]);
  let transmissionOptions = $state<{ value: string; label: string }[]>([]);
  let yearOptions = $state<{ value: string; label: string }[]>([]);
  let monthOptions = $state<{ value: string; label: string }[]>([]);

  let loadingSeries = $state(false);
  let loadingBody = $state(false);
  let loadingModel = $state(false);
  let loadingRegion = $state(false);
  let loadingSteering = $state(false);
  let loadingTransmission = $state(false);
  let loadingYear = $state(false);
  let loadingMonth = $state(false);
  let loadingMospId = $state(false);

  let errorMessage = $state<string | null>(null);

  const isMotorcycle = () => product === 'M';

  const getLabel = (options: { value: string; label: string }[], value: string) =>
    options.find((opt) => opt.value === value)?.label || value;

  const resetAfterSeries = () => {
    body = '';
    model = '';
    region = '';
    steering = '';
    transmission = '';
    year = '';
    month = '';
    mospId = '';
    bodyOptions = [];
    modelOptions = [];
    regionOptions = [];
    steeringOptions = [];
    transmissionOptions = [];
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterBody = () => {
    model = '';
    region = '';
    steering = '';
    transmission = '';
    year = '';
    month = '';
    mospId = '';
    modelOptions = [];
    regionOptions = [];
    steeringOptions = [];
    transmissionOptions = [];
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterModel = () => {
    region = '';
    steering = '';
    transmission = '';
    year = '';
    month = '';
    mospId = '';
    regionOptions = [];
    steeringOptions = [];
    transmissionOptions = [];
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterRegion = () => {
    steering = '';
    transmission = '';
    year = '';
    month = '';
    mospId = '';
    steeringOptions = [];
    transmissionOptions = [];
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterSteering = () => {
    transmission = '';
    year = '';
    month = '';
    mospId = '';
    transmissionOptions = [];
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterTransmission = () => {
    year = '';
    month = '';
    mospId = '';
    yearOptions = [];
    monthOptions = [];
  };

  const resetAfterYear = () => {
    month = '';
    mospId = '';
    monthOptions = [];
  };

  const loadSeries = async () => {
    loadingSeries = true;
    errorMessage = null;
    try {
      const result = await getSeries({
        marke: DEFAULT_BRAND,
        produktart: product,
        katalogumfang: DEFAULT_SCOPE,
        regionen: DEFAULT_REGIONS,
        iso: DEFAULT_ISO,
        regiso: DEFAULT_REGISO
      });
      seriesOptions = result.map((s) => ({
        value: s.baureihe,
        label: s.extBaureihe || s.baureihe
      }));
    } catch (e) {
      console.error('Failed to load series:', e);
      errorMessage = 'Failed to load series.';
      seriesOptions = [];
    } finally {
      loadingSeries = false;
    }
  };

  const loadBodies = async () => {
    if (!series || isMotorcycle()) {
      bodyOptions = [];
      return;
    }
    loadingBody = true;
    errorMessage = null;
    try {
      const result = await getBodies(series, DEFAULT_SCOPE, DEFAULT_REGIONS, DEFAULT_ISO, DEFAULT_REGISO);
      bodyOptions = result.map((entry) => ({
        value: entry.karosserie,
        label: entry.extKarosserie || entry.karosserie
      }));
    } catch (e) {
      console.error('Failed to load bodies:', e);
      errorMessage = 'Failed to load body types.';
      bodyOptions = [];
    } finally {
      loadingBody = false;
    }
  };

  const loadModels = async () => {
    if (!series || (!isMotorcycle() && !body)) {
      modelOptions = [];
      return;
    }
    loadingModel = true;
    errorMessage = null;
    try {
      const result = await getModels(series, DEFAULT_SCOPE, DEFAULT_REGIONS, isMotorcycle() ? undefined : body);
      modelOptions = result.map((entry) => ({
        value: entry.modell,
        label: entry.modell
      }));
    } catch (e) {
      console.error('Failed to load models:', e);
      errorMessage = 'Failed to load models.';
      modelOptions = [];
    } finally {
      loadingModel = false;
    }
  };

  const loadRegions = async () => {
    if (!series || !model) {
      regionOptions = [];
      return;
    }
    loadingRegion = true;
    errorMessage = null;
    try {
      const result = await getRegions(
        series,
        DEFAULT_SCOPE,
        model,
        DEFAULT_REGIONS,
        isMotorcycle() ? undefined : body
      );
      regionOptions = result.map((entry) => ({
        value: entry.region,
        label: entry.region
      }));
    } catch (e) {
      console.error('Failed to load regions:', e);
      errorMessage = 'Failed to load regions.';
      regionOptions = [];
    } finally {
      loadingRegion = false;
    }
  };

  const loadSteerings = async () => {
    if (isMotorcycle() || !series || !body || !model || !region) {
      steeringOptions = [];
      return;
    }
    loadingSteering = true;
    errorMessage = null;
    try {
      const result = await getSteerings(series, DEFAULT_SCOPE, body, model, region, DEFAULT_ISO, DEFAULT_REGISO);
      steeringOptions = result.map((entry) => ({
        value: entry.lenkung,
        label: entry.extLenkung || entry.lenkung
      }));
    } catch (e) {
      console.error('Failed to load steerings:', e);
      errorMessage = 'Failed to load steering options.';
      steeringOptions = [];
    } finally {
      loadingSteering = false;
    }
  };

  const loadTransmissions = async () => {
    if (isMotorcycle() || !series || !body || !model || !region) {
      transmissionOptions = [];
      return;
    }
    loadingTransmission = true;
    errorMessage = null;
    try {
      const result = await getTransmissions(
        series,
        DEFAULT_SCOPE,
        body,
        model,
        region,
        DEFAULT_ISO,
        DEFAULT_REGISO,
        steering || undefined
      );
      transmissionOptions = result.map((entry) => ({
        value: entry.getriebe,
        label: entry.extGetriebe || entry.getriebe
      }));
    } catch (e) {
      console.error('Failed to load transmissions:', e);
      errorMessage = 'Failed to load transmissions.';
      transmissionOptions = [];
    } finally {
      loadingTransmission = false;
    }
  };

  const loadYears = async () => {
    if (!series || !model || !region) {
      yearOptions = [];
      return;
    }
    loadingYear = true;
    errorMessage = null;
    try {
      const result = await getYears(
        series,
        DEFAULT_SCOPE,
        model,
        region,
        isMotorcycle() ? undefined : body,
        isMotorcycle() ? undefined : steering || undefined,
        isMotorcycle() ? undefined : transmission || undefined
      );
      yearOptions = result.map((entry) => ({
        value: entry.baujahr,
        label: entry.baujahr
      }));
    } catch (e) {
      console.error('Failed to load years:', e);
      errorMessage = 'Failed to load production years.';
      yearOptions = [];
    } finally {
      loadingYear = false;
    }
  };

  const loadMonths = async () => {
    if (!series || !model || !region || !year) {
      monthOptions = [];
      return;
    }
    loadingMonth = true;
    errorMessage = null;
    try {
      const result = await getMonths(
        series,
        DEFAULT_SCOPE,
        model,
        region,
        year,
        DEFAULT_ISO,
        DEFAULT_REGISO,
        isMotorcycle() ? undefined : body,
        isMotorcycle() ? undefined : steering || undefined,
        isMotorcycle() ? undefined : transmission || undefined
      );
      monthOptions = result.map((entry) => ({
        value: entry.zulassungsmonat,
        label: entry.extZulassungsmonat || entry.zulassungsmonat
      }));
    } catch (e) {
      console.error('Failed to load months:', e);
      errorMessage = 'Failed to load months.';
      monthOptions = [];
    } finally {
      loadingMonth = false;
    }
  };

  const loadMospId = async () => {
    if (!series || !model || !region || !month) {
      mospId = '';
      return;
    }
    loadingMospId = true;
    errorMessage = null;
    try {
      const result = await resolveMospId(
        series,
        model,
        region,
        isMotorcycle() ? undefined : body,
        product
      );
      mospId = result[0]?.mospId ?? '';
    } catch (e) {
      console.error('Failed to resolve mospId:', e);
      errorMessage = 'Failed to resolve model identifier.';
      mospId = '';
    } finally {
      loadingMospId = false;
    }
  };

  const handleProductChange = (value: string) => {
    product = value;
    series = '';
    seriesOptions = [];
    resetAfterSeries();
    loadSeries();
  };

  const handleSeriesChange = (value: string) => {
    series = value;
    resetAfterSeries();
    if (isMotorcycle()) {
      loadModels();
    } else {
      loadBodies();
    }
  };

  const handleBodyChange = (value: string) => {
    body = value;
    resetAfterBody();
    loadModels();
  };

  const handleModelChange = (value: string) => {
    model = value;
    resetAfterModel();
    loadRegions();
  };

  const handleRegionChange = (value: string) => {
    region = value;
    resetAfterRegion();
    if (isMotorcycle()) {
      loadYears();
    } else {
      loadSteerings();
    }
  };

  const handleSteeringChange = (value: string) => {
    steering = value;
    resetAfterSteering();
    loadTransmissions();
  };

  const handleTransmissionChange = (value: string) => {
    transmission = value;
    resetAfterTransmission();
    loadYears();
  };

  const handleYearChange = (value: string) => {
    year = value;
    resetAfterYear();
    loadMonths();
  };

  const handleMonthChange = (value: string) => {
    month = value;
    mospId = '';
    loadMospId();
  };

  $effect(() => {
    loadSeries();
  });

  const datum = $derived(year && month ? `${year}-${month.padStart(2, '0')}-01` : '');

  const saveAndNavigate = () => {
    if (!mospId || !datum) return;

    const seriesLabel = getLabel(seriesOptions, series);
    const modelLabel = getLabel(modelOptions, model);
    const regionLabel = getLabel(regionOptions, region);

    const labelParts = [DEFAULT_BRAND, seriesLabel, modelLabel, regionLabel, `${year}-${month}`];

    myVehicles.add({
      mospId,
      datum,
      label: labelParts.filter(Boolean).join(' '),
      series,
      model: modelLabel,
      region: regionLabel,
      addedAt: Date.now()
    });

    goto(`/vehicles/${mospId}?datum=${datum}`);
  };
</script>

<div class="space-y-4">
  <div class="grid gap-4 sm:grid-cols-2">
    <CascadingSelect
      label="Product"
      options={[
        { value: 'P', label: 'Car' },
        { value: 'M', label: 'Motorcycle' }
      ]}
      bind:value={product}
      onchange={handleProductChange}
    />

    <CascadingSelect
      label="Series"
      options={seriesOptions}
      bind:value={series}
      loading={loadingSeries}
      disabled={loadingSeries}
      placeholder="Select series..."
      onchange={handleSeriesChange}
    />

    {#if !isMotorcycle()}
      <CascadingSelect
        label="Body"
        options={bodyOptions}
        bind:value={body}
        loading={loadingBody}
        disabled={!series || loadingBody}
        placeholder="Select body..."
        onchange={handleBodyChange}
      />
    {/if}

    <CascadingSelect
      label="Model"
      options={modelOptions}
      bind:value={model}
      loading={loadingModel}
      disabled={!series || (!isMotorcycle() && !body) || loadingModel}
      placeholder="Select model..."
      onchange={handleModelChange}
    />

    <CascadingSelect
      label="Region"
      options={regionOptions}
      bind:value={region}
      loading={loadingRegion}
      disabled={!model || loadingRegion}
      placeholder="Select region..."
      onchange={handleRegionChange}
    />

    {#if !isMotorcycle()}
      <CascadingSelect
        label="Steering"
        options={steeringOptions}
        bind:value={steering}
        loading={loadingSteering}
        disabled={!region || loadingSteering}
        placeholder="Select steering..."
        onchange={handleSteeringChange}
      />

      <CascadingSelect
        label="Transmission"
        options={transmissionOptions}
        bind:value={transmission}
        loading={loadingTransmission}
        disabled={!steering || loadingTransmission}
        placeholder="Select transmission..."
        onchange={handleTransmissionChange}
      />
    {/if}

    <CascadingSelect
      label="Year"
      options={yearOptions}
      bind:value={year}
      loading={loadingYear}
      disabled={(!isMotorcycle() && !transmission) || (!region && isMotorcycle()) || loadingYear}
      placeholder="Select year..."
      onchange={handleYearChange}
    />

    <CascadingSelect
      label="Month"
      options={monthOptions}
      bind:value={month}
      loading={loadingMonth}
      disabled={!year || loadingMonth}
      placeholder="Select month..."
      onchange={handleMonthChange}
    />
  </div>

  {#if errorMessage}
    <p class="text-sm text-red-600 dark:text-red-400">{errorMessage}</p>
  {/if}

  {#if mospId && datum}
    <div class="rounded-xl border border-slate-200 bg-slate-50 p-4 dark:border-slate-700 dark:bg-slate-800/50">
      <h3 class="text-sm font-semibold text-slate-900 dark:text-white">Selected Vehicle</h3>
      <dl class="mt-3 grid grid-cols-2 gap-x-4 gap-y-2 text-sm">
        <dt class="text-slate-500 dark:text-slate-400">Series</dt>
        <dd class="font-medium text-slate-900 dark:text-white">{getLabel(seriesOptions, series)}</dd>
        <dt class="text-slate-500 dark:text-slate-400">Model</dt>
        <dd class="font-medium text-slate-900 dark:text-white">{getLabel(modelOptions, model)}</dd>
        <dt class="text-slate-500 dark:text-slate-400">Region</dt>
        <dd class="font-medium text-slate-900 dark:text-white">{getLabel(regionOptions, region)}</dd>
        <dt class="text-slate-500 dark:text-slate-400">Production Date</dt>
        <dd class="font-medium text-slate-900 dark:text-white">{datum}</dd>
      </dl>
      <button
        onclick={saveAndNavigate}
        class="mt-4 w-full rounded-lg bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-green-700"
      >
        Save & Open Catalog
      </button>
    </div>
  {/if}
</div>
