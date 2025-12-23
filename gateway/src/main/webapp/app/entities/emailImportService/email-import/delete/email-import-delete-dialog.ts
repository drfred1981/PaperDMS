import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IEmailImport } from '../email-import.model';
import { EmailImportService } from '../service/email-import.service';

@Component({
  templateUrl: './email-import-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportDeleteDialog {
  emailImport?: IEmailImport;

  protected emailImportService = inject(EmailImportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailImportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
