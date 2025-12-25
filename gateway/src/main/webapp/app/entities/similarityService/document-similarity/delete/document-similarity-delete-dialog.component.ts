import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentSimilarity } from '../document-similarity.model';
import { DocumentSimilarityService } from '../service/document-similarity.service';

@Component({
  templateUrl: './document-similarity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentSimilarityDeleteDialogComponent {
  documentSimilarity?: IDocumentSimilarity;

  protected documentSimilarityService = inject(DocumentSimilarityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentSimilarityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
