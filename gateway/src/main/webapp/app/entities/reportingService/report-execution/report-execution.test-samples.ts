import dayjs from 'dayjs/esm';

import { IReportExecution, NewReportExecution } from './report-execution.model';

export const sampleWithRequiredData: IReportExecution = {
  id: 24938,
  scheduledReportId: 8123,
  status: 'FAILED',
  startDate: dayjs('2025-12-20T06:07'),
};

export const sampleWithPartialData: IReportExecution = {
  id: 31800,
  scheduledReportId: 26407,
  status: 'FAILED',
  startDate: dayjs('2025-12-19T23:06'),
  endDate: dayjs('2025-12-20T00:25'),
  outputS3Key: 'recount',
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReportExecution = {
  id: 27309,
  scheduledReportId: 23243,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-20T03:24'),
  endDate: dayjs('2025-12-20T08:28'),
  recordsProcessed: 5786,
  outputS3Key: 'hmph rudely whenever',
  outputSize: 14278,
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportExecution = {
  scheduledReportId: 80,
  status: 'FAILED',
  startDate: dayjs('2025-12-19T23:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
