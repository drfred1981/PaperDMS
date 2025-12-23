import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';

@Component({
  templateUrl: './contract-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ContractDeleteDialog {
  contract?: IContract;

  protected contractService = inject(ContractService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
