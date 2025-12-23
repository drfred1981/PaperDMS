import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IBookmark } from '../bookmark.model';
import { BookmarkService } from '../service/bookmark.service';

@Component({
  templateUrl: './bookmark-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class BookmarkDeleteDialog {
  bookmark?: IBookmark;

  protected bookmarkService = inject(BookmarkService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookmarkService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
