import dayjs from 'dayjs/esm';

import { IReportExecution, NewReportExecution } from './report-execution.model';

export const sampleWithRequiredData: IReportExecution = {
  id: 24938,
  scheduledReportId: 8123,
  status: 'FAILED',
  startDate: dayjs('2025-12-25T01:06'),
};

export const sampleWithPartialData: IReportExecution = {
  id: 31800,
  scheduledReportId: 26407,
  status: 'FAILED',
  startDate: dayjs('2025-12-24T18:05'),
  endDate: dayjs('2025-12-24T19:24'),
  outputS3Key: 'recount',
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReportExecution = {
  id: 27309,
  scheduledReportId: 23243,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-24T22:23'),
  endDate: dayjs('2025-12-25T03:27'),
  recordsProcessed: 5786,
  outputS3Key: 'hmph rudely whenever',
  outputSize: 14278,
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportExecution = {
  scheduledReportId: 80,
  status: 'FAILED',
  startDate: dayjs('2025-12-24T18:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
