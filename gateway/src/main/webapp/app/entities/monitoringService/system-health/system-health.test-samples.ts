import dayjs from 'dayjs/esm';

import { ISystemHealth, NewSystemHealth } from './system-health.model';

export const sampleWithRequiredData: ISystemHealth = {
  id: 17088,
  serviceName: 'yawningly hype',
  status: 'UNKNOWN',
  lastCheck: dayjs('2025-12-20T03:12'),
};

export const sampleWithPartialData: ISystemHealth = {
  id: 16371,
  serviceName: 'micromanage',
  status: 'UNHEALTHY',
  version: 'adventurously plus fog',
  uptime: 21069,
  cpuUsage: 8541.87,
  memoryUsage: 31075.35,
  errorRate: 8700.91,
  lastCheck: dayjs('2025-12-19T23:24'),
};

export const sampleWithFullData: ISystemHealth = {
  id: 7142,
  serviceName: 'or as',
  status: 'HEALTHY',
  version: 'amid how',
  uptime: 29104,
  cpuUsage: 9117.49,
  memoryUsage: 7873.04,
  errorRate: 9881.82,
  lastCheck: dayjs('2025-12-20T06:46'),
};

export const sampleWithNewData: NewSystemHealth = {
  serviceName: 'although unkempt',
  status: 'HEALTHY',
  lastCheck: dayjs('2025-12-20T10:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
