import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IExportPattern } from '../export-pattern.model';
import { ExportPatternService } from '../service/export-pattern.service';

@Component({
  templateUrl: './export-pattern-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ExportPatternDeleteDialog {
  exportPattern?: IExportPattern;

  protected exportPatternService = inject(ExportPatternService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exportPatternService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
