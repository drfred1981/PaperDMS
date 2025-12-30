import dayjs from 'dayjs/esm';

import { IReportingExecution, NewReportingExecution } from './reporting-execution.model';

export const sampleWithRequiredData: IReportingExecution = {
  id: 27320,
  status: 'FAILED',
  startDate: dayjs('2025-12-30T01:37'),
};

export const sampleWithPartialData: IReportingExecution = {
  id: 20530,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-29T17:10'),
  recordsProcessed: 13217,
  outputS3Key: 'exaggerate likewise zowie',
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReportingExecution = {
  id: 25335,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-29T11:36'),
  endDate: dayjs('2025-12-30T02:30'),
  recordsProcessed: 2676,
  outputS3Key: 'expansion',
  outputSize: 21947,
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportingExecution = {
  status: 'COMPLETED',
  startDate: dayjs('2025-12-29T15:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
