import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WorkflowInstanceService } from '../service/workflow-instance.service';
import { IWorkflowInstance } from '../workflow-instance.model';

@Component({
  templateUrl: './workflow-instance-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowInstanceDeleteDialog {
  workflowInstance?: IWorkflowInstance;

  protected workflowInstanceService = inject(WorkflowInstanceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowInstanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
