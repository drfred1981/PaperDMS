import dayjs from 'dayjs/esm';

import { IArchiveJob, NewArchiveJob } from './archive-job.model';

export const sampleWithRequiredData: IArchiveJob = {
  id: 13491,
  name: 'tousle recent gadzooks',
  documentQuery: '../fake-data/blob/hipster.txt',
  archiveFormat: 'TAR_GZ',
  encryptionEnabled: true,
  status: 'ARCHIVING',
  createdBy: 'finally hard-to-find',
  createdDate: dayjs('2025-12-29T10:25'),
};

export const sampleWithPartialData: IArchiveJob = {
  id: 7720,
  name: 'reconsideration selfishly lyre',
  documentQuery: '../fake-data/blob/hipster.txt',
  archiveFormat: 'ZIP',
  compressionLevel: 3,
  encryptionEnabled: true,
  encryptionAlgorithm: 'address',
  archiveSha256: 'when seriously positively',
  archiveSize: 26684,
  documentCount: 25324,
  status: 'COMPLETED',
  endDate: dayjs('2025-12-30T05:31'),
  createdBy: 'phew',
  createdDate: dayjs('2025-12-29T17:34'),
};

export const sampleWithFullData: IArchiveJob = {
  id: 7125,
  name: 'often sham dearly',
  description: '../fake-data/blob/hipster.txt',
  documentQuery: '../fake-data/blob/hipster.txt',
  archiveFormat: 'TAR_XZ',
  compressionLevel: 1,
  encryptionEnabled: true,
  encryptionAlgorithm: 'tomorrow',
  password: 'well thump down',
  s3ArchiveKey: 'instance the',
  archiveSha256: 'pronoun ugh following',
  archiveSize: 26837,
  documentCount: 7382,
  status: 'COMPLETED',
  startDate: dayjs('2025-12-29T14:39'),
  endDate: dayjs('2025-12-29T13:16'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'sedately',
  createdDate: dayjs('2025-12-29T16:54'),
};

export const sampleWithNewData: NewArchiveJob = {
  name: 'dimly',
  documentQuery: '../fake-data/blob/hipster.txt',
  archiveFormat: 'TAR_XZ',
  encryptionEnabled: false,
  status: 'CANCELLED',
  createdBy: 'fireplace soulful',
  createdDate: dayjs('2025-12-29T22:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
