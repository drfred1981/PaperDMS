import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentTemplate } from '../document-template.model';
import { DocumentTemplateService } from '../service/document-template.service';

@Component({
  templateUrl: './document-template-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentTemplateDeleteDialog {
  documentTemplate?: IDocumentTemplate;

  protected documentTemplateService = inject(DocumentTemplateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTemplateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
