import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentComment } from '../document-comment.model';
import { DocumentCommentService } from '../service/document-comment.service';

@Component({
  templateUrl: './document-comment-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentCommentDeleteDialog {
  documentComment?: IDocumentComment;

  protected documentCommentService = inject(DocumentCommentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentCommentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
