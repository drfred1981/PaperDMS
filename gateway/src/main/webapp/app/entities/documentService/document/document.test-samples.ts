import dayjs from 'dayjs/esm';

import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 26644,
  title: 'incidentally before',
  fileName: 'pish thankfully',
  fileSize: 25318,
  mimeType: 'lasting roughly implode',
  sha256: 'ack able promptly',
  s3Key: 'mealy',
  s3Bucket: 'alongside redact',
  status: 'UPLOADING',
  uploadDate: dayjs('2025-12-20T01:39'),
  isPublic: true,
  createdDate: dayjs('2025-12-19T21:18'),
  createdBy: 'admonish',
};

export const sampleWithPartialData: IDocument = {
  id: 5780,
  title: 'bakeware wearily likewise',
  fileName: 'absent pigsty',
  fileSize: 11573,
  mimeType: 'ceramics yippee',
  sha256: 'put',
  s3Key: 'indeed since solution',
  s3Bucket: 'dreamily aw',
  s3Etag: 'playfully however anti',
  thumbnailSha256: 'nervously',
  webpPreviewS3Key: 'stingy hopeful lest',
  status: 'UPLOADING',
  uploadDate: dayjs('2025-12-20T12:47'),
  isPublic: true,
  downloadCount: 4706,
  viewCount: 17513,
  detectedLanguage: 'hospitable',
  manualLanguage: 'capitalize',
  languageConfidence: 0.41,
  createdDate: dayjs('2025-12-20T08:13'),
  createdBy: 'whoa',
};

export const sampleWithFullData: IDocument = {
  id: 31599,
  title: 'chasuble',
  fileName: 'happy-go-lucky mallard forenenst',
  fileSize: 29696,
  mimeType: 'smoothly till',
  sha256: 'memorable uh-huh mostly',
  s3Key: 'sticker',
  s3Bucket: 'frizzy whose',
  s3Region: 'deploy importance',
  s3Etag: 'nor',
  thumbnailS3Key: 'meh raw',
  thumbnailSha256: 'categorise for whenever',
  webpPreviewS3Key: 'provided crazy but',
  webpPreviewSha256: 'flawless evenly dishonor',
  status: 'UPLOADED',
  uploadDate: dayjs('2025-12-19T17:17'),
  isPublic: false,
  downloadCount: 29408,
  viewCount: 23244,
  detectedLanguage: 'annex stic',
  manualLanguage: 'these as',
  languageConfidence: 0.71,
  pageCount: 8217,
  createdDate: dayjs('2025-12-20T02:21'),
  createdBy: 'circa',
};

export const sampleWithNewData: NewDocument = {
  title: 'vain',
  fileName: 'geez furthermore',
  fileSize: 19763,
  mimeType: 'reschedule failing',
  sha256: 'circular ouch',
  s3Key: 'madly relaunch',
  s3Bucket: 'fatal',
  status: 'UPLOADED',
  uploadDate: dayjs('2025-12-20T01:03'),
  isPublic: false,
  createdDate: dayjs('2025-12-20T04:58'),
  createdBy: 'yet unethically upsell',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
