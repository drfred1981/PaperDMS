import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentType } from '../document-type.model';
import { DocumentTypeService } from '../service/document-type.service';

@Component({
  templateUrl: './document-type-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentTypeDeleteDialog {
  documentType?: IDocumentType;

  protected documentTypeService = inject(DocumentTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
