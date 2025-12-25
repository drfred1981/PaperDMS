import dayjs from 'dayjs/esm';

import { IRedactionJob, NewRedactionJob } from './redaction-job.model';

export const sampleWithRequiredData: IRedactionJob = {
  id: 31053,
  documentId: 12490,
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'WHITE_BOX',
  status: 'CANCELLED',
  createdBy: 'um design',
  createdDate: dayjs('2025-12-25T04:17'),
};

export const sampleWithPartialData: IRedactionJob = {
  id: 17332,
  documentId: 11277,
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'BLUR',
  redactionColor: 'turret ',
  outputDocumentId: 22695,
  status: 'COMPLETED',
  endDate: dayjs('2025-12-25T08:36'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'huzzah',
  createdDate: dayjs('2025-12-25T04:52'),
};

export const sampleWithFullData: IRedactionJob = {
  id: 17530,
  documentId: 30517,
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'BLUR',
  redactionColor: 'pish',
  replaceWith: 'forsaken ready duh',
  outputS3Key: 'properly notwithstanding',
  outputDocumentId: 31121,
  status: 'PROCESSING',
  startDate: dayjs('2025-12-25T08:22'),
  endDate: dayjs('2025-12-25T09:14'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'hoarse mechanically',
  createdDate: dayjs('2025-12-24T15:16'),
};

export const sampleWithNewData: NewRedactionJob = {
  documentId: 25122,
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'BLACK_BOX',
  status: 'PROCESSING',
  createdBy: 'before square inwardly',
  createdDate: dayjs('2025-12-25T06:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
