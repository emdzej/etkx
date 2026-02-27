import { getPartDetails, getPartUsage, getSupersession } from '$lib/api';

export const load = async ({ params, fetch }) => {
  const { sachnr } = params;
  const [details, usage, supersession] = await Promise.all([
    getPartDetails(sachnr, fetch),
    getPartUsage(sachnr, fetch),
    getSupersession(sachnr, 'table', fetch)
  ]);

  return { details, usage, supersession };
};
