import dayjs from 'dayjs/esm';

import { IScheduledReport, NewScheduledReport } from './scheduled-report.model';

export const sampleWithRequiredData: IScheduledReport = {
  id: 2801,
  name: 'lest yawningly executor',
  reportType: 'WORKFLOW_PERFORMANCE',
  schedule: 'pleasant graffiti bathrobe',
  format: 'XML',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdBy: 'past reprimand offensively',
  createdDate: dayjs('2025-12-24T13:00'),
};

export const sampleWithPartialData: IScheduledReport = {
  id: 12985,
  name: 'meh',
  description: '../fake-data/blob/hipster.txt',
  reportType: 'USER_ACTIVITY',
  query: '../fake-data/blob/hipster.txt',
  schedule: 'scratchy highly',
  format: 'XML',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdBy: 'likewise likewise rim',
  createdDate: dayjs('2025-12-25T01:56'),
};

export const sampleWithFullData: IScheduledReport = {
  id: 3440,
  name: 'spotless whenever if',
  description: '../fake-data/blob/hipster.txt',
  reportType: 'USER_ACTIVITY',
  query: '../fake-data/blob/hipster.txt',
  schedule: 'through famously',
  format: 'JSON',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: false,
  lastRun: dayjs('2025-12-25T02:00'),
  nextRun: dayjs('2025-12-25T03:52'),
  createdBy: 'puny meh oof',
  createdDate: dayjs('2025-12-24T18:14'),
};

export const sampleWithNewData: NewScheduledReport = {
  name: 'officially per bowler',
  reportType: 'TAG_USAGE',
  schedule: 'qua consequently',
  format: 'PDF',
  recipients: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'intermix sternly',
  createdDate: dayjs('2025-12-25T07:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
