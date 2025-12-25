import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISearchFacet } from '../search-facet.model';
import { SearchFacetService } from '../service/search-facet.service';

@Component({
  templateUrl: './search-facet-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SearchFacetDeleteDialogComponent {
  searchFacet?: ISearchFacet;

  protected searchFacetService = inject(SearchFacetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.searchFacetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
