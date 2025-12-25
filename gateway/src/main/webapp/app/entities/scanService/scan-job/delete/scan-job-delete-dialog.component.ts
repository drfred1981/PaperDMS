import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IScanJob } from '../scan-job.model';
import { ScanJobService } from '../service/scan-job.service';

@Component({
  templateUrl: './scan-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ScanJobDeleteDialogComponent {
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
