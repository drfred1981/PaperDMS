import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentType } from '../document-type.model';

@Component({
  selector: 'jhi-document-type-detail',
  templateUrl: './document-type-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentTypeDetail {
  documentType = input<IDocumentType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
