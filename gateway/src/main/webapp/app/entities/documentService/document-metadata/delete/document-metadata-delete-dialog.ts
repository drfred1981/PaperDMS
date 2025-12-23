import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentMetadata } from '../document-metadata.model';
import { DocumentMetadataService } from '../service/document-metadata.service';

@Component({
  templateUrl: './document-metadata-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentMetadataDeleteDialog {
  documentMetadata?: IDocumentMetadata;

  protected documentMetadataService = inject(DocumentMetadataService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentMetadataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
