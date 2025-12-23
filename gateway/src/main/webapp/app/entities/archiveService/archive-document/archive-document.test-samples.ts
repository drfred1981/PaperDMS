import dayjs from 'dayjs/esm';

import { IArchiveDocument, NewArchiveDocument } from './archive-document.model';

export const sampleWithRequiredData: IArchiveDocument = {
  id: 11174,
  archiveJobId: 3454,
  documentId: 20759,
  documentSha256: 'rudely until',
  addedDate: dayjs('2025-12-19T19:06'),
};

export const sampleWithPartialData: IArchiveDocument = {
  id: 26938,
  archiveJobId: 19211,
  documentId: 12763,
  documentSha256: 'geez jogging hmph',
  originalPath: 'but',
  archivePath: 'for',
  fileSize: 31481,
  addedDate: dayjs('2025-12-20T00:09'),
};

export const sampleWithFullData: IArchiveDocument = {
  id: 3721,
  archiveJobId: 15041,
  documentId: 32529,
  documentSha256: 'brush rubbery',
  originalPath: 'fort tributary',
  archivePath: 'lifestyle lovingly',
  fileSize: 10058,
  addedDate: dayjs('2025-12-19T20:50'),
};

export const sampleWithNewData: NewArchiveDocument = {
  archiveJobId: 28509,
  documentId: 23448,
  documentSha256: 'coexist elevator drat',
  addedDate: dayjs('2025-12-20T11:32'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
