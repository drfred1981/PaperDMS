import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBankStatement } from '../bank-statement.model';
import { BankStatementService } from '../service/bank-statement.service';

@Component({
  templateUrl: './bank-statement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BankStatementDeleteDialogComponent {
  bankStatement?: IBankStatement;

  protected bankStatementService = inject(BankStatementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankStatementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
