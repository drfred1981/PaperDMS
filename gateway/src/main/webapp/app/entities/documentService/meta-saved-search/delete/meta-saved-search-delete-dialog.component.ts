import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetaSavedSearch } from '../meta-saved-search.model';
import { MetaSavedSearchService } from '../service/meta-saved-search.service';

@Component({
  templateUrl: './meta-saved-search-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetaSavedSearchDeleteDialogComponent {
  metaSavedSearch?: IMetaSavedSearch;

  protected metaSavedSearchService = inject(MetaSavedSearchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaSavedSearchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
