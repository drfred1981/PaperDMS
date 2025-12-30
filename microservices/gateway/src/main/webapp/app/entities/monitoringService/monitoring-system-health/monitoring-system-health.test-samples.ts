import dayjs from 'dayjs/esm';

import { IMonitoringSystemHealth, NewMonitoringSystemHealth } from './monitoring-system-health.model';

export const sampleWithRequiredData: IMonitoringSystemHealth = {
  id: 13884,
  serviceName: 'bah',
  status: 'UNHEALTHY',
  lastCheck: dayjs('2025-12-30T00:44'),
};

export const sampleWithPartialData: IMonitoringSystemHealth = {
  id: 1520,
  serviceName: 'broadly',
  status: 'UNKNOWN',
  cpuUsage: 16347.56,
  lastCheck: dayjs('2025-12-29T19:09'),
};

export const sampleWithFullData: IMonitoringSystemHealth = {
  id: 4875,
  serviceName: 'considering',
  status: 'HEALTHY',
  version: 'with',
  uptime: 24211,
  cpuUsage: 30930.62,
  memoryUsage: 27066.39,
  errorRate: 18206.85,
  lastCheck: dayjs('2025-12-29T16:29'),
};

export const sampleWithNewData: NewMonitoringSystemHealth = {
  serviceName: 'slimy',
  status: 'UNHEALTHY',
  lastCheck: dayjs('2025-12-29T22:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
