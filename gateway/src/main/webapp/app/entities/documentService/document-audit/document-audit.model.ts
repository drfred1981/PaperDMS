import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';
import { AuditAction } from 'app/entities/enumerations/audit-action.model';

export interface IDocumentAudit {
  id: number;
  documentSha256?: string | null;
  action?: keyof typeof AuditAction | null;
  userId?: string | null;
  userIp?: string | null;
  actionDate?: dayjs.Dayjs | null;
  additionalInfo?: string | null;
  document?: Pick<IDocument, 'id'> | null;
}

export type NewDocumentAudit = Omit<IDocumentAudit, 'id'> & { id: null };
