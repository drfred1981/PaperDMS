import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IComparisonJob } from '../comparison-job.model';
import { ComparisonJobService } from '../service/comparison-job.service';

@Component({
  templateUrl: './comparison-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ComparisonJobDeleteDialogComponent {
  comparisonJob?: IComparisonJob;

  protected comparisonJobService = inject(ComparisonJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comparisonJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
