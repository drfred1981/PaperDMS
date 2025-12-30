import dayjs from 'dayjs/esm';

import { IImageConversionHistory, NewImageConversionHistory } from './image-conversion-history.model';

export const sampleWithRequiredData: IImageConversionHistory = {
  id: 12713,
  originalRequestId: 28293,
  archivedAt: dayjs('2025-12-29T15:34'),
  conversionData: '../fake-data/blob/hipster.txt',
  finalStatus: 'COMPLETED',
};

export const sampleWithPartialData: IImageConversionHistory = {
  id: 25652,
  originalRequestId: 10920,
  archivedAt: dayjs('2025-12-29T18:58'),
  conversionData: '../fake-data/blob/hipster.txt',
  finalStatus: 'CANCELLED',
  processingDuration: 22655,
};

export const sampleWithFullData: IImageConversionHistory = {
  id: 19212,
  originalRequestId: 14572,
  archivedAt: dayjs('2025-12-29T22:25'),
  conversionData: '../fake-data/blob/hipster.txt',
  imagesCount: 1432,
  totalSize: 8180,
  finalStatus: 'PENDING',
  processingDuration: 7803,
};

export const sampleWithNewData: NewImageConversionHistory = {
  originalRequestId: 23247,
  archivedAt: dayjs('2025-12-29T19:03'),
  conversionData: '../fake-data/blob/hipster.txt',
  finalStatus: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
