import dayjs from 'dayjs/esm';

import { IAlert, NewAlert } from './alert.model';

export const sampleWithRequiredData: IAlert = {
  id: 5277,
  alertRuleId: 23580,
  severity: 'LOW',
  title: 'ad save',
  message: '../fake-data/blob/hipster.txt',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-25T11:03'),
};

export const sampleWithPartialData: IAlert = {
  id: 26409,
  alertRuleId: 12180,
  severity: 'LOW',
  title: 'oh',
  message: '../fake-data/blob/hipster.txt',
  entityType: 'aboard',
  status: 'ACTIVE',
  triggeredDate: dayjs('2025-12-24T17:52'),
  acknowledgedDate: dayjs('2025-12-25T01:11'),
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
  triggeredDate: dayjs('2025-12-24T15:42'),
  acknowledgedBy: 'supportive',
  acknowledgedDate: dayjs('2025-12-25T08:40'),
  resolvedBy: 'brilliant venom mob',
  resolvedDate: dayjs('2025-12-25T09:39'),
};

export const sampleWithNewData: NewAlert = {
  alertRuleId: 10112,
  severity: 'CRITICAL',
  title: 'geez',
  message: '../fake-data/blob/hipster.txt',
  status: 'RESOLVED',
  triggeredDate: dayjs('2025-12-25T05:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
