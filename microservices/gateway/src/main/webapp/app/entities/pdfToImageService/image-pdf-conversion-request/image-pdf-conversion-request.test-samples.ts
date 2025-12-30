import dayjs from 'dayjs/esm';

import { IImagePdfConversionRequest, NewImagePdfConversionRequest } from './image-pdf-conversion-request.model';

export const sampleWithRequiredData: IImagePdfConversionRequest = {
  id: 9525,
  sourceDocumentId: 14950,
  sourceFileName: 'amid reluctantly oof',
  sourcePdfS3Key: 'roughly gadzooks stale',
  imageQuality: 'MEDIUM',
  imageFormat: 'JPEG',
  conversionType: 'FIRST_PAGE_ONLY',
  status: 'CANCELLED',
  requestedAt: dayjs('2025-12-29T22:19'),
};

export const sampleWithPartialData: IImagePdfConversionRequest = {
  id: 11710,
  sourceDocumentId: 31885,
  sourceFileName: 'whenever daughter but',
  sourcePdfS3Key: 'and pish',
  imageQuality: 'ULTRA',
  imageFormat: 'JPEG',
  conversionType: 'FIRST_PAGE_ONLY',
  totalPages: 11360,
  status: 'FAILED',
  requestedAt: dayjs('2025-12-29T11:04'),
  processingDuration: 17214,
  imagesGenerated: 13124,
  dpi: 334,
  requestedByUserId: 1336,
};

export const sampleWithFullData: IImagePdfConversionRequest = {
  id: 7206,
  sourceDocumentId: 14944,
  sourceFileName: 'quip the',
  sourcePdfS3Key: 'insist justly terribly',
  imageQuality: 'HIGH',
  imageFormat: 'JPEG',
  conversionType: 'ALL_PAGES',
  startPage: 13910,
  endPage: 3537,
  totalPages: 28931,
  status: 'PENDING',
  errorMessage: 'satirize whether tomb',
  requestedAt: dayjs('2025-12-30T05:15'),
  startedAt: dayjs('2025-12-30T00:21'),
  completedAt: dayjs('2025-12-29T12:23'),
  processingDuration: 16674,
  totalImagesSize: 30466,
  imagesGenerated: 11211,
  dpi: 510,
  requestedByUserId: 9669,
  priority: 4,
  additionalOptions: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewImagePdfConversionRequest = {
  sourceDocumentId: 31376,
  sourceFileName: 'consequently',
  sourcePdfS3Key: 'cuckoo selfishly',
  imageQuality: 'LOW',
  imageFormat: 'WEBP',
  conversionType: 'FIRST_PAGE_ONLY',
  status: 'CANCELLED',
  requestedAt: dayjs('2025-12-29T14:23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
