import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInvoice } from 'app/entities/businessDocService/invoice/invoice.model';
import { InvoiceService } from 'app/entities/businessDocService/invoice/service/invoice.service';
import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineService } from '../service/invoice-line.service';
import { InvoiceLineFormGroup, InvoiceLineFormService } from './invoice-line-form.service';

@Component({
  selector: 'jhi-invoice-line-update',
  templateUrl: './invoice-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceLineUpdateComponent implements OnInit {
  isSaving = false;
  invoiceLine: IInvoiceLine | null = null;

  invoicesSharedCollection: IInvoice[] = [];

  protected invoiceLineService = inject(InvoiceLineService);
  protected invoiceLineFormService = inject(InvoiceLineFormService);
  protected invoiceService = inject(InvoiceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceLineFormGroup = this.invoiceLineFormService.createInvoiceLineFormGroup();

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceLine }) => {
      this.invoiceLine = invoiceLine;
      if (invoiceLine) {
        this.updateForm(invoiceLine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceLine = this.invoiceLineFormService.getInvoiceLine(this.editForm);
    if (invoiceLine.id !== null) {
      this.subscribeToSaveResponse(this.invoiceLineService.update(invoiceLine));
    } else {
      this.subscribeToSaveResponse(this.invoiceLineService.create(invoiceLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceLine>>): void {
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

  protected updateForm(invoiceLine: IInvoiceLine): void {
    this.invoiceLine = invoiceLine;
    this.invoiceLineFormService.resetForm(this.editForm, invoiceLine);

    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
      this.invoicesSharedCollection,
      invoiceLine.invoice,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceLine?.invoice)),
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }
}
