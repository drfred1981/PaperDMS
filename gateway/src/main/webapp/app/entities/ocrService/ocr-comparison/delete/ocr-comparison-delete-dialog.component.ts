import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOcrComparison } from '../ocr-comparison.model';
import { OcrComparisonService } from '../service/ocr-comparison.service';

@Component({
  templateUrl: './ocr-comparison-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OcrComparisonDeleteDialogComponent {
  ocrComparison?: IOcrComparison;

  protected ocrComparisonService = inject(OcrComparisonService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ocrComparisonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
