import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ISearchIndex } from '../search-index.model';
import { SearchIndexService } from '../service/search-index.service';

@Component({
  templateUrl: './search-index-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SearchIndexDeleteDialog {
  searchIndex?: ISearchIndex;

  protected searchIndexService = inject(SearchIndexService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.searchIndexService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
