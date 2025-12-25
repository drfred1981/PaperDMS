import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBankStatement } from '../bank-statement.model';

@Component({
  selector: 'jhi-bank-statement-detail',
  templateUrl: './bank-statement-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BankStatementDetailComponent {
  bankStatement = input<IBankStatement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
