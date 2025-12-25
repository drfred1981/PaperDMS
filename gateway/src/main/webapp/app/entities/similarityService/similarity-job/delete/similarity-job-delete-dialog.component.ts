import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISimilarityJob } from '../similarity-job.model';
import { SimilarityJobService } from '../service/similarity-job.service';

@Component({
  templateUrl: './similarity-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SimilarityJobDeleteDialogComponent {
  similarityJob?: ISimilarityJob;

  protected similarityJobService = inject(SimilarityJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.similarityJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
