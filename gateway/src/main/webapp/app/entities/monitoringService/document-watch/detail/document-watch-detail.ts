import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentWatch } from '../document-watch.model';

@Component({
  selector: 'jhi-document-watch-detail',
  templateUrl: './document-watch-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentWatchDetail {
  documentWatch = input<IDocumentWatch | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
