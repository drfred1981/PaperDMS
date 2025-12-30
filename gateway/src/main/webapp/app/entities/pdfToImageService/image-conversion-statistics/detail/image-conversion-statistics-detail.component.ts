import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IImageConversionStatistics } from '../image-conversion-statistics.model';

@Component({
  selector: 'jhi-image-conversion-statistics-detail',
  templateUrl: './image-conversion-statistics-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ImageConversionStatisticsDetailComponent {
  imageConversionStatistics = input<IImageConversionStatistics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
