import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { SmartFolderService } from '../service/smart-folder.service';
import { ISmartFolder } from '../smart-folder.model';

@Component({
  templateUrl: './smart-folder-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SmartFolderDeleteDialog {
  smartFolder?: ISmartFolder;

  protected smartFolderService = inject(SmartFolderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.smartFolderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
