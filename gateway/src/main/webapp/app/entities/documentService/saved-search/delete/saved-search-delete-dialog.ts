import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ISavedSearch } from '../saved-search.model';
import { SavedSearchService } from '../service/saved-search.service';

@Component({
  templateUrl: './saved-search-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SavedSearchDeleteDialog {
  savedSearch?: ISavedSearch;

  protected savedSearchService = inject(SavedSearchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.savedSearchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
