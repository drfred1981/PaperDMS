import dayjs from 'dayjs/esm';

import { IBankStatement, NewBankStatement } from './bank-statement.model';

export const sampleWithRequiredData: IBankStatement = {
  id: 22661,
  documentId: 21449,
  accountNumber: 'huzzah alive ethyl',
  bankName: 'afford',
  statementDate: dayjs('2025-12-19'),
  statementPeriodStart: dayjs('2025-12-20'),
  statementPeriodEnd: dayjs('2025-12-20'),
  openingBalance: 32473.09,
  closingBalance: 30800.1,
  currency: 'meh',
  status: 'PROCESSED',
  isReconciled: false,
  createdDate: dayjs('2025-12-20T15:15'),
};

export const sampleWithPartialData: IBankStatement = {
  id: 11533,
  documentId: 853,
  accountNumber: 'director clonk innocently',
  bankName: 'likewise simplistic meh',
  statementDate: dayjs('2025-12-19'),
  statementPeriodStart: dayjs('2025-12-20'),
  statementPeriodEnd: dayjs('2025-12-20'),
  openingBalance: 12791.85,
  closingBalance: 772.14,
  currency: 'pff',
  status: 'ARCHIVED',
  isReconciled: true,
  createdDate: dayjs('2025-12-20T13:05'),
};

export const sampleWithFullData: IBankStatement = {
  id: 28382,
  documentId: 32661,
  accountNumber: 'furthermore',
  bankName: 'phooey finally institutionalize',
  statementDate: dayjs('2025-12-20'),
  statementPeriodStart: dayjs('2025-12-19'),
  statementPeriodEnd: dayjs('2025-12-20'),
  openingBalance: 26512.06,
  closingBalance: 6857.83,
  currency: 'ick',
  status: 'RECONCILED',
  isReconciled: false,
  createdDate: dayjs('2025-12-20T15:36'),
};

export const sampleWithNewData: NewBankStatement = {
  documentId: 26772,
  accountNumber: 'closely safely phooey',
  bankName: 'aha regarding',
  statementDate: dayjs('2025-12-20'),
  statementPeriodStart: dayjs('2025-12-20'),
  statementPeriodEnd: dayjs('2025-12-19'),
  openingBalance: 14198.58,
  closingBalance: 15068.56,
  currency: 'ser',
  status: 'DRAFT',
  isReconciled: true,
  createdDate: dayjs('2025-12-20T06:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
