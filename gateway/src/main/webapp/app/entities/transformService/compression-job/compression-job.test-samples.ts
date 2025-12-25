import dayjs from 'dayjs/esm';

import { ICompressionJob, NewCompressionJob } from './compression-job.model';

export const sampleWithRequiredData: ICompressionJob = {
  id: 11248,
  documentId: 23050,
  compressionType: 'LOSSY',
  status: 'CANCELLED',
  createdBy: 'pace brr yuck',
  createdDate: dayjs('2025-12-25T05:30'),
};

export const sampleWithPartialData: ICompressionJob = {
  id: 17676,
  documentId: 31351,
  compressionType: 'ADAPTIVE',
  outputDocumentId: 1147,
  status: 'PROCESSING',
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'shipper whoever',
  createdDate: dayjs('2025-12-24T22:40'),
};

export const sampleWithFullData: ICompressionJob = {
  id: 13662,
  documentId: 7541,
  compressionType: 'LOSSY',
  quality: 100,
  targetSizeKb: 23828,
  originalSize: 26770,
  compressedSize: 17320,
  compressionRatio: 7488.96,
  outputS3Key: 'what humor',
  outputDocumentId: 23410,
  status: 'CANCELLED',
  startDate: dayjs('2025-12-24T20:11'),
  endDate: dayjs('2025-12-24T23:05'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'anti but',
  createdDate: dayjs('2025-12-24T18:44'),
};

export const sampleWithNewData: NewCompressionJob = {
  documentId: 3384,
  compressionType: 'LOSSY',
  status: 'PENDING',
  createdBy: 'cemetery blank',
  createdDate: dayjs('2025-12-24T17:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
