import dayjs from 'dayjs/esm';

import { ITransformRedactionJob, NewTransformRedactionJob } from './transform-redaction-job.model';

export const sampleWithRequiredData: ITransformRedactionJob = {
  id: 32404,
  documentSha256: 'dress',
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'PIXELATE',
  status: 'COMPLETED',
  createdBy: 'yuck',
  createdDate: dayjs('2025-12-29T07:59'),
};

export const sampleWithPartialData: ITransformRedactionJob = {
  id: 8646,
  documentSha256: 'contrail',
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'BLUR',
  redactionColor: 'blossom',
  replaceWith: 'anti',
  outputS3Key: 'quietly',
  status: 'PROCESSING',
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'anenst limply',
  createdDate: dayjs('2025-12-29T13:39'),
};

export const sampleWithFullData: ITransformRedactionJob = {
  id: 31516,
  documentSha256: 'so fort',
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'PATTERN',
  redactionColor: 'why',
  replaceWith: 'save who',
  outputS3Key: 'ack',
  outputDocumentSha256: 'um',
  status: 'COMPLETED',
  startDate: dayjs('2025-12-30T02:07'),
  endDate: dayjs('2025-12-30T00:16'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'highly doubtfully',
  createdDate: dayjs('2025-12-30T03:41'),
};

export const sampleWithNewData: NewTransformRedactionJob = {
  documentSha256: 'atop',
  redactionAreas: '../fake-data/blob/hipster.txt',
  redactionType: 'BLACK_BOX',
  status: 'FAILED',
  createdBy: 'yuck ick',
  createdDate: dayjs('2025-12-30T04:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
