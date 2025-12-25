import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  documentId: 15398,
  invoiceNumber: 'utilization vice',
  invoiceType: 'PURCHASE',
  issueDate: dayjs('2025-12-25'),
  totalAmountExclTax: 30591.35,
  taxAmount: 18303.99,
  totalAmountInclTax: 31001.36,
  currency: 'nic',
  status: 'PENDING_APPROVAL',
  createdDate: dayjs('2025-12-25T09:03'),
};

export const sampleWithPartialData: IInvoice = {
  id: 20215,
  documentId: 4754,
  invoiceNumber: 'nor vaguely',
  invoiceType: 'DEBIT_NOTE',
  customerName: 'yawningly than allegation',
  issueDate: dayjs('2025-12-24'),
  totalAmountExclTax: 26996.29,
  taxAmount: 510.75,
  totalAmountInclTax: 20714.98,
  currency: 'tim',
  status: 'OVERDUE',
  createdDate: dayjs('2025-12-24T15:45'),
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  documentId: 16471,
  invoiceNumber: 'in fess',
  invoiceType: 'SALES',
  supplierName: 'carpool rowdy',
  customerName: 'cork showboat bravely',
  issueDate: dayjs('2025-12-25'),
  dueDate: dayjs('2025-12-25'),
  paymentDate: dayjs('2025-12-25'),
  totalAmountExclTax: 13153.34,
  taxAmount: 11949.54,
  totalAmountInclTax: 14165.35,
  currency: 'oof',
  status: 'OVERDUE',
  paymentMethod: 'PAYPAL',
  createdDate: dayjs('2025-12-24T14:09'),
};

export const sampleWithNewData: NewInvoice = {
  documentId: 16356,
  invoiceNumber: 'ew now',
  invoiceType: 'PURCHASE',
  issueDate: dayjs('2025-12-24'),
  totalAmountExclTax: 17194.57,
  taxAmount: 26067.95,
  totalAmountInclTax: 23357.37,
  currency: 'cit',
  status: 'PENDING_APPROVAL',
  createdDate: dayjs('2025-12-25T01:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
