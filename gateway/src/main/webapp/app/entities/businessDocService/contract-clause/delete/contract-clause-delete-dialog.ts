import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IContractClause } from '../contract-clause.model';
import { ContractClauseService } from '../service/contract-clause.service';

@Component({
  templateUrl: './contract-clause-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ContractClauseDeleteDialog {
  contractClause?: IContractClause;

  protected contractClauseService = inject(ContractClauseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractClauseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
