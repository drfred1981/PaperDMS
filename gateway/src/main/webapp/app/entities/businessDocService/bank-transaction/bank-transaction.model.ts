import dayjs from 'dayjs/esm';
import { IBankStatement } from 'app/entities/businessDocService/bank-statement/bank-statement.model';

export interface IBankTransaction {
  id: number;
  statementId?: number | null;
  transactionDate?: dayjs.Dayjs | null;
  description?: string | null;
  debitAmount?: number | null;
  creditAmount?: number | null;
  balance?: number | null;
  isReconciled?: boolean | null;
  statement?: Pick<IBankStatement, 'id'> | null;
}

export type NewBankTransaction = Omit<IBankTransaction, 'id'> & { id: null };
