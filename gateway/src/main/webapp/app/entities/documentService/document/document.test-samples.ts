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
  uploadDate: dayjs('2025-12-24T14:15'),
  isPublic: true,
  createdDate: dayjs('2025-12-24T19:19'),
  createdBy: 'whether',
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
  uploadDate: dayjs('2025-12-24T13:26'),
  isPublic: false,
  downloadCount: 16184,
  viewCount: 4706,
  detectedLanguage: 'silent nex',
  manualLanguage: 'phew chunt',
  languageConfidence: 0.49,
  createdDate: dayjs('2025-12-24T14:35'),
  createdBy: 'scarily',
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
  uploadDate: dayjs('2025-12-24T16:08'),
  isPublic: true,
  downloadCount: 25556,
  viewCount: 29408,
  detectedLanguage: 'experience',
  manualLanguage: 'unnecessar',
  languageConfidence: 0.08,
  pageCount: 18797,
  createdDate: dayjs('2025-12-24T17:59'),
  createdBy: 'for where',
};

export const sampleWithNewData: NewDocument = {
  title: 'vain',
  fileName: 'geez furthermore',
  fileSize: 19763,
  mimeType: 'reschedule failing',
  sha256: 'circular ouch',
  s3Key: 'madly relaunch',
  s3Bucket: 'fatal',
  uploadDate: dayjs('2025-12-24T15:53'),
  isPublic: true,
  createdDate: dayjs('2025-12-25T08:18'),
  createdBy: 'yawningly fervently',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
