import dayjs from 'dayjs/esm';

import { IDocumentComment, NewDocumentComment } from './document-comment.model';

export const sampleWithRequiredData: IDocumentComment = {
  id: 19385,
  content: '../fake-data/blob/hipster.txt',
  isResolved: false,
  authorId: 'often throughout',
  createdDate: dayjs('2025-12-29T07:11'),
};

export const sampleWithPartialData: IDocumentComment = {
  id: 4989,
  content: '../fake-data/blob/hipster.txt',
  pageNumber: 7052,
  isResolved: true,
  authorId: 'restaurant facilitate',
  createdDate: dayjs('2025-12-30T06:15'),
};

export const sampleWithFullData: IDocumentComment = {
  id: 17535,
  content: '../fake-data/blob/hipster.txt',
  pageNumber: 29580,
  isResolved: true,
  authorId: 'sequester closed',
  createdDate: dayjs('2025-12-29T11:59'),
};

export const sampleWithNewData: NewDocumentComment = {
  content: '../fake-data/blob/hipster.txt',
  isResolved: false,
  authorId: 'likely except',
  createdDate: dayjs('2025-12-29T23:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
