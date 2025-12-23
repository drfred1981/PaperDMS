import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WorkflowService } from '../service/workflow.service';
import { IWorkflow } from '../workflow.model';

@Component({
  templateUrl: './workflow-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowDeleteDialog {
  workflow?: IWorkflow;

  protected workflowService = inject(WorkflowService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
