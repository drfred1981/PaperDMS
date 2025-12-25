import dayjs from 'dayjs/esm';

import { IScanBatch, NewScanBatch } from './scan-batch.model';

export const sampleWithRequiredData: IScanBatch = {
  id: 10420,
  name: 'how ah',
  status: 'FAILED',
  createdBy: 'atop machine miserably',
  createdDate: dayjs('2025-12-24T12:15'),
};

export const sampleWithPartialData: IScanBatch = {
  id: 11404,
  name: 'out supposing',
  status: 'PARTIAL',
  createdBy: 'enormously keenly yesterday',
  createdDate: dayjs('2025-12-25T05:33'),
};

export const sampleWithFullData: IScanBatch = {
  id: 5817,
  name: 'dapper responsible',
  description: '../fake-data/blob/hipster.txt',
  totalJobs: 28241,
  completedJobs: 9801,
  totalPages: 30220,
  status: 'COMPLETED',
  createdBy: 'profane dull',
  createdDate: dayjs('2025-12-25T08:01'),
};

export const sampleWithNewData: NewScanBatch = {
  name: 'across starboard',
  status: 'PENDING',
  createdBy: 'hawk prime',
  createdDate: dayjs('2025-12-25T05:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
