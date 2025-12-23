import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IOcrCache } from '../ocr-cache.model';
import { OcrCacheService } from '../service/ocr-cache.service';

@Component({
  templateUrl: './ocr-cache-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class OcrCacheDeleteDialog {
  ocrCache?: IOcrCache;

  protected ocrCacheService = inject(OcrCacheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ocrCacheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
