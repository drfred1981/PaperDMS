import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IScanJob } from '../scan-job.model';
import { ScanJobService } from '../service/scan-job.service';

@Component({
  templateUrl: './scan-job-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ScanJobDeleteDialog {
  scanJob?: IScanJob;

  protected scanJobService = inject(ScanJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scanJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
