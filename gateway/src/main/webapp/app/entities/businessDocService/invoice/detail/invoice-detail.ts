import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IInvoice } from '../invoice.model';

@Component({
  selector: 'jhi-invoice-detail',
  templateUrl: './invoice-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InvoiceDetail {
  invoice = input<IInvoice | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
