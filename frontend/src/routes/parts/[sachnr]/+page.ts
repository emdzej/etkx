import { getPartDetails, getPartUsage } from '$lib/api';

export const load = async ({ params, fetch }) => {
  const { sachnr } = params;
  const [details, usage] = await Promise.all([
    getPartDetails(sachnr, fetch),
    getPartUsage(sachnr, fetch)
  ]);

  return { details, usage };
};
