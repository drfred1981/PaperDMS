import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImagePdfConversionRequest } from '../image-pdf-conversion-request.model';
import { ImagePdfConversionRequestService } from '../service/image-pdf-conversion-request.service';

@Component({
  templateUrl: './image-pdf-conversion-request-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImagePdfConversionRequestDeleteDialogComponent {
  imagePdfConversionRequest?: IImagePdfConversionRequest;

  protected imagePdfConversionRequestService = inject(ImagePdfConversionRequestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imagePdfConversionRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
