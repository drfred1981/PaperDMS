import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImageConversionConfig } from '../image-conversion-config.model';
import { ImageConversionConfigService } from '../service/image-conversion-config.service';

@Component({
  templateUrl: './image-conversion-config-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageConversionConfigDeleteDialogComponent {
  imageConversionConfig?: IImageConversionConfig;

  protected imageConversionConfigService = inject(ImageConversionConfigService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageConversionConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
