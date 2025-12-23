import { IEmailAttachment, NewEmailAttachment } from './email-attachment.model';

export const sampleWithRequiredData: IEmailAttachment = {
  id: 23847,
  emailImportId: 27967,
  fileName: 'geez safely until',
  fileSize: 20566,
  mimeType: 'lighthearted eek concerning',
  sha256: 'er',
  status: 'PROCESSING',
};

export const sampleWithPartialData: IEmailAttachment = {
  id: 3161,
  emailImportId: 21688,
  fileName: 'definite wobbly yearly',
  fileSize: 17406,
  mimeType: 'woefully sorrowful',
  sha256: 'atomize competent',
  status: 'IMPORTED',
};

export const sampleWithFullData: IEmailAttachment = {
  id: 14901,
  emailImportId: 16545,
  fileName: 'cruelly athwart advanced',
  fileSize: 64,
  mimeType: 'or personalise',
  sha256: 'baritone',
  s3Key: 'given',
  documentId: 4555,
  status: 'IMPORTED',
  errorMessage: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewEmailAttachment = {
  emailImportId: 15468,
  fileName: 'especially',
  fileSize: 8266,
  mimeType: 'enchanted sparkling agreement',
  sha256: 'likewise haze aboard',
  status: 'SKIPPED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
