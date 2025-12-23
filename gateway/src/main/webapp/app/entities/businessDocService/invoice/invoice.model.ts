import dayjs from 'dayjs/esm';

import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';
import { InvoiceType } from 'app/entities/enumerations/invoice-type.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';

export interface IInvoice {
  id: number;
  documentId?: number | null;
  invoiceNumber?: string | null;
  invoiceType?: keyof typeof InvoiceType | null;
  supplierName?: string | null;
  customerName?: string | null;
  issueDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  totalAmountExclTax?: number | null;
  taxAmount?: number | null;
  totalAmountInclTax?: number | null;
  currency?: string | null;
  status?: keyof typeof InvoiceStatus | null;
  paymentMethod?: keyof typeof PaymentMethod | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
