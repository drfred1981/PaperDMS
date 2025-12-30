import dayjs from 'dayjs/esm';

import { IOcrJob, NewOcrJob } from './ocr-job.model';

export const sampleWithRequiredData: IOcrJob = {
  id: 6011,
  status: 'FAILED',
  documentSha256: 'briskly huzzah yum',
  s3Key: 'readily banish forage',
  s3Bucket: 'supposing appliance',
  isCached: false,
  createdDate: dayjs('2025-12-29T13:30'),
  createdBy: 'that till digital',
};

export const sampleWithPartialData: IOcrJob = {
  id: 8514,
  status: 'COMPARING',
  documentSha256: 'lashes',
  s3Key: 'hm',
  s3Bucket: 'quaff farm',
  detectedLanguage: 'till yum',
  aiModel: 'powerful ridge',
  resultCacheKey: 'um tuber',
  isCached: true,
  startDate: dayjs('2025-12-29T12:12'),
  endDate: dayjs('2025-12-30T02:07'),
  progress: 83,
  createdDate: dayjs('2025-12-29T19:36'),
  createdBy: 'emphasise',
};

export const sampleWithFullData: IOcrJob = {
  id: 16434,
  status: 'CANCELLED',
  documentSha256: 'between',
  s3Key: 'that voluntarily though',
  s3Bucket: 'while',
  requestedLanguage: 'soggy',
  detectedLanguage: 'barring hm',
  languageConfidence: 0.77,
  ocrEngine: 'AWS_TEXTRACT',
  tikaEndpoint: 'untimely nucleotidase',
  aiProvider: 'radiant like bouncy',
  aiModel: 'adjourn wearily zowie',
  resultCacheKey: 'daintily topsail',
  isCached: true,
  startDate: dayjs('2025-12-29T10:07'),
  endDate: dayjs('2025-12-29T07:07'),
  errorMessage: '../fake-data/blob/hipster.txt',
  pageCount: 19561,
  progress: 69,
  retryCount: 11613,
  priority: 20920,
  processingTime: 29969,
  costEstimate: 7011.46,
  createdDate: dayjs('2025-12-29T16:28'),
  createdBy: 'well-lit chip boohoo',
};

export const sampleWithNewData: NewOcrJob = {
  status: 'RETRYING',
  documentSha256: 'when warmhearted',
  s3Key: 'edge rejigger kielbasa',
  s3Bucket: 'growing inquisitively',
  isCached: true,
  createdDate: dayjs('2025-12-29T07:21'),
  createdBy: 'whenever',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
