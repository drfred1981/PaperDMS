import { IInvoice } from 'app/entities/businessDocService/invoice/invoice.model';

export interface IInvoiceLine {
  id: number;
  invoiceId?: number | null;
  lineNumber?: number | null;
  description?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  taxRate?: number | null;
  totalAmountExclTax?: number | null;
  invoice?: Pick<IInvoice, 'id'> | null;
}

export type NewInvoiceLine = Omit<IInvoiceLine, 'id'> & { id: null };
