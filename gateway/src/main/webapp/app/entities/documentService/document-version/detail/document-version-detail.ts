import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentVersion } from '../document-version.model';

@Component({
  selector: 'jhi-document-version-detail',
  templateUrl: './document-version-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentVersionDetail {
  documentVersion = input<IDocumentVersion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
