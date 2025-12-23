import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';
import { InvoiceType } from 'app/entities/enumerations/invoice-type.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';
import SharedModule from 'app/shared/shared.module';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';

import { InvoiceFormGroup, InvoiceFormService } from './invoice-form.service';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class InvoiceUpdate implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  invoiceTypeValues = Object.keys(InvoiceType);
  invoiceStatusValues = Object.keys(InvoiceStatus);
  paymentMethodValues = Object.keys(PaymentMethod);

  protected invoiceService = inject(InvoiceService);
  protected invoiceFormService = inject(InvoiceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id === null) {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);
  }
}
