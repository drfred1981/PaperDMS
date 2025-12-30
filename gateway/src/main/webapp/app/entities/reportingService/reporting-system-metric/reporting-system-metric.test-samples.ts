import dayjs from 'dayjs/esm';

import { IReportingSystemMetric, NewReportingSystemMetric } from './reporting-system-metric.model';

export const sampleWithRequiredData: IReportingSystemMetric = {
  id: 13412,
  metricName: 'and',
  timestamp: dayjs('2025-12-29T22:16'),
};

export const sampleWithPartialData: IReportingSystemMetric = {
  id: 3917,
  metricName: 'low',
  memoryUsage: 9000.76,
  networkIn: 16519,
  networkOut: 11290,
  timestamp: dayjs('2025-12-29T14:30'),
};

export const sampleWithFullData: IReportingSystemMetric = {
  id: 30399,
  metricName: 'per for',
  cpuUsage: 21244.84,
  memoryUsage: 14916.62,
  diskUsage: 2656.88,
  networkIn: 4944,
  networkOut: 32065,
  activeConnections: 12651,
  timestamp: dayjs('2025-12-29T23:32'),
};

export const sampleWithNewData: NewReportingSystemMetric = {
  metricName: 'why wrongly um',
  timestamp: dayjs('2025-12-29T08:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
