import dayjs from 'dayjs/esm';

import { IManual, NewManual } from './manual.model';

export const sampleWithRequiredData: IManual = {
  id: 20033,
  documentId: 12012,
  title: 'linseed',
  manualType: 'TECHNICAL_MANUAL',
  version: 'what so',
  language: 'spook port',
  status: 'DRAFT',
  isPublic: false,
  createdDate: dayjs('2025-12-25T04:27'),
};

export const sampleWithPartialData: IManual = {
  id: 29764,
  documentId: 32106,
  title: 'hmph scarcely',
  manualType: 'USER_MANUAL',
  version: 'brr',
  language: 'vestment d',
  publicationDate: dayjs('2025-12-25'),
  status: 'ARCHIVED',
  isPublic: false,
  createdDate: dayjs('2025-12-25T03:15'),
};

export const sampleWithFullData: IManual = {
  id: 29018,
  documentId: 1692,
  title: 'untimely interestingly muffled',
  manualType: 'MAINTENANCE_GUIDE',
  version: 'than shirk fuss',
  language: 'machine mu',
  publicationDate: dayjs('2025-12-24'),
  pageCount: 21287,
  status: 'DRAFT',
  isPublic: false,
  createdDate: dayjs('2025-12-24T22:21'),
};

export const sampleWithNewData: NewManual = {
  documentId: 3794,
  title: 'yarmulke',
  manualType: 'USER_MANUAL',
  version: 'outset orchid',
  language: 'than',
  status: 'PUBLISHED',
  isPublic: true,
  createdDate: dayjs('2025-12-25T00:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
