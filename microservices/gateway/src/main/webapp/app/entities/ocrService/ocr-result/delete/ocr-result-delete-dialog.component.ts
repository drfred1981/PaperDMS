import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOcrResult } from '../ocr-result.model';
import { OcrResultService } from '../service/ocr-result.service';

@Component({
  templateUrl: './ocr-result-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OcrResultDeleteDialogComponent {
  ocrResult?: IOcrResult;

  protected ocrResultService = inject(OcrResultService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ocrResultService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
