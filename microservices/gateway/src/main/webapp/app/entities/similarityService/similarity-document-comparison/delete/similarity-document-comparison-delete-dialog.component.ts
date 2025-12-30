import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';

@Component({
  templateUrl: './similarity-document-comparison-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SimilarityDocumentComparisonDeleteDialogComponent {
  similarityDocumentComparison?: ISimilarityDocumentComparison;

  protected similarityDocumentComparisonService = inject(SimilarityDocumentComparisonService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.similarityDocumentComparisonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
