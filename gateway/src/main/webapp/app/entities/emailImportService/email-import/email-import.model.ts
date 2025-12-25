import dayjs from 'dayjs/esm';
import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { ImportStatus } from 'app/entities/enumerations/import-status.model';

export interface IEmailImport {
  id: number;
  fromEmail?: string | null;
  toEmail?: string | null;
  subject?: string | null;
  body?: string | null;
  bodyHtml?: string | null;
  receivedDate?: dayjs.Dayjs | null;
  processedDate?: dayjs.Dayjs | null;
  status?: keyof typeof ImportStatus | null;
  folderId?: number | null;
  documentTypeId?: number | null;
  attachmentCount?: number | null;
  documentsCreated?: number | null;
  appliedRuleId?: number | null;
  errorMessage?: string | null;
  metadata?: string | null;
  appliedRule?: Pick<IImportRule, 'id'> | null;
}

export type NewEmailImport = Omit<IEmailImport, 'id'> & { id: null };
