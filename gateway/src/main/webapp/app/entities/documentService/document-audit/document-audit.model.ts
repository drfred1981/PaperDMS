import dayjs from 'dayjs/esm';
import { AuditAction } from 'app/entities/enumerations/audit-action.model';

export interface IDocumentAudit {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  action?: keyof typeof AuditAction | null;
  userId?: string | null;
  userIp?: string | null;
  actionDate?: dayjs.Dayjs | null;
  additionalInfo?: string | null;
}

export type NewDocumentAudit = Omit<IDocumentAudit, 'id'> & { id: null };
