import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImageConversionBatch } from '../image-conversion-batch.model';
import { ImageConversionBatchService } from '../service/image-conversion-batch.service';

@Component({
  templateUrl: './image-conversion-batch-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageConversionBatchDeleteDialogComponent {
  imageConversionBatch?: IImageConversionBatch;

  protected imageConversionBatchService = inject(ImageConversionBatchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageConversionBatchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
