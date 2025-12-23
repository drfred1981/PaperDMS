import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentTypeField } from '../document-type-field.model';

@Component({
  selector: 'jhi-document-type-field-detail',
  templateUrl: './document-type-field-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentTypeFieldDetail {
  documentTypeField = input<IDocumentTypeField | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
