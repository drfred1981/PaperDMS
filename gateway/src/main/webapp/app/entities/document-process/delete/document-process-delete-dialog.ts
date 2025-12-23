import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentProcess } from '../document-process.model';
import { DocumentProcessService } from '../service/document-process.service';

@Component({
  templateUrl: './document-process-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentProcessDeleteDialog {
  documentProcess?: IDocumentProcess;

  protected documentProcessService = inject(DocumentProcessService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentProcessService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
