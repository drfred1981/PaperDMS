import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentRelation } from '../document-relation.model';
import { DocumentRelationService } from '../service/document-relation.service';

@Component({
  templateUrl: './document-relation-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentRelationDeleteDialog {
  documentRelation?: IDocumentRelation;

  protected documentRelationService = inject(DocumentRelationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentRelationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
