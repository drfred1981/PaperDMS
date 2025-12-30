import dayjs from 'dayjs/esm';

import { IMonitoringMaintenanceTask, NewMonitoringMaintenanceTask } from './monitoring-maintenance-task.model';

export const sampleWithRequiredData: IMonitoringMaintenanceTask = {
  id: 10961,
  name: 'provided duh spiffy',
  taskType: 'PRUNE_AUDIT_LOGS',
  schedule: 'monasticism defiantly',
  status: 'PROCESSING',
  isActive: false,
  createdBy: 'carelessly',
  createdDate: dayjs('2025-12-29T15:40'),
};

export const sampleWithPartialData: IMonitoringMaintenanceTask = {
  id: 8137,
  name: 'phew',
  description: '../fake-data/blob/hipster.txt',
  taskType: 'UPDATE_STATISTICS',
  schedule: 'since',
  status: 'CANCELLED',
  isActive: true,
  lastRun: dayjs('2025-12-29T17:51'),
  nextRun: dayjs('2025-12-29T18:15'),
  duration: 7511,
  recordsProcessed: 20738,
  createdBy: 'narrow yum',
  createdDate: dayjs('2025-12-29T13:52'),
};

export const sampleWithFullData: IMonitoringMaintenanceTask = {
  id: 24521,
  name: 'gape',
  description: '../fake-data/blob/hipster.txt',
  taskType: 'PRUNE_AUDIT_LOGS',
  schedule: 'bah question',
  status: 'PENDING',
  isActive: true,
  lastRun: dayjs('2025-12-30T05:11'),
  nextRun: dayjs('2025-12-29T09:47'),
  duration: 4353,
  recordsProcessed: 17979,
  createdBy: 'underneath',
  createdDate: dayjs('2025-12-29T08:05'),
};

export const sampleWithNewData: NewMonitoringMaintenanceTask = {
  name: 'so unless',
  taskType: 'OPTIMIZE_STORAGE',
  schedule: 'after ostrich courteous',
  status: 'PROCESSING',
  isActive: false,
  createdBy: 'unsung solemnly government',
  createdDate: dayjs('2025-12-29T22:26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
