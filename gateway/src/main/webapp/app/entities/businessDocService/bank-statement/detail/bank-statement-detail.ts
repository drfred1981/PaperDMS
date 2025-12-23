import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IBankStatement } from '../bank-statement.model';

@Component({
  selector: 'jhi-bank-statement-detail',
  templateUrl: './bank-statement-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BankStatementDetail {
  bankStatement = input<IBankStatement | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
