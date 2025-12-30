import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISearchSemantic } from '../search-semantic.model';
import { SearchSemanticService } from '../service/search-semantic.service';

@Component({
  templateUrl: './search-semantic-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SearchSemanticDeleteDialogComponent {
  searchSemantic?: ISearchSemantic;

  protected searchSemanticService = inject(SearchSemanticService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.searchSemanticService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
