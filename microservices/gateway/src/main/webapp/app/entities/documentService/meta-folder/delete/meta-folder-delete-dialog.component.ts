import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetaFolder } from '../meta-folder.model';
import { MetaFolderService } from '../service/meta-folder.service';

@Component({
  templateUrl: './meta-folder-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetaFolderDeleteDialogComponent {
  metaFolder?: IMetaFolder;

  protected metaFolderService = inject(MetaFolderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaFolderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
