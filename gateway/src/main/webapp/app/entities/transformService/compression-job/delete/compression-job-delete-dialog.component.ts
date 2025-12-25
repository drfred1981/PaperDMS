import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompressionJob } from '../compression-job.model';
import { CompressionJobService } from '../service/compression-job.service';

@Component({
  templateUrl: './compression-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CompressionJobDeleteDialogComponent {
  compressionJob?: ICompressionJob;

  protected compressionJobService = inject(CompressionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compressionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
