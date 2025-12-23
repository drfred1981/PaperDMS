import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IExportResult } from '../export-result.model';
import { ExportResultService } from '../service/export-result.service';

@Component({
  templateUrl: './export-result-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ExportResultDeleteDialog {
  exportResult?: IExportResult;

  protected exportResultService = inject(ExportResultService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exportResultService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
