import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IScannedPage } from '../scanned-page.model';

@Component({
  selector: 'jhi-scanned-page-detail',
  templateUrl: './scanned-page-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class ScannedPageDetail {
  scannedPage = input<IScannedPage | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
