import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInvoiceLine } from '../invoice-line.model';

@Component({
  selector: 'jhi-invoice-line-detail',
  templateUrl: './invoice-line-detail.html',
  imports: [SharedModule, RouterLink],
})
export class InvoiceLineDetail {
  invoiceLine = input<IInvoiceLine | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
