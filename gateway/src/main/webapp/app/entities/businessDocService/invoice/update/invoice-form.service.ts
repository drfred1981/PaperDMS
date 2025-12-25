import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'createdDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  documentId: FormControl<InvoiceFormRawValue['documentId']>;
  invoiceNumber: FormControl<InvoiceFormRawValue['invoiceNumber']>;
  invoiceType: FormControl<InvoiceFormRawValue['invoiceType']>;
  supplierName: FormControl<InvoiceFormRawValue['supplierName']>;
  customerName: FormControl<InvoiceFormRawValue['customerName']>;
  issueDate: FormControl<InvoiceFormRawValue['issueDate']>;
  dueDate: FormControl<InvoiceFormRawValue['dueDate']>;
  paymentDate: FormControl<InvoiceFormRawValue['paymentDate']>;
  totalAmountExclTax: FormControl<InvoiceFormRawValue['totalAmountExclTax']>;
  taxAmount: FormControl<InvoiceFormRawValue['taxAmount']>;
  totalAmountInclTax: FormControl<InvoiceFormRawValue['totalAmountInclTax']>;
  currency: FormControl<InvoiceFormRawValue['currency']>;
  status: FormControl<InvoiceFormRawValue['status']>;
  paymentMethod: FormControl<InvoiceFormRawValue['paymentMethod']>;
  createdDate: FormControl<InvoiceFormRawValue['createdDate']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(invoiceRawValue.documentId, {
        validators: [Validators.required],
      }),
      invoiceNumber: new FormControl(invoiceRawValue.invoiceNumber, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      invoiceType: new FormControl(invoiceRawValue.invoiceType, {
        validators: [Validators.required],
      }),
      supplierName: new FormControl(invoiceRawValue.supplierName, {
        validators: [Validators.maxLength(255)],
      }),
      customerName: new FormControl(invoiceRawValue.customerName, {
        validators: [Validators.maxLength(255)],
      }),
      issueDate: new FormControl(invoiceRawValue.issueDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(invoiceRawValue.dueDate),
      paymentDate: new FormControl(invoiceRawValue.paymentDate),
      totalAmountExclTax: new FormControl(invoiceRawValue.totalAmountExclTax, {
        validators: [Validators.required],
      }),
      taxAmount: new FormControl(invoiceRawValue.taxAmount, {
        validators: [Validators.required],
      }),
      totalAmountInclTax: new FormControl(invoiceRawValue.totalAmountInclTax, {
        validators: [Validators.required],
      }),
      currency: new FormControl(invoiceRawValue.currency, {
        validators: [Validators.required, Validators.maxLength(3)],
      }),
      status: new FormControl(invoiceRawValue.status, {
        validators: [Validators.required],
      }),
      paymentMethod: new FormControl(invoiceRawValue.paymentMethod),
      createdDate: new FormControl(invoiceRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      createdDate: dayjs(rawInvoice.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      createdDate: invoice.createdDate ? invoice.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
