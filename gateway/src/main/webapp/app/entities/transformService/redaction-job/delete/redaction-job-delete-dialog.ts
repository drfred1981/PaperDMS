import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IRedactionJob } from '../redaction-job.model';
import { RedactionJobService } from '../service/redaction-job.service';

@Component({
  templateUrl: './redaction-job-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class RedactionJobDeleteDialog {
  redactionJob?: IRedactionJob;

  protected redactionJobService = inject(RedactionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.redactionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
