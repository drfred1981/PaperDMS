import dayjs from 'dayjs/esm';

import { IImageGeneratedImage, NewImageGeneratedImage } from './image-generated-image.model';

export const sampleWithRequiredData: IImageGeneratedImage = {
  id: 10589,
  pageNumber: 32501,
  fileName: 'whine gadzooks',
  s3Key: 'amongst whether',
  format: 'JPEG',
  quality: 'MEDIUM',
  width: 9853,
  height: 10546,
  fileSize: 27959,
  dpi: 21880,
  generatedAt: dayjs('2025-12-30T00:25'),
};

export const sampleWithPartialData: IImageGeneratedImage = {
  id: 25555,
  pageNumber: 23739,
  fileName: 'wrongly wherever trench',
  s3Key: 'er coordinated',
  preSignedUrl: 'proud',
  format: 'JPEG',
  quality: 'LOW',
  width: 19754,
  height: 24350,
  fileSize: 32523,
  dpi: 299,
  generatedAt: dayjs('2025-12-29T22:03'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IImageGeneratedImage = {
  id: 20087,
  pageNumber: 624,
  fileName: 'pulp',
  s3Key: 'debut',
  preSignedUrl: 'almost afore neck',
  urlExpiresAt: dayjs('2025-12-29T08:26'),
  format: 'PNG',
  quality: 'HIGH',
  width: 1711,
  height: 11756,
  fileSize: 21760,
  dpi: 27629,
  sha256Hash: 'so',
  generatedAt: dayjs('2025-12-29T10:47'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewImageGeneratedImage = {
  pageNumber: 15713,
  fileName: 'golden incidentally',
  s3Key: 'ack hence by',
  format: 'JPEG',
  quality: 'LOW',
  width: 10948,
  height: 25907,
  fileSize: 19863,
  dpi: 13385,
  generatedAt: dayjs('2025-12-29T18:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
