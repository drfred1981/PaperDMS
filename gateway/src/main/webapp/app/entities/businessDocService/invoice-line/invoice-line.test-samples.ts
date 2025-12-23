import { IInvoiceLine, NewInvoiceLine } from './invoice-line.model';

export const sampleWithRequiredData: IInvoiceLine = {
  id: 4931,
  invoiceId: 15085,
  lineNumber: 29500,
  description: 'blah analogy searchingly',
  quantity: 3344.42,
  unitPrice: 9598.92,
  taxRate: 15734.65,
  totalAmountExclTax: 18281.85,
};

export const sampleWithPartialData: IInvoiceLine = {
  id: 18142,
  invoiceId: 19322,
  lineNumber: 24078,
  description: 'trouser',
  quantity: 25813.31,
  unitPrice: 28003.12,
  taxRate: 25293.17,
  totalAmountExclTax: 7353.68,
};

export const sampleWithFullData: IInvoiceLine = {
  id: 23631,
  invoiceId: 32691,
  lineNumber: 10083,
  description: 'promptly whereas',
  quantity: 8699.94,
  unitPrice: 23725.14,
  taxRate: 25635.33,
  totalAmountExclTax: 21993.1,
};

export const sampleWithNewData: NewInvoiceLine = {
  invoiceId: 26337,
  lineNumber: 17296,
  description: 'gosh',
  quantity: 6715.27,
  unitPrice: 16965.39,
  taxRate: 9768.59,
  totalAmountExclTax: 25556.95,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
