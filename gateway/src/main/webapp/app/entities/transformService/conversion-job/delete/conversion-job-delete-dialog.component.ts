import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConversionJob } from '../conversion-job.model';
import { ConversionJobService } from '../service/conversion-job.service';

@Component({
  templateUrl: './conversion-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConversionJobDeleteDialogComponent {
  conversionJob?: IConversionJob;

  protected conversionJobService = inject(ConversionJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conversionJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
