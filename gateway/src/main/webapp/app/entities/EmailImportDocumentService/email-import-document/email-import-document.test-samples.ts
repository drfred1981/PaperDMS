import dayjs from 'dayjs/esm';

import { IEmailImportDocument, NewEmailImportDocument } from './email-import-document.model';

export const sampleWithRequiredData: IEmailImportDocument = {
  id: 212,
  sha256: 'valiantly while and',
  fromEmail: 'ha sharply',
  toEmail: 'instead until motivate',
  receivedDate: dayjs('2025-12-29T12:01'),
  status: 'PROCESSING',
};

export const sampleWithPartialData: IEmailImportDocument = {
  id: 18135,
  sha256: 'homeschool',
  fromEmail: 'readies armchair assail',
  toEmail: 'woot',
  body: '../fake-data/blob/hipster.txt',
  bodyHtml: '../fake-data/blob/hipster.txt',
  receivedDate: dayjs('2025-12-30T04:38'),
  processedDate: dayjs('2025-12-29T09:49'),
  status: 'PENDING',
  errorMessage: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IEmailImportDocument = {
  id: 28430,
  sha256: 'rosemary',
  fromEmail: 'funny wilt',
  toEmail: 'where really',
  subject: 'attribute supposing mad',
  body: '../fake-data/blob/hipster.txt',
  bodyHtml: '../fake-data/blob/hipster.txt',
  receivedDate: dayjs('2025-12-29T10:58'),
  processedDate: dayjs('2025-12-29T19:53'),
  status: 'PARTIAL',
  attachmentCount: 4211,
  documentsCreated: 17060,
  errorMessage: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  documentSha256: 'pfft aboard',
};

export const sampleWithNewData: NewEmailImportDocument = {
  sha256: 'neglected',
  fromEmail: 'phew manipulate nor',
  toEmail: 'along gah from',
  receivedDate: dayjs('2025-12-29T20:08'),
  status: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
