import { IDocumentProcess, NewDocumentProcess } from './document-process.model';

export const sampleWithRequiredData: IDocumentProcess = {
  id: 28636,
  documentSha256: 'colon duh',
};

export const sampleWithPartialData: IDocumentProcess = {
  id: 25512,
  documentSha256: 'for',
};

export const sampleWithFullData: IDocumentProcess = {
  id: 31647,
  status: 'CANCELLED',
  documentSha256: 'flawless broken abaft',
};

export const sampleWithNewData: NewDocumentProcess = {
  documentSha256: 'mechanically',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
