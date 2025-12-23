import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IApprovalHistory } from '../approval-history.model';
import { ApprovalHistoryService } from '../service/approval-history.service';

@Component({
  templateUrl: './approval-history-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ApprovalHistoryDeleteDialog {
  approvalHistory?: IApprovalHistory;

  protected approvalHistoryService = inject(ApprovalHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvalHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
