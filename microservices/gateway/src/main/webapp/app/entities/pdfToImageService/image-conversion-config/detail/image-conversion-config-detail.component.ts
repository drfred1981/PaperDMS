import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IImageConversionConfig } from '../image-conversion-config.model';

@Component({
  selector: 'jhi-image-conversion-config-detail',
  templateUrl: './image-conversion-config-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ImageConversionConfigDetailComponent {
  imageConversionConfig = input<IImageConversionConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
