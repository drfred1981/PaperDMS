import dayjs from 'dayjs/esm';

import { IBankTransaction, NewBankTransaction } from './bank-transaction.model';

export const sampleWithRequiredData: IBankTransaction = {
  id: 29331,
  statementId: 19068,
  transactionDate: dayjs('2025-12-19'),
  description: 'cheese fixed hmph',
  balance: 1894.97,
  isReconciled: false,
};

export const sampleWithPartialData: IBankTransaction = {
  id: 6251,
  statementId: 24229,
  transactionDate: dayjs('2025-12-20'),
  description: 'following',
  balance: 12774.53,
  isReconciled: true,
};

export const sampleWithFullData: IBankTransaction = {
  id: 3029,
  statementId: 8968,
  transactionDate: dayjs('2025-12-19'),
  description: 'bend',
  debitAmount: 8853.66,
  creditAmount: 16115.72,
  balance: 9414.49,
  isReconciled: true,
};

export const sampleWithNewData: NewBankTransaction = {
  statementId: 31995,
  transactionDate: dayjs('2025-12-20'),
  description: 'straw aside unaccountably',
  balance: 14106.33,
  isReconciled: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
