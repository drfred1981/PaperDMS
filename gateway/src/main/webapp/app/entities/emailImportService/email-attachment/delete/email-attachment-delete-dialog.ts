import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IEmailAttachment } from '../email-attachment.model';
import { EmailAttachmentService } from '../service/email-attachment.service';

@Component({
  templateUrl: './email-attachment-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class EmailAttachmentDeleteDialog {
  emailAttachment?: IEmailAttachment;

  protected emailAttachmentService = inject(EmailAttachmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailAttachmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
