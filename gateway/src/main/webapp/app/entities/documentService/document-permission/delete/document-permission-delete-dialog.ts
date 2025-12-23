import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentPermission } from '../document-permission.model';
import { DocumentPermissionService } from '../service/document-permission.service';

@Component({
  templateUrl: './document-permission-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentPermissionDeleteDialog {
  documentPermission?: IDocumentPermission;

  protected documentPermissionService = inject(DocumentPermissionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentPermissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
