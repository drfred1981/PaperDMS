import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetaPermissionGroup } from '../meta-permission-group.model';
import { MetaPermissionGroupService } from '../service/meta-permission-group.service';

@Component({
  templateUrl: './meta-permission-group-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetaPermissionGroupDeleteDialogComponent {
  metaPermissionGroup?: IMetaPermissionGroup;

  protected metaPermissionGroupService = inject(MetaPermissionGroupService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaPermissionGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
