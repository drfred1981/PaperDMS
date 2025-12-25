import dayjs from 'dayjs/esm';
import { StatementStatus } from 'app/entities/enumerations/statement-status.model';

export interface IBankStatement {
  id: number;
  documentId?: number | null;
  accountNumber?: string | null;
  bankName?: string | null;
  statementDate?: dayjs.Dayjs | null;
  statementPeriodStart?: dayjs.Dayjs | null;
  statementPeriodEnd?: dayjs.Dayjs | null;
  openingBalance?: number | null;
  closingBalance?: number | null;
  currency?: string | null;
  status?: keyof typeof StatementStatus | null;
  isReconciled?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewBankStatement = Omit<IBankStatement, 'id'> & { id: null };
