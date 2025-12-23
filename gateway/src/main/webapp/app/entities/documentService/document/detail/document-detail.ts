import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocument } from '../document.model';

@Component({
  selector: 'jhi-document-detail',
  templateUrl: './document-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentDetail {
  document = input<IDocument | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
