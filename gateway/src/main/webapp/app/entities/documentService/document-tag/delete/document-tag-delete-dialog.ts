import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentTag } from '../document-tag.model';
import { DocumentTagService } from '../service/document-tag.service';

@Component({
  templateUrl: './document-tag-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentTagDeleteDialog {
  documentTag?: IDocumentTag;

  protected documentTagService = inject(DocumentTagService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
