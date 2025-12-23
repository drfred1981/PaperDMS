import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ISearchQuery } from '../search-query.model';
import { SearchQueryService } from '../service/search-query.service';

@Component({
  templateUrl: './search-query-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SearchQueryDeleteDialog {
  searchQuery?: ISearchQuery;

  protected searchQueryService = inject(SearchQueryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.searchQueryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
