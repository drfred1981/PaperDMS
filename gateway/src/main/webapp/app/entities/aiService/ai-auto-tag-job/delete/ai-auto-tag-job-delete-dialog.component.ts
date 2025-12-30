import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAIAutoTagJob } from '../ai-auto-tag-job.model';
import { AIAutoTagJobService } from '../service/ai-auto-tag-job.service';

@Component({
  templateUrl: './ai-auto-tag-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AIAutoTagJobDeleteDialogComponent {
  aIAutoTagJob?: IAIAutoTagJob;

  protected aIAutoTagJobService = inject(AIAutoTagJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aIAutoTagJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
