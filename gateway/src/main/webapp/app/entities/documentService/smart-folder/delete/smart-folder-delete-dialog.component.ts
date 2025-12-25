import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISmartFolder } from '../smart-folder.model';
import { SmartFolderService } from '../service/smart-folder.service';

@Component({
  templateUrl: './smart-folder-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SmartFolderDeleteDialogComponent {
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
