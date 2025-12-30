import dayjs from 'dayjs/esm';

import { IReportingPerformanceMetric, NewReportingPerformanceMetric } from './reporting-performance-metric.model';

export const sampleWithRequiredData: IReportingPerformanceMetric = {
  id: 10059,
  metricName: 'federate',
  metricType: 'CACHE_HIT_RATE',
  value: 1806.6,
  unit: 'before',
  timestamp: dayjs('2025-12-29T11:02'),
};

export const sampleWithPartialData: IReportingPerformanceMetric = {
  id: 25017,
  metricName: 'redevelop hippodrome traffic',
  metricType: 'OCR_DURATION',
  value: 17448.95,
  unit: 'inculcate editor',
  timestamp: dayjs('2025-12-30T00:48'),
};

export const sampleWithFullData: IReportingPerformanceMetric = {
  id: 31688,
  metricName: 'ring',
  metricType: 'CACHE_HIT_RATE',
  value: 3731.82,
  unit: 'how mid',
  serviceName: 'phooey',
  timestamp: dayjs('2025-12-29T20:49'),
};

export const sampleWithNewData: NewReportingPerformanceMetric = {
  metricName: 'nun digestive',
  metricType: 'API_RESPONSE_TIME',
  value: 19737.45,
  unit: 'direct',
  timestamp: dayjs('2025-12-30T05:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
