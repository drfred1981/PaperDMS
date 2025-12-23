import dayjs from 'dayjs/esm';

import { IScanJob, NewScanJob } from './scan-job.model';

export const sampleWithRequiredData: IScanJob = {
  id: 19796,
  name: 'so yak buck',
  scannerConfigId: 19635,
  status: 'CANCELLED',
  createdBy: 'netsuke which next',
  createdDate: dayjs('2025-12-20T00:54'),
};

export const sampleWithPartialData: IScanJob = {
  id: 25545,
  name: 'madly now',
  scannerConfigId: 24372,
  status: 'COMPLETED',
  colorMode: 'COLOR',
  resolution: 5176,
  startDate: dayjs('2025-12-19T22:47'),
  endDate: dayjs('2025-12-20T05:55'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'justly formula',
  createdDate: dayjs('2025-12-19T16:26'),
};

export const sampleWithFullData: IScanJob = {
  id: 19829,
  name: 'awkwardly',
  description: '../fake-data/blob/hipster.txt',
  scannerConfigId: 6097,
  batchId: 524,
  documentTypeId: 5006,
  folderId: 18038,
  pageCount: 32605,
  status: 'COMPLETED',
  colorMode: 'BLACK_WHITE',
  resolution: 14861,
  fileFormat: 'JPEG',
  startDate: dayjs('2025-12-20T10:31'),
  endDate: dayjs('2025-12-20T09:49'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'huzzah source what',
  createdDate: dayjs('2025-12-19T20:02'),
};

export const sampleWithNewData: NewScanJob = {
  name: 'huzzah ah shyly',
  scannerConfigId: 18267,
  status: 'COMPLETED',
  createdBy: 'norm lamp brr',
  createdDate: dayjs('2025-12-20T03:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
