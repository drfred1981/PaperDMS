import dayjs from 'dayjs/esm';

import { IExportResult, NewExportResult } from './export-result.model';

export const sampleWithRequiredData: IExportResult = {
  id: 5762,
  documentSha256: 'separately',
  exportedDate: dayjs('2025-12-29T18:11'),
};

export const sampleWithPartialData: IExportResult = {
  id: 462,
  documentSha256: 'er absent beyond',
  exportedPath: 'but endow obscure',
  s3ExportKey: 'configuration',
  status: 'SUCCESS',
  exportedDate: dayjs('2025-12-29T13:17'),
};

export const sampleWithFullData: IExportResult = {
  id: 2924,
  documentSha256: 'separately yuck um',
  originalFileName: 'psst denitrify inasmuch',
  exportedPath: 'redevelop meanwhile',
  exportedFileName: 'bookcase puny',
  s3ExportKey: 'healthily',
  fileSize: 5569,
  status: 'SKIPPED',
  errorMessage: '../fake-data/blob/hipster.txt',
  exportedDate: dayjs('2025-12-29T23:03'),
};

export const sampleWithNewData: NewExportResult = {
  documentSha256: 'ugly pike surface',
  exportedDate: dayjs('2025-12-29T19:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
