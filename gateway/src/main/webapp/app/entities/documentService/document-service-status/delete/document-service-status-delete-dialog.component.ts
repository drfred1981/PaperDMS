import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusService } from '../service/document-service-status.service';

@Component({
  templateUrl: './document-service-status-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentServiceStatusDeleteDialogComponent {
  documentServiceStatus?: IDocumentServiceStatus;

  protected documentServiceStatusService = inject(DocumentServiceStatusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentServiceStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
