import dayjs from 'dayjs/esm';

import { IExportJob, NewExportJob } from './export-job.model';

export const sampleWithRequiredData: IExportJob = {
  id: 10847,
  name: 'ick',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportPatternId: 11681,
  exportFormat: 'PDF_A',
  includeMetadata: false,
  includeVersions: false,
  status: 'COMPLETED',
  createdBy: 'suffice although',
  createdDate: dayjs('2025-12-24T20:05'),
};

export const sampleWithPartialData: IExportJob = {
  id: 16726,
  name: 'handful hmph',
  description: '../fake-data/blob/hipster.txt',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportPatternId: 15391,
  exportFormat: 'PDF_A',
  includeMetadata: false,
  includeVersions: false,
  includeComments: false,
  s3ExportKey: 'sham',
  documentCount: 25897,
  filesGenerated: 9299,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-25T05:21'),
  endDate: dayjs('2025-12-24T18:17'),
  createdBy: 'excited',
  createdDate: dayjs('2025-12-24T23:12'),
};

export const sampleWithFullData: IExportJob = {
  id: 24600,
  name: 'yearningly variable',
  description: '../fake-data/blob/hipster.txt',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportPatternId: 12672,
  exportFormat: 'PDF_A',
  includeMetadata: false,
  includeVersions: false,
  includeComments: false,
  includeAuditTrail: false,
  s3ExportKey: 'while pretty',
  exportSize: 20890,
  documentCount: 28369,
  filesGenerated: 14355,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-24T12:29'),
  endDate: dayjs('2025-12-25T00:42'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'if soggy',
  createdDate: dayjs('2025-12-24T22:33'),
};

export const sampleWithNewData: NewExportJob = {
  name: 'huzzah',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportPatternId: 23359,
  exportFormat: 'FOLDER',
  includeMetadata: false,
  includeVersions: true,
  status: 'FAILED',
  createdBy: 'huzzah unconscious wherever',
  createdDate: dayjs('2025-12-25T00:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
