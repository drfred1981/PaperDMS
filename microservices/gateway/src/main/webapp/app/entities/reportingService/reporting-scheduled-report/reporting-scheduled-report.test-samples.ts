import dayjs from 'dayjs/esm';

import { IReportingScheduledReport, NewReportingScheduledReport } from './reporting-scheduled-report.model';

export const sampleWithRequiredData: IReportingScheduledReport = {
  id: 18889,
  name: 'to',
  reportType: 'CUSTOM',
  schedule: 'besides faint smoothly',
  format: 'PDF',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'forenenst',
  createdDate: dayjs('2025-12-29T08:45'),
};

export const sampleWithPartialData: IReportingScheduledReport = {
  id: 24804,
  name: 'gleefully shoulder yet',
  description: '../fake-data/blob/hipster.txt',
  reportType: 'TAG_USAGE',
  query: '../fake-data/blob/hipster.txt',
  schedule: 'per arid however',
  format: 'PDF',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'sweetly curiously',
  createdDate: dayjs('2025-12-29T10:47'),
};

export const sampleWithFullData: IReportingScheduledReport = {
  id: 18502,
  name: 'ultimately compromise for',
  description: '../fake-data/blob/hipster.txt',
  reportType: 'COMPLIANCE_AUDIT',
  query: '../fake-data/blob/hipster.txt',
  schedule: 'cellar reproachfully',
  format: 'PDF',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  lastRun: dayjs('2025-12-30T05:50'),
  nextRun: dayjs('2025-12-30T01:27'),
  createdBy: 'following consequently pushy',
  createdDate: dayjs('2025-12-29T12:48'),
};

export const sampleWithNewData: NewReportingScheduledReport = {
  name: 'across once after',
  reportType: 'COMPLIANCE_AUDIT',
  schedule: 'poppy wide-eyed ew',
  format: 'JSON',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'that',
  createdDate: dayjs('2025-12-29T23:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
