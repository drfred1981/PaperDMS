import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IScannedPage } from '../scanned-page.model';

@Component({
  selector: 'jhi-scanned-page-detail',
  templateUrl: './scanned-page-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ScannedPageDetailComponent {
  scannedPage = input<IScannedPage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
