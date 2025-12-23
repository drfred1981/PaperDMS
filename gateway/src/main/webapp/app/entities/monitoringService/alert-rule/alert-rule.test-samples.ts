import dayjs from 'dayjs/esm';

import { IAlertRule, NewAlertRule } from './alert-rule.model';

export const sampleWithRequiredData: IAlertRule = {
  id: 389,
  name: 'blight',
  alertType: 'HIGH_ERROR_RATE',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'CRITICAL',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdBy: 'midst',
  createdDate: dayjs('2025-12-20T04:33'),
};

export const sampleWithPartialData: IAlertRule = {
  id: 13428,
  name: 'blah diversity between',
  alertType: 'DUPLICATE_DETECTED',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'MEDIUM',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  triggerCount: 10285,
  lastTriggered: dayjs('2025-12-20T08:30'),
  createdBy: 'why',
  createdDate: dayjs('2025-12-20T01:25'),
};

export const sampleWithFullData: IAlertRule = {
  id: 28204,
  name: 'search',
  description: '../fake-data/blob/hipster.txt',
  alertType: 'FAILED_OCR',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'LOW',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  triggerCount: 1384,
  lastTriggered: dayjs('2025-12-20T10:53'),
  createdBy: 'clearly atop pressure',
  createdDate: dayjs('2025-12-20T15:17'),
};

export const sampleWithNewData: NewAlertRule = {
  name: 'huzzah',
  alertType: 'DUPLICATE_DETECTED',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'HIGH',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdBy: 'mindless esteemed inside',
  createdDate: dayjs('2025-12-20T00:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
