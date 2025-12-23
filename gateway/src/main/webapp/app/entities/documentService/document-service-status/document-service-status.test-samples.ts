import dayjs from 'dayjs/esm';

import { IDocumentServiceStatus, NewDocumentServiceStatus } from './document-service-status.model';

export const sampleWithRequiredData: IDocumentServiceStatus = {
  id: 23107,
  documentId: 29512,
  serviceType: 'WORKFLOW_SERVICE',
  status: 'COMPLETED',
  updatedDate: dayjs('2025-12-19T20:29'),
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
  updatedDate: dayjs('2025-12-19T20:33'),
};

export const sampleWithFullData: IDocumentServiceStatus = {
  id: 9129,
  documentId: 10806,
  serviceType: 'TRANSFORM_SERVICE',
  status: 'COMPLETED',
  statusDetails: '../fake-data/blob/hipster.txt',
  errorMessage: '../fake-data/blob/hipster.txt',
  retryCount: 24704,
  lastProcessedDate: dayjs('2025-12-20T14:19'),
  processingStartDate: dayjs('2025-12-20T08:12'),
  processingEndDate: dayjs('2025-12-20T13:57'),
  processingDuration: 22878,
  jobId: 'gadzooks where',
  priority: 17692,
  updatedBy: 'mediocre provision psst',
  updatedDate: dayjs('2025-12-19T22:00'),
};

export const sampleWithNewData: NewDocumentServiceStatus = {
  documentId: 18601,
  serviceType: 'BUSINESS_SERVICE',
  status: 'IN_PROGRESS',
  updatedDate: dayjs('2025-12-20T01:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
