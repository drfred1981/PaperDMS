import { IDocumentProcess, NewDocumentProcess } from './document-process.model';

export const sampleWithRequiredData: IDocumentProcess = {
  id: 28636,
  documentId: 18193,
  documentSha256: 'dearly aha',
};

export const sampleWithPartialData: IDocumentProcess = {
  id: 25512,
  documentId: 2420,
  documentSha256: 'emphasize wherever meanwhile',
};

export const sampleWithFullData: IDocumentProcess = {
  id: 31647,
  status: 'CANCELLED',
  documentId: 30090,
  documentSha256: 'ack yowza',
};

export const sampleWithNewData: NewDocumentProcess = {
  documentId: 9213,
  documentSha256: 'ghost breed jiggle',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
