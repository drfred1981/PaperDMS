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
  createdDate: dayjs('2025-12-20T01:06'),
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
  startDate: dayjs('2025-12-20T10:22'),
  endDate: dayjs('2025-12-19T23:18'),
  createdBy: 'excited',
  createdDate: dayjs('2025-12-20T04:13'),
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
  startDate: dayjs('2025-12-19T17:30'),
  endDate: dayjs('2025-12-20T05:43'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'if soggy',
  createdDate: dayjs('2025-12-20T03:34'),
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
  createdDate: dayjs('2025-12-20T05:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
