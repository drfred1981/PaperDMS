import dayjs from 'dayjs/esm';

import { IDocumentServiceStatus, NewDocumentServiceStatus } from './document-service-status.model';

export const sampleWithRequiredData: IDocumentServiceStatus = {
  id: 23107,
  documentId: 29512,
  serviceType: 'WORKFLOW_SERVICE',
  status: 'COMPLETED',
  updatedDate: dayjs('2025-12-24T15:28'),
};

export const sampleWithPartialData: IDocumentServiceStatus = {
  id: 26163,
  documentId: 12333,
  serviceType: 'SCAN_SERVICE',
  status: 'COMPLETED',
  errorMessage: '../fake-data/blob/hipster.txt',
  retryCount: 4406,
  processingDuration: 2628,
  priority: 21963,
  updatedBy: 'lyre digitize hence',
  updatedDate: dayjs('2025-12-24T15:32'),
};

export const sampleWithFullData: IDocumentServiceStatus = {
  id: 9129,
  documentId: 10806,
  serviceType: 'TRANSFORM_SERVICE',
  status: 'COMPLETED',
  statusDetails: '../fake-data/blob/hipster.txt',
  errorMessage: '../fake-data/blob/hipster.txt',
  retryCount: 24704,
  lastProcessedDate: dayjs('2025-12-25T09:18'),
  processingStartDate: dayjs('2025-12-25T03:11'),
  processingEndDate: dayjs('2025-12-25T08:56'),
  processingDuration: 22878,
  jobId: 'gadzooks where',
  priority: 17692,
  updatedBy: 'mediocre provision psst',
  updatedDate: dayjs('2025-12-24T16:59'),
};

export const sampleWithNewData: NewDocumentServiceStatus = {
  documentId: 18601,
  serviceType: 'BUSINESS_SERVICE',
  status: 'IN_PROGRESS',
  updatedDate: dayjs('2025-12-24T20:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
