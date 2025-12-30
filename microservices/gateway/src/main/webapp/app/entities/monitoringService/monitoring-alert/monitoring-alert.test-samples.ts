import dayjs from 'dayjs/esm';

import { IMonitoringAlert, NewMonitoringAlert } from './monitoring-alert.model';

export const sampleWithRequiredData: IMonitoringAlert = {
  id: 22175,
  severity: 'LOW',
  title: 'scarcely',
  message: '../fake-data/blob/hipster.txt',
  status: 'ESCALATED',
  triggeredDate: dayjs('2025-12-29T08:40'),
};

export const sampleWithPartialData: IMonitoringAlert = {
  id: 10885,
  severity: 'CRITICAL',
  title: 'window whereas',
  message: '../fake-data/blob/hipster.txt',
  entityName: 'round',
  status: 'ACKNOWLEDGED',
  triggeredDate: dayjs('2025-12-30T04:08'),
  acknowledgedBy: 'despite',
  resolvedDate: dayjs('2025-12-29T21:54'),
};

export const sampleWithFullData: IMonitoringAlert = {
  id: 285,
  severity: 'MEDIUM',
  title: 'deafening',
  message: '../fake-data/blob/hipster.txt',
  entityType: 'wrong unlike anaesthetise',
  entityName: 'controvert',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-30T06:48'),
  acknowledgedBy: 'inwardly despite',
  acknowledgedDate: dayjs('2025-12-30T03:23'),
  resolvedBy: 'sashay likewise hamburger',
  resolvedDate: dayjs('2025-12-30T06:40'),
};

export const sampleWithNewData: NewMonitoringAlert = {
  severity: 'LOW',
  title: 'avalanche service',
  message: '../fake-data/blob/hipster.txt',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-29T09:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
