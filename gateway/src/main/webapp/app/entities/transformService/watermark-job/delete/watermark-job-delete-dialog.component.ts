import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWatermarkJob } from '../watermark-job.model';
import { WatermarkJobService } from '../service/watermark-job.service';

@Component({
  templateUrl: './watermark-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WatermarkJobDeleteDialogComponent {
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
