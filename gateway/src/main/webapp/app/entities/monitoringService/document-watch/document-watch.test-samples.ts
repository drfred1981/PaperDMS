import dayjs from 'dayjs/esm';

import { IDocumentWatch, NewDocumentWatch } from './document-watch.model';

export const sampleWithRequiredData: IDocumentWatch = {
  id: 12198,
  documentId: 29538,
  userId: 'thyme eek frenetically',
  watchType: 'REVIEWER',
  notifyOnView: true,
  notifyOnDownload: false,
  notifyOnModify: false,
  notifyOnShare: true,
  notifyOnDelete: false,
  createdDate: dayjs('2025-12-25T07:52'),
};

export const sampleWithPartialData: IDocumentWatch = {
  id: 26489,
  documentId: 21819,
  userId: 'prickly',
  watchType: 'REVIEWER',
  notifyOnView: false,
  notifyOnDownload: true,
  notifyOnModify: true,
  notifyOnShare: true,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-24T16:45'),
};

export const sampleWithFullData: IDocumentWatch = {
  id: 22407,
  documentId: 3953,
  userId: 'surprisingly coincide',
  watchType: 'REVIEWER',
  notifyOnView: false,
  notifyOnDownload: false,
  notifyOnModify: true,
  notifyOnShare: true,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-25T10:58'),
};

export const sampleWithNewData: NewDocumentWatch = {
  documentId: 12886,
  userId: 'round',
  watchType: 'STAKEHOLDER',
  notifyOnView: false,
  notifyOnDownload: false,
  notifyOnModify: false,
  notifyOnShare: false,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-25T03:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
