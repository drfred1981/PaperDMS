import dayjs from 'dayjs/esm';

import { IEmailImport, NewEmailImport } from './email-import.model';

export const sampleWithRequiredData: IEmailImport = {
  id: 9534,
  fromEmail: 'runny',
  toEmail: 'and',
  receivedDate: dayjs('2025-12-19T16:57'),
  status: 'COMPLETED',
};

export const sampleWithPartialData: IEmailImport = {
  id: 29177,
  fromEmail: 'considering courageous rekindle',
  toEmail: 'including bid so',
  receivedDate: dayjs('2025-12-20T04:25'),
  processedDate: dayjs('2025-12-19T21:19'),
  status: 'PENDING',
  documentTypeId: 18960,
  attachmentCount: 31253,
  documentsCreated: 23038,
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IEmailImport = {
  id: 5904,
  fromEmail: 'till',
  toEmail: 'sleet despite',
  subject: 'true scarily since',
  body: '../fake-data/blob/hipster.txt',
  bodyHtml: '../fake-data/blob/hipster.txt',
  receivedDate: dayjs('2025-12-20T10:44'),
  processedDate: dayjs('2025-12-20T08:16'),
  status: 'PARTIAL',
  folderId: 14953,
  documentTypeId: 16936,
  attachmentCount: 13064,
  documentsCreated: 7005,
  appliedRuleId: 24216,
  errorMessage: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewEmailImport = {
  fromEmail: 'atop pulp turbulent',
  toEmail: 'shrilly',
  receivedDate: dayjs('2025-12-20T15:20'),
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
