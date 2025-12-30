import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmailImportDocument } from '../email-import-document.model';
import { EmailImportDocumentService } from '../service/email-import-document.service';

@Component({
  templateUrl: './email-import-document-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportDocumentDeleteDialogComponent {
  emailImportDocument?: IEmailImportDocument;

  protected emailImportDocumentService = inject(EmailImportDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailImportDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
