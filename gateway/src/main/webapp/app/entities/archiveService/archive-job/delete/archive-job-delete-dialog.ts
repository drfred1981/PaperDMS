import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IArchiveJob } from '../archive-job.model';
import { ArchiveJobService } from '../service/archive-job.service';

@Component({
  templateUrl: './archive-job-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ArchiveJobDeleteDialog {
  archiveJob?: IArchiveJob;

  protected archiveJobService = inject(ArchiveJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.archiveJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
