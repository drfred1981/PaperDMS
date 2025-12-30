import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetaSmartFolder } from '../meta-smart-folder.model';
import { MetaSmartFolderService } from '../service/meta-smart-folder.service';

@Component({
  templateUrl: './meta-smart-folder-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetaSmartFolderDeleteDialogComponent {
  metaSmartFolder?: IMetaSmartFolder;

  protected metaSmartFolderService = inject(MetaSmartFolderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaSmartFolderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
