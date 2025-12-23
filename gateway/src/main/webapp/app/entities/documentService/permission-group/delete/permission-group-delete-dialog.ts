import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IPermissionGroup } from '../permission-group.model';
import { PermissionGroupService } from '../service/permission-group.service';

@Component({
  templateUrl: './permission-group-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class PermissionGroupDeleteDialog {
  permissionGroup?: IPermissionGroup;

  protected permissionGroupService = inject(PermissionGroupService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
