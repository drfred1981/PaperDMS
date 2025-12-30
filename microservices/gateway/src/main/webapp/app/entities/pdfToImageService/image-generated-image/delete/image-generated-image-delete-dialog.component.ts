import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImageGeneratedImage } from '../image-generated-image.model';
import { ImageGeneratedImageService } from '../service/image-generated-image.service';

@Component({
  templateUrl: './image-generated-image-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageGeneratedImageDeleteDialogComponent {
  imageGeneratedImage?: IImageGeneratedImage;

  protected imageGeneratedImageService = inject(ImageGeneratedImageService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageGeneratedImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
