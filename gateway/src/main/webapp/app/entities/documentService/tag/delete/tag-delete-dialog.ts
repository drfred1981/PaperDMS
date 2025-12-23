import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { TagService } from '../service/tag.service';
import { ITag } from '../tag.model';

@Component({
  templateUrl: './tag-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class TagDeleteDialog {
  tag?: ITag;

  protected tagService = inject(TagService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
