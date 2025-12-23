import dayjs from 'dayjs/esm';

import { IScannedPage, NewScannedPage } from './scanned-page.model';

export const sampleWithRequiredData: IScannedPage = {
  id: 22442,
  scanJobId: 18115,
  pageNumber: 12291,
  sha256: 'tremendously descendant digestive',
  s3Key: 'quietly zowie',
  scannedDate: dayjs('2025-12-20T09:23'),
};

export const sampleWithPartialData: IScannedPage = {
  id: 11100,
  scanJobId: 24325,
  pageNumber: 1228,
  sha256: 'godfather',
  s3Key: 'kick',
  fileSize: 17127,
  scannedDate: dayjs('2025-12-19T17:10'),
};

export const sampleWithFullData: IScannedPage = {
  id: 18368,
  scanJobId: 14674,
  pageNumber: 29712,
  sha256: 'after',
  s3Key: 'nicely almighty',
  s3PreviewKey: 'gee along',
  fileSize: 23591,
  width: 14467,
  height: 26905,
  dpi: 23082,
  documentId: 31842,
  scannedDate: dayjs('2025-12-20T09:20'),
};

export const sampleWithNewData: NewScannedPage = {
  scanJobId: 8717,
  pageNumber: 9562,
  sha256: 'gee indeed',
  s3Key: 'poetry excepting',
  scannedDate: dayjs('2025-12-20T15:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
