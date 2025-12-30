import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExportJob } from '../export-job.model';
import { ExportJobService } from '../service/export-job.service';

@Component({
  templateUrl: './export-job-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExportJobDeleteDialogComponent {
  exportJob?: IExportJob;

  protected exportJobService = inject(ExportJobService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exportJobService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
