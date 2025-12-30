import dayjs from 'dayjs/esm';

import { IImageConversionBatch, NewImageConversionBatch } from './image-conversion-batch.model';

export const sampleWithRequiredData: IImageConversionBatch = {
  id: 11997,
  batchName: 'unaware via yahoo',
  createdAt: dayjs('2025-12-29T16:46'),
  status: 'COMPLETED',
};

export const sampleWithPartialData: IImageConversionBatch = {
  id: 11174,
  batchName: 'plus',
  createdAt: dayjs('2025-12-29T08:47'),
  status: 'CANCELLED',
  totalConversions: 6804,
  completedConversions: 13734,
  startedAt: dayjs('2025-12-29T09:51'),
  completedAt: dayjs('2025-12-29T16:14'),
};

export const sampleWithFullData: IImageConversionBatch = {
  id: 7885,
  batchName: 'ah',
  description: 'spear for stealthily',
  createdAt: dayjs('2025-12-29T16:25'),
  status: 'PENDING',
  totalConversions: 8613,
  completedConversions: 16702,
  failedConversions: 16776,
  startedAt: dayjs('2025-12-29T14:03'),
  completedAt: dayjs('2025-12-29T09:31'),
  totalProcessingDuration: 19847,
  createdByUserId: 3808,
};

export const sampleWithNewData: NewImageConversionBatch = {
  batchName: 'knickers',
  createdAt: dayjs('2025-12-29T13:12'),
  status: 'PROCESSING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
