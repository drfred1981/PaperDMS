import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImageConversionStatistics } from '../image-conversion-statistics.model';
import { ImageConversionStatisticsService } from '../service/image-conversion-statistics.service';

@Component({
  templateUrl: './image-conversion-statistics-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageConversionStatisticsDeleteDialogComponent {
  imageConversionStatistics?: IImageConversionStatistics;

  protected imageConversionStatisticsService = inject(ImageConversionStatisticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageConversionStatisticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
