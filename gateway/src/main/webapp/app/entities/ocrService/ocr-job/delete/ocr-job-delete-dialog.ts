import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IOcrJob } from '../ocr-job.model';
import { OcrJobService } from '../service/ocr-job.service';

@Component({
  templateUrl: './ocr-job-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class OcrJobDeleteDialog {
  ocrJob?: IOcrJob;

  protected ocrJobService = inject(OcrJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ocrJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
