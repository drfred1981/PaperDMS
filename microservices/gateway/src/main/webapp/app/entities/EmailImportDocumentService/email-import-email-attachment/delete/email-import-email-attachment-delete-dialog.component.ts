import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmailImportEmailAttachment } from '../email-import-email-attachment.model';
import { EmailImportEmailAttachmentService } from '../service/email-import-email-attachment.service';

@Component({
  templateUrl: './email-import-email-attachment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportEmailAttachmentDeleteDialogComponent {
  emailImportEmailAttachment?: IEmailImportEmailAttachment;

  protected emailImportEmailAttachmentService = inject(EmailImportEmailAttachmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailImportEmailAttachmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
