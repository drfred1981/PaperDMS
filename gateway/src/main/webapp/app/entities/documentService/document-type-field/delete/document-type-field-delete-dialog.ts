import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentTypeField } from '../document-type-field.model';
import { DocumentTypeFieldService } from '../service/document-type-field.service';

@Component({
  templateUrl: './document-type-field-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentTypeFieldDeleteDialog {
  documentTypeField?: IDocumentTypeField;

  protected documentTypeFieldService = inject(DocumentTypeFieldService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTypeFieldService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
