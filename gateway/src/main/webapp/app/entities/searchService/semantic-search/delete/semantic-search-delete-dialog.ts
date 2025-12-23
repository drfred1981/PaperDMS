import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ISemanticSearch } from '../semantic-search.model';
import { SemanticSearchService } from '../service/semantic-search.service';

@Component({
  templateUrl: './semantic-search-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SemanticSearchDeleteDialog {
  semanticSearch?: ISemanticSearch;

  protected semanticSearchService = inject(SemanticSearchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.semanticSearchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
