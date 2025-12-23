import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WorkflowStepService } from '../service/workflow-step.service';
import { IWorkflowStep } from '../workflow-step.model';

@Component({
  templateUrl: './workflow-step-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowStepDeleteDialog {
  workflowStep?: IWorkflowStep;

  protected workflowStepService = inject(WorkflowStepService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowStepService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
