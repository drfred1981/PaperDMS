import dayjs from 'dayjs/esm';
import { IEmailImportImportRule } from 'app/entities/EmailImportDocumentService/email-import-import-rule/email-import-import-rule.model';
import { ImportStatus } from 'app/entities/enumerations/import-status.model';

export interface IEmailImportDocument {
  id: number;
  sha256?: string | null;
  fromEmail?: string | null;
  toEmail?: string | null;
  subject?: string | null;
  body?: string | null;
  bodyHtml?: string | null;
  receivedDate?: dayjs.Dayjs | null;
  processedDate?: dayjs.Dayjs | null;
  status?: keyof typeof ImportStatus | null;
  attachmentCount?: number | null;
  documentsCreated?: number | null;
  errorMessage?: string | null;
  metadata?: string | null;
  documentSha256?: string | null;
  appliedRule?: Pick<IEmailImportImportRule, 'id'> | null;
}

export type NewEmailImportDocument = Omit<IEmailImportDocument, 'id'> & { id: null };
