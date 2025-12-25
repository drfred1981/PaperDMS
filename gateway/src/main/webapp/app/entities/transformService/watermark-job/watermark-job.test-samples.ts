import dayjs from 'dayjs/esm';

import { IWatermarkJob, NewWatermarkJob } from './watermark-job.model';

export const sampleWithRequiredData: IWatermarkJob = {
  id: 8906,
  documentId: 26913,
  watermarkType: 'TEXT',
  position: 'TOP_RIGHT',
  status: 'PENDING',
  createdBy: 'brr eek',
  createdDate: dayjs('2025-12-24T13:15'),
};

export const sampleWithPartialData: IWatermarkJob = {
  id: 3837,
  documentId: 30358,
  watermarkType: 'TEXT',
  watermarkText: 'revere before outnumber',
  watermarkImageS3Key: 'eyeliner rewrite ick',
  position: 'DIAGONAL',
  opacity: 72,
  fontSize: 31948,
  color: 'orange',
  status: 'PROCESSING',
  startDate: dayjs('2025-12-24T21:01'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'eminent ha',
  createdDate: dayjs('2025-12-24T20:21'),
};

export const sampleWithFullData: IWatermarkJob = {
  id: 16689,
  documentId: 20591,
  watermarkType: 'TEXT',
  watermarkText: 'down mountain kinase',
  watermarkImageS3Key: 'boohoo snappy wisely',
  position: 'TOP_RIGHT',
  opacity: 71,
  fontSize: 19061,
  color: 'fuchsia',
  rotation: 30471,
  tiled: true,
  outputS3Key: 'bitterly',
  outputDocumentId: 13726,
  status: 'COMPLETED',
  startDate: dayjs('2025-12-24T15:07'),
  endDate: dayjs('2025-12-24T22:17'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'penalise suffocate helpfully',
  createdDate: dayjs('2025-12-24T18:25'),
};

export const sampleWithNewData: NewWatermarkJob = {
  documentId: 15729,
  watermarkType: 'INVISIBLE',
  position: 'BOTTOM_CENTER',
  status: 'PROCESSING',
  createdBy: 'poorly stylish',
  createdDate: dayjs('2025-12-24T18:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
