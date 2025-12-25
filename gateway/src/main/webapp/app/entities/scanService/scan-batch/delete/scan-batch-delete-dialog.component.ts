import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IScanBatch } from '../scan-batch.model';
import { ScanBatchService } from '../service/scan-batch.service';

@Component({
  templateUrl: './scan-batch-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ScanBatchDeleteDialogComponent {
  scanBatch?: IScanBatch;

  protected scanBatchService = inject(ScanBatchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scanBatchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
