import dayjs from 'dayjs/esm';

import { IMonitoringDocumentWatch, NewMonitoringDocumentWatch } from './monitoring-document-watch.model';

export const sampleWithRequiredData: IMonitoringDocumentWatch = {
  id: 8847,
  userId: 'huzzah',
  watchType: 'REVIEWER',
  notifyOnView: false,
  notifyOnDownload: true,
  notifyOnModify: true,
  notifyOnShare: false,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-29T20:57'),
};

export const sampleWithPartialData: IMonitoringDocumentWatch = {
  id: 15492,
  documentSha256: 'inside than simplistic',
  userId: 'fully',
  watchType: 'AUDITOR',
  notifyOnView: false,
  notifyOnDownload: false,
  notifyOnModify: true,
  notifyOnShare: false,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-29T17:50'),
};

export const sampleWithFullData: IMonitoringDocumentWatch = {
  id: 15180,
  documentSha256: 'basket',
  userId: 'than',
  watchType: 'AUDITOR',
  notifyOnView: true,
  notifyOnDownload: true,
  notifyOnModify: false,
  notifyOnShare: true,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-29T18:32'),
};

export const sampleWithNewData: NewMonitoringDocumentWatch = {
  userId: 'along woot usually',
  watchType: 'REVIEWER',
  notifyOnView: true,
  notifyOnDownload: true,
  notifyOnModify: true,
  notifyOnShare: true,
  notifyOnDelete: true,
  createdDate: dayjs('2025-12-30T07:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
