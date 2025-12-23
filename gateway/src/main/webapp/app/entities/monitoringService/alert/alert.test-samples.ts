import dayjs from 'dayjs/esm';

import { IAlert, NewAlert } from './alert.model';

export const sampleWithRequiredData: IAlert = {
  id: 5277,
  alertRuleId: 23580,
  severity: 'LOW',
  title: 'ad save',
  message: '../fake-data/blob/hipster.txt',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-20T16:04'),
};

export const sampleWithPartialData: IAlert = {
  id: 26409,
  alertRuleId: 12180,
  severity: 'LOW',
  title: 'oh',
  message: '../fake-data/blob/hipster.txt',
  entityType: 'aboard',
  status: 'ACTIVE',
  triggeredDate: dayjs('2025-12-19T22:53'),
  acknowledgedDate: dayjs('2025-12-20T06:12'),
};

export const sampleWithFullData: IAlert = {
  id: 22495,
  alertRuleId: 3493,
  severity: 'LOW',
  title: 'as to beside',
  message: '../fake-data/blob/hipster.txt',
  entityType: 'speedy viciously',
  entityId: 28853,
  status: 'DISMISSED',
  triggeredDate: dayjs('2025-12-19T20:43'),
  acknowledgedBy: 'supportive',
  acknowledgedDate: dayjs('2025-12-20T13:41'),
  resolvedBy: 'brilliant venom mob',
  resolvedDate: dayjs('2025-12-20T14:40'),
};

export const sampleWithNewData: NewAlert = {
  alertRuleId: 10112,
  severity: 'CRITICAL',
  title: 'geez',
  message: '../fake-data/blob/hipster.txt',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-20T10:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
