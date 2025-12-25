import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentFingerprint } from '../document-fingerprint.model';
import { DocumentFingerprintService } from '../service/document-fingerprint.service';

@Component({
  templateUrl: './document-fingerprint-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentFingerprintDeleteDialogComponent {
  documentFingerprint?: IDocumentFingerprint;

  protected documentFingerprintService = inject(DocumentFingerprintService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentFingerprintService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
