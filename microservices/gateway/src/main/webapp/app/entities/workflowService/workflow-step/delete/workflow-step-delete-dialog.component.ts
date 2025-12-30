import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkflowStep } from '../workflow-step.model';
import { WorkflowStepService } from '../service/workflow-step.service';

@Component({
  templateUrl: './workflow-step-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowStepDeleteDialogComponent {
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
