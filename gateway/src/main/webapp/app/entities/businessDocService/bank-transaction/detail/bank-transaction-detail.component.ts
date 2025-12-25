import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IBankTransaction } from '../bank-transaction.model';

@Component({
  selector: 'jhi-bank-transaction-detail',
  templateUrl: './bank-transaction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class BankTransactionDetailComponent {
  bankTransaction = input<IBankTransaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
