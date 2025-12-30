import dayjs from 'dayjs/esm';

import { IDocumentExtractedField, NewDocumentExtractedField } from './document-extracted-field.model';

export const sampleWithRequiredData: IDocumentExtractedField = {
  id: 4285,
  fieldKey: 'properly',
  fieldValue: '../fake-data/blob/hipster.txt',
  isVerified: false,
  extractedDate: dayjs('2025-12-30T06:12'),
};

export const sampleWithPartialData: IDocumentExtractedField = {
  id: 3046,
  fieldKey: 'sashay',
  fieldValue: '../fake-data/blob/hipster.txt',
  confidence: 0.61,
  extractionMethod: 'BARCODE',
  isVerified: false,
  extractedDate: dayjs('2025-12-29T11:18'),
};

export const sampleWithFullData: IDocumentExtractedField = {
  id: 27823,
  fieldKey: 'through impanel frizzy',
  fieldValue: '../fake-data/blob/hipster.txt',
  confidence: 0.99,
  extractionMethod: 'OCR',
  isVerified: false,
  extractedDate: dayjs('2025-12-30T04:02'),
};

export const sampleWithNewData: NewDocumentExtractedField = {
  fieldKey: 'quarrelsome',
  fieldValue: '../fake-data/blob/hipster.txt',
  isVerified: true,
  extractedDate: dayjs('2025-12-29T16:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
