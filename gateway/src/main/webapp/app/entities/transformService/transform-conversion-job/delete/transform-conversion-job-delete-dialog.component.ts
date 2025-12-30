import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransformConversionJob } from '../transform-conversion-job.model';
import { TransformConversionJobService } from '../service/transform-conversion-job.service';

@Component({
  templateUrl: './transform-conversion-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransformConversionJobDeleteDialogComponent {
  transformConversionJob?: ITransformConversionJob;

  protected transformConversionJobService = inject(TransformConversionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transformConversionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
