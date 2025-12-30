import dayjs from 'dayjs/esm';

import { IDocumentVersion, NewDocumentVersion } from './document-version.model';

export const sampleWithRequiredData: IDocumentVersion = {
  id: 28658,
  versionNumber: 12051,
  sha256: 'athwart although',
  s3Key: 'golden',
  fileSize: 3803,
  uploadDate: dayjs('2025-12-29T12:22'),
  isActive: true,
  createdBy: 'past made-up annually',
};

export const sampleWithPartialData: IDocumentVersion = {
  id: 21592,
  versionNumber: 23520,
  sha256: 'parody',
  s3Key: 'gulp ew across',
  fileSize: 25012,
  uploadDate: dayjs('2025-12-29T10:34'),
  isActive: false,
  createdBy: 'settle pfft psst',
};

export const sampleWithFullData: IDocumentVersion = {
  id: 23123,
  versionNumber: 4180,
  sha256: 'rebound sport',
  s3Key: 'until',
  fileSize: 497,
  uploadDate: dayjs('2025-12-30T04:48'),
  isActive: true,
  createdBy: 'yum',
};

export const sampleWithNewData: NewDocumentVersion = {
  versionNumber: 4961,
  sha256: 'towards ouch till',
  s3Key: 'down however',
  fileSize: 30190,
  uploadDate: dayjs('2025-12-29T19:08'),
  isActive: false,
  createdBy: 'who',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
