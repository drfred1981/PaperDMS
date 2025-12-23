import dayjs from 'dayjs/esm';

import { IWatermarkJob, NewWatermarkJob } from './watermark-job.model';

export const sampleWithRequiredData: IWatermarkJob = {
  id: 8906,
  documentId: 26913,
  watermarkType: 'TEXT',
  position: 'TOP_RIGHT',
  status: 'PENDING',
  createdBy: 'brr eek',
  createdDate: dayjs('2025-12-19T18:16'),
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
  startDate: dayjs('2025-12-20T02:02'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'eminent ha',
  createdDate: dayjs('2025-12-20T01:22'),
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
  startDate: dayjs('2025-12-19T20:08'),
  endDate: dayjs('2025-12-20T03:18'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'penalise suffocate helpfully',
  createdDate: dayjs('2025-12-19T23:26'),
};

export const sampleWithNewData: NewWatermarkJob = {
  documentId: 15729,
  watermarkType: 'INVISIBLE',
  position: 'BOTTOM_CENTER',
  status: 'PROCESSING',
  createdBy: 'poorly stylish',
  createdDate: dayjs('2025-12-19T23:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
