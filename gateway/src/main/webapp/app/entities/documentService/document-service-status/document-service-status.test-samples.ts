import dayjs from 'dayjs/esm';

import { IDocumentServiceStatus, NewDocumentServiceStatus } from './document-service-status.model';

export const sampleWithRequiredData: IDocumentServiceStatus = {
  id: 23107,
  serviceType: 'REPORTING_SERVICE',
  status: 'PENDING',
  updatedDate: dayjs('2025-12-29T17:42'),
};

export const sampleWithPartialData: IDocumentServiceStatus = {
  id: 26163,
  serviceType: 'TRANSFORM_SERVICE',
  status: 'SKIPPED',
  errorMessage: '../fake-data/blob/hipster.txt',
  retryCount: 14885,
  processingDuration: 4406,
  priority: 2628,
  updatedBy: 'mentor underneath demonstrate',
  updatedDate: dayjs('2025-12-29T12:28'),
};

export const sampleWithFullData: IDocumentServiceStatus = {
  id: 9129,
  serviceType: 'TRANSFORM_SERVICE',
  status: 'IN_PROGRESS',
  statusDetails: '../fake-data/blob/hipster.txt',
  errorMessage: '../fake-data/blob/hipster.txt',
  retryCount: 13644,
  lastProcessedDate: dayjs('2025-12-30T00:36'),
  processingStartDate: dayjs('2025-12-30T05:24'),
  processingEndDate: dayjs('2025-12-29T23:17'),
  processingDuration: 30751,
  jobId: 'and fruitful wicked',
  priority: 23773,
  updatedBy: 'plait supposing',
  updatedDate: dayjs('2025-12-29T18:39'),
};

export const sampleWithNewData: NewDocumentServiceStatus = {
  serviceType: 'SIMILARITY_SERVICE',
  status: 'CANCELLED',
  updatedDate: dayjs('2025-12-29T14:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
