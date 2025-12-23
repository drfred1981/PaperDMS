import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IBankTransaction } from '../bank-transaction.model';

@Component({
  selector: 'jhi-bank-transaction-detail',
  templateUrl: './bank-transaction-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatePipe],
})
export class BankTransactionDetail {
  bankTransaction = input<IBankTransaction | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
