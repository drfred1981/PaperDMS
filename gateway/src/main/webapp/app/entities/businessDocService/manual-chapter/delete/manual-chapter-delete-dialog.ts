import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IManualChapter } from '../manual-chapter.model';
import { ManualChapterService } from '../service/manual-chapter.service';

@Component({
  templateUrl: './manual-chapter-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ManualChapterDeleteDialog {
  manualChapter?: IManualChapter;

  protected manualChapterService = inject(ManualChapterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualChapterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
