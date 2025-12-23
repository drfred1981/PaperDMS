import { IEmailImport } from 'app/entities/emailImportService/email-import/email-import.model';
import { AttachmentStatus } from 'app/entities/enumerations/attachment-status.model';

export interface IEmailAttachment {
  id: number;
  emailImportId?: number | null;
  fileName?: string | null;
  fileSize?: number | null;
  mimeType?: string | null;
  sha256?: string | null;
  s3Key?: string | null;
  documentId?: number | null;
  status?: keyof typeof AttachmentStatus | null;
  errorMessage?: string | null;
  emailImport?: Pick<IEmailImport, 'id'> | null;
}

export type NewEmailAttachment = Omit<IEmailAttachment, 'id'> & { id: null };
