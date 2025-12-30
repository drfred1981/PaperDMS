import dayjs from 'dayjs/esm';

import { ITransformWatermarkJob, NewTransformWatermarkJob } from './transform-watermark-job.model';

export const sampleWithRequiredData: ITransformWatermarkJob = {
  id: 14474,
  documentSha256: 'negative geez',
  watermarkType: 'IMAGE',
  position: 'TOP_LEFT',
  status: 'PROCESSING',
  createdBy: 'boohoo for',
  createdDate: dayjs('2025-12-29T15:02'),
};

export const sampleWithPartialData: ITransformWatermarkJob = {
  id: 22374,
  documentSha256: 'goose',
  watermarkType: 'QR_CODE',
  watermarkText: 'whose dental',
  position: 'DIAGONAL',
  opacity: 63,
  color: 'cyan',
  rotation: 14071,
  tiled: false,
  outputS3Key: 'crossly place',
  status: 'CANCELLED',
  startDate: dayjs('2025-12-29T19:40'),
  endDate: dayjs('2025-12-29T10:15'),
  createdBy: 'although birdcage',
  createdDate: dayjs('2025-12-29T14:38'),
};

export const sampleWithFullData: ITransformWatermarkJob = {
  id: 1558,
  documentSha256: 'space brr',
  watermarkType: 'TEXT',
  watermarkText: 'lest tensely attraction',
  watermarkImageS3Key: 'uh-huh',
  position: 'CENTER',
  opacity: 3,
  fontSize: 10178,
  color: 'orange',
  rotation: 17795,
  tiled: true,
  outputS3Key: 'never when foolishly',
  outputDocumentSha256: 'forenenst indeed quintuple',
  status: 'PENDING',
  startDate: dayjs('2025-12-29T22:27'),
  endDate: dayjs('2025-12-29T10:42'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'entire vastly appertain',
  createdDate: dayjs('2025-12-29T23:24'),
};

export const sampleWithNewData: NewTransformWatermarkJob = {
  documentSha256: 'aged',
  watermarkType: 'TEXT',
  position: 'DIAGONAL',
  status: 'CANCELLED',
  createdBy: 'joshingly cleverly',
  createdDate: dayjs('2025-12-29T09:47'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
