import dayjs from 'dayjs/esm';

import { ITransformCompressionJob, NewTransformCompressionJob } from './transform-compression-job.model';

export const sampleWithRequiredData: ITransformCompressionJob = {
  id: 31669,
  documentSha256: 'furthermore jubilantly',
  compressionType: 'LOSSY',
  status: 'PROCESSING',
  createdBy: 'noon agreeable',
  createdDate: dayjs('2025-12-29T22:32'),
};

export const sampleWithPartialData: ITransformCompressionJob = {
  id: 10148,
  documentSha256: 'apud priesthood',
  compressionType: 'LOSSLESS',
  quality: 62,
  targetSizeKb: 30944,
  originalSize: 1381,
  compressionRatio: 334.83,
  outputS3Key: 'until safeguard',
  status: 'FAILED',
  startDate: dayjs('2025-12-30T05:05'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'shy inside why',
  createdDate: dayjs('2025-12-30T03:09'),
};

export const sampleWithFullData: ITransformCompressionJob = {
  id: 11830,
  documentSha256: 'finally mmm below',
  compressionType: 'LOSSY',
  quality: 94,
  targetSizeKb: 11097,
  originalSize: 10715,
  compressedSize: 25786,
  compressionRatio: 29356.11,
  outputS3Key: 'haul aftermath anenst',
  outputDocumentSha256: 'afore meh',
  status: 'PENDING',
  startDate: dayjs('2025-12-30T01:22'),
  endDate: dayjs('2025-12-29T08:13'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'seemingly',
  createdDate: dayjs('2025-12-29T07:49'),
};

export const sampleWithNewData: NewTransformCompressionJob = {
  documentSha256: 'especially',
  compressionType: 'LOSSY',
  status: 'PENDING',
  createdBy: 'filthy via',
  createdDate: dayjs('2025-12-29T16:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
