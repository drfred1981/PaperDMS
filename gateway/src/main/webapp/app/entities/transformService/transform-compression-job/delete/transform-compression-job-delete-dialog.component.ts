import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransformCompressionJob } from '../transform-compression-job.model';
import { TransformCompressionJobService } from '../service/transform-compression-job.service';

@Component({
  templateUrl: './transform-compression-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransformCompressionJobDeleteDialogComponent {
  transformCompressionJob?: ITransformCompressionJob;

  protected transformCompressionJobService = inject(TransformCompressionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transformCompressionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
