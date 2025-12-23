import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IExtractedField } from '../extracted-field.model';
import { ExtractedFieldService } from '../service/extracted-field.service';

@Component({
  templateUrl: './extracted-field-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ExtractedFieldDeleteDialog {
  extractedField?: IExtractedField;

  protected extractedFieldService = inject(ExtractedFieldService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extractedFieldService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
