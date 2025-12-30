import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransformMergeJob } from '../transform-merge-job.model';
import { TransformMergeJobService } from '../service/transform-merge-job.service';

@Component({
  templateUrl: './transform-merge-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransformMergeJobDeleteDialogComponent {
  transformMergeJob?: ITransformMergeJob;

  protected transformMergeJobService = inject(TransformMergeJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transformMergeJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
