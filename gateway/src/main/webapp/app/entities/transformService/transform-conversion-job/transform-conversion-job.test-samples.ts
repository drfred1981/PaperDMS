import dayjs from 'dayjs/esm';

import { ITransformConversionJob, NewTransformConversionJob } from './transform-conversion-job.model';

export const sampleWithRequiredData: ITransformConversionJob = {
  id: 115,
  documentSha256: 'fooey clavicle delicious',
  sourceFormat: 'towards',
  targetFormat: 'beside',
  status: 'PENDING',
  createdBy: 'aha',
  createdDate: dayjs('2025-12-29T21:07'),
};

export const sampleWithPartialData: ITransformConversionJob = {
  id: 19295,
  documentSha256: 'overload uh-huh academics',
  sourceFormat: 'how consequently freight',
  targetFormat: 'complicated trivial',
  outputS3Key: 'firsthand stool whereas',
  status: 'FAILED',
  startDate: dayjs('2025-12-30T03:41'),
  createdBy: 'fort',
  createdDate: dayjs('2025-12-29T21:00'),
};

export const sampleWithFullData: ITransformConversionJob = {
  id: 18375,
  documentSha256: 'as rapidly an',
  sourceFormat: 'lest enraged zowie',
  targetFormat: 'gosh',
  conversionEngine: 'obnoxiously meanwhile',
  options: '../fake-data/blob/hipster.txt',
  outputS3Key: 'cinch',
  outputDocumentSha256: 'director energetic',
  status: 'CANCELLED',
  startDate: dayjs('2025-12-29T18:28'),
  endDate: dayjs('2025-12-30T05:27'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'failing readily backburn',
  createdDate: dayjs('2025-12-29T20:49'),
};

export const sampleWithNewData: NewTransformConversionJob = {
  documentSha256: 'if onto license',
  sourceFormat: 'ha since greedily',
  targetFormat: 'to yieldingly sneaky',
  status: 'CANCELLED',
  createdBy: 'as mobilize as',
  createdDate: dayjs('2025-12-29T19:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
