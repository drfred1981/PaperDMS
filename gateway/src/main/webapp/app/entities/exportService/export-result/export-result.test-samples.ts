import dayjs from 'dayjs/esm';

import { IExportResult, NewExportResult } from './export-result.model';

export const sampleWithRequiredData: IExportResult = {
  id: 5762,
  exportJobId: 3845,
  documentId: 24275,
  documentSha256: 'oof because',
  exportedDate: dayjs('2025-12-24T13:32'),
};

export const sampleWithPartialData: IExportResult = {
  id: 462,
  exportJobId: 29728,
  documentId: 26880,
  documentSha256: 'huzzah scholarship lest',
  exportedPath: 'whoa wriggler design',
  s3ExportKey: 'pfft',
  status: 'FAILED',
  exportedDate: dayjs('2025-12-25T04:48'),
};

export const sampleWithFullData: IExportResult = {
  id: 2924,
  exportJobId: 27534,
  documentId: 20594,
  documentSha256: 'gullible crossly',
  originalFileName: 'come slake prejudge',
  exportedPath: 'until cleave yowza',
  exportedFileName: 'likely',
  s3ExportKey: 'miserably twist',
  fileSize: 21326,
  status: 'FAILED',
  errorMessage: '../fake-data/blob/hipster.txt',
  exportedDate: dayjs('2025-12-25T05:24'),
};

export const sampleWithNewData: NewExportResult = {
  exportJobId: 26399,
  documentId: 15591,
  documentSha256: 'offensively barring triumphantly',
  exportedDate: dayjs('2025-12-25T07:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
