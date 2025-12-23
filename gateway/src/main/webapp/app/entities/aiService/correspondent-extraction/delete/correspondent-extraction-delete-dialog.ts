import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ICorrespondentExtraction } from '../correspondent-extraction.model';
import { CorrespondentExtractionService } from '../service/correspondent-extraction.service';

@Component({
  templateUrl: './correspondent-extraction-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class CorrespondentExtractionDeleteDialog {
  correspondentExtraction?: ICorrespondentExtraction;

  protected correspondentExtractionService = inject(CorrespondentExtractionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.correspondentExtractionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
