import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransformRedactionJob } from '../transform-redaction-job.model';
import { TransformRedactionJobService } from '../service/transform-redaction-job.service';

@Component({
  templateUrl: './transform-redaction-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransformRedactionJobDeleteDialogComponent {
  transformRedactionJob?: ITransformRedactionJob;

  protected transformRedactionJobService = inject(TransformRedactionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transformRedactionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
