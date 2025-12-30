import { IEmailImportDocument } from 'app/entities/EmailImportDocumentService/email-import-document/email-import-document.model';
import { AttachmentStatus } from 'app/entities/enumerations/attachment-status.model';

export interface IEmailImportEmailAttachment {
  id: number;
  fileName?: string | null;
  fileSize?: number | null;
  mimeType?: string | null;
  sha256?: string | null;
  s3Key?: string | null;
  status?: keyof typeof AttachmentStatus | null;
  errorMessage?: string | null;
  documentSha256?: string | null;
  emailImportDocument?: Pick<IEmailImportDocument, 'id'> | null;
}

export type NewEmailImportEmailAttachment = Omit<IEmailImportEmailAttachment, 'id'> & { id: null };
