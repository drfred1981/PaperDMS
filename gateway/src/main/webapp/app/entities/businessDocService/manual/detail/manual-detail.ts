import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IManual } from '../manual.model';

@Component({
  selector: 'jhi-manual-detail',
  templateUrl: './manual-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ManualDetail {
  manual = input<IManual | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
