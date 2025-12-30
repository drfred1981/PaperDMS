import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IImageConversionBatch } from '../image-conversion-batch.model';

@Component({
  selector: 'jhi-image-conversion-batch-detail',
  templateUrl: './image-conversion-batch-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ImageConversionBatchDetailComponent {
  imageConversionBatch = input<IImageConversionBatch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
