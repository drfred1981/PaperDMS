import dayjs from 'dayjs/esm';

import { IDocumentMetadata, NewDocumentMetadata } from './document-metadata.model';

export const sampleWithRequiredData: IDocumentMetadata = {
  id: 24441,
  key: 'only unexpectedly though',
  value: '../fake-data/blob/hipster.txt',
  isSearchable: true,
  createdDate: dayjs('2025-12-30T00:56'),
};

export const sampleWithPartialData: IDocumentMetadata = {
  id: 1489,
  key: 'actual phew',
  value: '../fake-data/blob/hipster.txt',
  isSearchable: false,
  createdDate: dayjs('2025-12-29T12:18'),
};

export const sampleWithFullData: IDocumentMetadata = {
  id: 6835,
  key: 'crocodile tired',
  value: '../fake-data/blob/hipster.txt',
  dataType: 'DECIMAL',
  isSearchable: true,
  createdDate: dayjs('2025-12-30T00:14'),
};

export const sampleWithNewData: NewDocumentMetadata = {
  key: 'yet miserably how',
  value: '../fake-data/blob/hipster.txt',
  isSearchable: true,
  createdDate: dayjs('2025-12-29T11:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
