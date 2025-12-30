import dayjs from 'dayjs/esm';

import { IMonitoringAlertRule, NewMonitoringAlertRule } from './monitoring-alert-rule.model';

export const sampleWithRequiredData: IMonitoringAlertRule = {
  id: 9556,
  name: 'past',
  alertType: 'SERVICE_DOWN',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'HIGH',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'that antagonize',
  createdDate: dayjs('2025-12-29T19:27'),
};

export const sampleWithPartialData: IMonitoringAlertRule = {
  id: 14919,
  name: 'hence diagram diligent',
  alertType: 'INVOICE_OVERDUE',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'MEDIUM',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  lastTriggered: dayjs('2025-12-30T00:07'),
  createdBy: 'cork fraternise',
  createdDate: dayjs('2025-12-29T12:43'),
};

export const sampleWithFullData: IMonitoringAlertRule = {
  id: 4990,
  name: 'wherever',
  description: '../fake-data/blob/hipster.txt',
  alertType: 'DOCUMENT_EXPIRATION',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'LOW',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  triggerCount: 28208,
  lastTriggered: dayjs('2025-12-30T03:48'),
  createdBy: 'crafty assail instead',
  createdDate: dayjs('2025-12-30T05:37'),
};

export const sampleWithNewData: NewMonitoringAlertRule = {
  name: 'pfft yum',
  alertType: 'WORKFLOW_DELAYED',
  conditions: '../fake-data/blob/hipster.txt',
  severity: 'LOW',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'about coolly happy',
  createdDate: dayjs('2025-12-29T17:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
