import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentAudit } from '../document-audit.model';
import { DocumentAuditService } from '../service/document-audit.service';

@Component({
  templateUrl: './document-audit-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentAuditDeleteDialog {
  documentAudit?: IDocumentAudit;

  protected documentAuditService = inject(DocumentAuditService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentAuditService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
