import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkflowTask } from '../workflow-task.model';
import { WorkflowTaskService } from '../service/workflow-task.service';

@Component({
  templateUrl: './workflow-task-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkflowTaskDeleteDialogComponent {
  workflowTask?: IWorkflowTask;

  protected workflowTaskService = inject(WorkflowTaskService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowTaskService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
