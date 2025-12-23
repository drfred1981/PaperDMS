import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IFolder } from '../folder.model';
import { FolderService } from '../service/folder.service';

@Component({
  templateUrl: './folder-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class FolderDeleteDialog {
  folder?: IFolder;

  protected folderService = inject(FolderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.folderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
