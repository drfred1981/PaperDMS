import dayjs from 'dayjs/esm';

import { ISystemMetric, NewSystemMetric } from './system-metric.model';

export const sampleWithRequiredData: ISystemMetric = {
  id: 20012,
  metricName: 'misjudge humiliating until',
  timestamp: dayjs('2025-12-24T17:42'),
};

export const sampleWithPartialData: ISystemMetric = {
  id: 6106,
  metricName: 'crafty',
  memoryUsage: 3614.46,
  networkOut: 25689,
  activeConnections: 15562,
  timestamp: dayjs('2025-12-25T07:59'),
};

export const sampleWithFullData: ISystemMetric = {
  id: 29804,
  metricName: 'oof',
  cpuUsage: 9304.85,
  memoryUsage: 30382.39,
  diskUsage: 852.24,
  networkIn: 17591,
  networkOut: 27975,
  activeConnections: 26456,
  timestamp: dayjs('2025-12-24T22:57'),
};

export const sampleWithNewData: NewSystemMetric = {
  metricName: 'conversation furthermore chip',
  timestamp: dayjs('2025-12-25T03:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
