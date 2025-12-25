import dayjs from 'dayjs/esm';

import { IComparisonJob, NewComparisonJob } from './comparison-job.model';

export const sampleWithRequiredData: IComparisonJob = {
  id: 26672,
  documentId1: 2472,
  documentId2: 31980,
  comparisonType: 'CONTENT_HASH',
  status: 'PENDING',
  comparedDate: dayjs('2025-12-24T21:52'),
  comparedBy: 'thoroughly cruelly uh-huh',
};

export const sampleWithPartialData: IComparisonJob = {
  id: 4593,
  documentId1: 7822,
  documentId2: 16781,
  comparisonType: 'SIDE_BY_SIDE',
  differences: '../fake-data/blob/hipster.txt',
  differenceCount: 28516,
  status: 'FAILED',
  comparedDate: dayjs('2025-12-24T22:52'),
  comparedBy: 'concerning orchid neatly',
};

export const sampleWithFullData: IComparisonJob = {
  id: 14657,
  documentId1: 5998,
  documentId2: 31512,
  comparisonType: 'METADATA',
  differences: '../fake-data/blob/hipster.txt',
  differenceCount: 13090,
  similarityPercentage: 70.72,
  diffReportS3Key: 'institute er unwieldy',
  diffVisualS3Key: 'mob soupy enroll',
  status: 'FAILED',
  comparedDate: dayjs('2025-12-24T12:10'),
  comparedBy: 'print dimly than',
};

export const sampleWithNewData: NewComparisonJob = {
  documentId1: 26438,
  documentId2: 19902,
  comparisonType: 'PDF_VISUAL',
  status: 'COMPLETED',
  comparedDate: dayjs('2025-12-25T05:23'),
  comparedBy: 'wrathful',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
