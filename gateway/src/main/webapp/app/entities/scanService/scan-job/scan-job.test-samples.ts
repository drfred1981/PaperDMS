import dayjs from 'dayjs/esm';

import { IScanJob, NewScanJob } from './scan-job.model';

export const sampleWithRequiredData: IScanJob = {
  id: 19796,
  name: 'so yak buck',
  scannerConfigId: 19635,
  status: 'CANCELLED',
  createdBy: 'netsuke which next',
  createdDate: dayjs('2025-12-24T19:53'),
};

export const sampleWithPartialData: IScanJob = {
  id: 25545,
  name: 'madly now',
  scannerConfigId: 24372,
  status: 'COMPLETED',
  colorMode: 'COLOR',
  resolution: 5176,
  startDate: dayjs('2025-12-24T17:46'),
  endDate: dayjs('2025-12-25T00:54'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'justly formula',
  createdDate: dayjs('2025-12-24T11:25'),
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
  startDate: dayjs('2025-12-25T05:30'),
  endDate: dayjs('2025-12-25T04:48'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'huzzah source what',
  createdDate: dayjs('2025-12-24T15:01'),
};

export const sampleWithNewData: NewScanJob = {
  name: 'huzzah ah shyly',
  scannerConfigId: 18267,
  status: 'COMPLETED',
  createdBy: 'norm lamp brr',
  createdDate: dayjs('2025-12-24T22:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
