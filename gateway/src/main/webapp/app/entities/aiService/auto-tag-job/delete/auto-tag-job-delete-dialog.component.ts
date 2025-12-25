import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAutoTagJob } from '../auto-tag-job.model';
import { AutoTagJobService } from '../service/auto-tag-job.service';

@Component({
  templateUrl: './auto-tag-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AutoTagJobDeleteDialogComponent {
  autoTagJob?: IAutoTagJob;

  protected autoTagJobService = inject(AutoTagJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.autoTagJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
