import dayjs from 'dayjs/esm';

import { IDocumentComment, NewDocumentComment } from './document-comment.model';

export const sampleWithRequiredData: IDocumentComment = {
  id: 19385,
  documentId: 30316,
  content: '../fake-data/blob/hipster.txt',
  isResolved: true,
  authorId: 'orchestrate agreeable co-producer',
  createdDate: dayjs('2025-12-19T17:16'),
};

export const sampleWithPartialData: IDocumentComment = {
  id: 4989,
  documentId: 7052,
  content: '../fake-data/blob/hipster.txt',
  pageNumber: 5441,
  isResolved: true,
  authorId: 'premier wear twist',
  createdDate: dayjs('2025-12-20T15:20'),
};

export const sampleWithFullData: IDocumentComment = {
  id: 17535,
  documentId: 29580,
  content: '../fake-data/blob/hipster.txt',
  pageNumber: 12977,
  isResolved: true,
  authorId: 'waterlogged',
  createdDate: dayjs('2025-12-19T19:39'),
};

export const sampleWithNewData: NewDocumentComment = {
  documentId: 18549,
  content: '../fake-data/blob/hipster.txt',
  isResolved: true,
  authorId: 'supposing',
  createdDate: dayjs('2025-12-20T01:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
