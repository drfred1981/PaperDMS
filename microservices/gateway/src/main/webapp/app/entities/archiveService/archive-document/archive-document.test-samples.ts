import dayjs from 'dayjs/esm';

import { IArchiveDocument, NewArchiveDocument } from './archive-document.model';

export const sampleWithRequiredData: IArchiveDocument = {
  id: 11174,
  documentSha256: 'phooey',
  addedDate: dayjs('2025-12-29T08:51'),
};

export const sampleWithPartialData: IArchiveDocument = {
  id: 26938,
  documentSha256: 'brr psst',
  originalPath: 'though gift pish',
  archivePath: 'stunt boohoo',
  fileSize: 2435,
  addedDate: dayjs('2025-12-30T06:31'),
};

export const sampleWithFullData: IArchiveDocument = {
  id: 3721,
  documentSha256: 'violently versus',
  originalPath: 'out queasily yahoo',
  archivePath: 'spotless till quirkily',
  fileSize: 3065,
  addedDate: dayjs('2025-12-29T11:36'),
};

export const sampleWithNewData: NewArchiveDocument = {
  documentSha256: 'meanwhile and gadzooks',
  addedDate: dayjs('2025-12-29T17:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
