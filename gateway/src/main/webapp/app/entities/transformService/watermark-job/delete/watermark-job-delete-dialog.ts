import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WatermarkJobService } from '../service/watermark-job.service';
import { IWatermarkJob } from '../watermark-job.model';

@Component({
  templateUrl: './watermark-job-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WatermarkJobDeleteDialog {
  watermarkJob?: IWatermarkJob;

  protected watermarkJobService = inject(WatermarkJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.watermarkJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
