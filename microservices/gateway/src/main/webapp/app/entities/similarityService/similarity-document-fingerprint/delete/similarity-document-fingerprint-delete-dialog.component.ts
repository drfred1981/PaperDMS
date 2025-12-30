import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';
import { SimilarityDocumentFingerprintService } from '../service/similarity-document-fingerprint.service';

@Component({
  templateUrl: './similarity-document-fingerprint-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SimilarityDocumentFingerprintDeleteDialogComponent {
  similarityDocumentFingerprint?: ISimilarityDocumentFingerprint;

  protected similarityDocumentFingerprintService = inject(SimilarityDocumentFingerprintService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.similarityDocumentFingerprintService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
