import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentExtractedField } from '../document-extracted-field.model';
import { DocumentExtractedFieldService } from '../service/document-extracted-field.service';

@Component({
  templateUrl: './document-extracted-field-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentExtractedFieldDeleteDialogComponent {
  documentExtractedField?: IDocumentExtractedField;

  protected documentExtractedFieldService = inject(DocumentExtractedFieldService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentExtractedFieldService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
