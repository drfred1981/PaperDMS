import dayjs from 'dayjs/esm';

import { IPerformanceMetric, NewPerformanceMetric } from './performance-metric.model';

export const sampleWithRequiredData: IPerformanceMetric = {
  id: 2184,
  metricName: 'ha',
  metricType: 'STORAGE_IOPS',
  value: 22768.13,
  unit: 'snoopy mid',
  timestamp: dayjs('2025-12-24T21:06'),
};

export const sampleWithPartialData: IPerformanceMetric = {
  id: 25157,
  metricName: 'although',
  metricType: 'CONCURRENT_USERS',
  value: 20741.65,
  unit: 'questionable oof pac',
  timestamp: dayjs('2025-12-25T11:24'),
};

export const sampleWithFullData: IPerformanceMetric = {
  id: 4673,
  metricName: 'following calmly',
  metricType: 'UPLOAD_SPEED',
  value: 14196.77,
  unit: 'fictionalize',
  serviceName: 'numeric casement aw',
  timestamp: dayjs('2025-12-24T23:01'),
};

export const sampleWithNewData: NewPerformanceMetric = {
  metricName: 'gummy readies',
  metricType: 'CACHE_HIT_RATE',
  value: 1279.54,
  unit: 'however humidity sma',
  timestamp: dayjs('2025-12-24T20:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
