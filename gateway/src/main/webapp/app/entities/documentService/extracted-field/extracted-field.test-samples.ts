import dayjs from 'dayjs/esm';

import { IExtractedField, NewExtractedField } from './extracted-field.model';

export const sampleWithRequiredData: IExtractedField = {
  id: 17432,
  documentId: 1769,
  fieldKey: 'rowdy phooey who',
  fieldValue: '../fake-data/blob/hipster.txt',
  isVerified: true,
  extractedDate: dayjs('2025-12-19T20:29'),
};

export const sampleWithPartialData: IExtractedField = {
  id: 25256,
  documentId: 9975,
  fieldKey: 'unethically dark proofread',
  fieldValue: '../fake-data/blob/hipster.txt',
  confidence: 0.46,
  extractionMethod: 'MANUAL',
  isVerified: false,
  extractedDate: dayjs('2025-12-19T23:13'),
};

export const sampleWithFullData: IExtractedField = {
  id: 28314,
  documentId: 10614,
  fieldKey: 'pace gum',
  fieldValue: '../fake-data/blob/hipster.txt',
  confidence: 0.36,
  extractionMethod: 'BARCODE',
  isVerified: true,
  extractedDate: dayjs('2025-12-20T02:52'),
};

export const sampleWithNewData: NewExtractedField = {
  documentId: 13463,
  fieldKey: 'while revitalise',
  fieldValue: '../fake-data/blob/hipster.txt',
  isVerified: false,
  extractedDate: dayjs('2025-12-20T05:24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
