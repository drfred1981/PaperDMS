import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImageConversionHistory } from '../image-conversion-history.model';
import { ImageConversionHistoryService } from '../service/image-conversion-history.service';

@Component({
  templateUrl: './image-conversion-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageConversionHistoryDeleteDialogComponent {
  imageConversionHistory?: IImageConversionHistory;

  protected imageConversionHistoryService = inject(ImageConversionHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageConversionHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
