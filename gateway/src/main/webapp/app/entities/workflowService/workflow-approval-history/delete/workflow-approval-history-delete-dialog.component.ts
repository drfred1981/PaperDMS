import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkflowApprovalHistory } from '../workflow-approval-history.model';
import { WorkflowApprovalHistoryService } from '../service/workflow-approval-history.service';

@Component({
  templateUrl: './workflow-approval-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowApprovalHistoryDeleteDialogComponent {
  workflowApprovalHistory?: IWorkflowApprovalHistory;

  protected workflowApprovalHistoryService = inject(WorkflowApprovalHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowApprovalHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
