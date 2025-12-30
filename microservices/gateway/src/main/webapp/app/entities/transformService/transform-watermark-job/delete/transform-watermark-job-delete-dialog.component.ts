import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransformWatermarkJob } from '../transform-watermark-job.model';
import { TransformWatermarkJobService } from '../service/transform-watermark-job.service';

@Component({
  templateUrl: './transform-watermark-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransformWatermarkJobDeleteDialogComponent {
  transformWatermarkJob?: ITransformWatermarkJob;

  protected transformWatermarkJobService = inject(TransformWatermarkJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transformWatermarkJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
