import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionService } from '../service/document-version.service';

@Component({
  templateUrl: './document-version-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentVersionDeleteDialog {
  documentVersion?: IDocumentVersion;

  protected documentVersionService = inject(DocumentVersionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentVersionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
