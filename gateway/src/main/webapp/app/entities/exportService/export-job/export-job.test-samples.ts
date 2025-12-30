import dayjs from 'dayjs/esm';

import { IExportJob, NewExportJob } from './export-job.model';

export const sampleWithRequiredData: IExportJob = {
  id: 10847,
  name: 'ick',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportFormat: 'PDF_A',
  includeMetadata: true,
  includeVersions: false,
  status: 'CANCELLED',
  createdBy: 'floodlight dimly',
  createdDate: dayjs('2025-12-29T08:02'),
};

export const sampleWithPartialData: IExportJob = {
  id: 16726,
  name: 'handful hmph',
  description: '../fake-data/blob/hipster.txt',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportFormat: 'ZIP',
  includeMetadata: true,
  includeVersions: false,
  includeComments: false,
  s3ExportKey: 'allocation adumbrate excited',
  documentCount: 16247,
  filesGenerated: 13335,
  status: 'PARTIAL',
  startDate: dayjs('2025-12-29T18:16'),
  endDate: dayjs('2025-12-30T02:40'),
  createdBy: 'now uselessly',
  createdDate: dayjs('2025-12-29T14:32'),
};

export const sampleWithFullData: IExportJob = {
  id: 24600,
  name: 'yearningly variable',
  description: '../fake-data/blob/hipster.txt',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportFormat: 'PDF_A',
  includeMetadata: true,
  includeVersions: false,
  includeComments: false,
  includeAuditTrail: false,
  s3ExportKey: 'meanwhile vice mid',
  exportSize: 13420,
  documentCount: 27951,
  filesGenerated: 6308,
  status: 'COMPLETED',
  startDate: dayjs('2025-12-29T18:22'),
  endDate: dayjs('2025-12-29T15:06'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'piglet hairy',
  createdDate: dayjs('2025-12-29T14:04'),
};

export const sampleWithNewData: NewExportJob = {
  name: 'huzzah',
  documentQuery: '../fake-data/blob/hipster.txt',
  exportFormat: 'FOLDER',
  includeMetadata: false,
  includeVersions: false,
  status: 'COMPLETED',
  createdBy: 'except upon without',
  createdDate: dayjs('2025-12-30T03:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
