import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IArchiveDocument } from '../archive-document.model';
import { ArchiveDocumentService } from '../service/archive-document.service';

@Component({
  templateUrl: './archive-document-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ArchiveDocumentDeleteDialog {
  archiveDocument?: IArchiveDocument;

  protected archiveDocumentService = inject(ArchiveDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.archiveDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
