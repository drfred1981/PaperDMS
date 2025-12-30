import { IEmailImportEmailAttachment, NewEmailImportEmailAttachment } from './email-import-email-attachment.model';

export const sampleWithRequiredData: IEmailImportEmailAttachment = {
  id: 25775,
  fileName: 'who',
  fileSize: 25651,
  mimeType: 'fleck forsaken behind',
  sha256: 'woot',
  status: 'SKIPPED',
};

export const sampleWithPartialData: IEmailImportEmailAttachment = {
  id: 1832,
  fileName: 'maul frenetically jealously',
  fileSize: 21209,
  mimeType: 'woot poorly cap',
  sha256: 'symbolise wisely rapidly',
  s3Key: 'wee',
  status: 'SKIPPED',
};

export const sampleWithFullData: IEmailImportEmailAttachment = {
  id: 17948,
  fileName: 'reschedule obsess',
  fileSize: 31169,
  mimeType: 'masculinize',
  sha256: 'vibrant frankly wonderfully',
  s3Key: 'carelessly why',
  status: 'PROCESSING',
  errorMessage: '../fake-data/blob/hipster.txt',
  documentSha256: 'who',
};

export const sampleWithNewData: NewEmailImportEmailAttachment = {
  fileName: 'strict',
  fileSize: 10963,
  mimeType: 'lovingly',
  sha256: 'arrange',
  status: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
