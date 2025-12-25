import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmailImport } from '../email-import.model';
import { EmailImportService } from '../service/email-import.service';

@Component({
  templateUrl: './email-import-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportDeleteDialogComponent {
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
