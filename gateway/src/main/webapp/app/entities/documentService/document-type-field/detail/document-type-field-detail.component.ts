import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentTypeField } from '../document-type-field.model';

@Component({
  selector: 'jhi-document-type-field-detail',
  templateUrl: './document-type-field-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentTypeFieldDetailComponent {
  documentTypeField = input<IDocumentTypeField | null>(null);

  previousState(): void {
    window.history.back();
  }
}
