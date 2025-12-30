import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentType } from '../document-type.model';

@Component({
  selector: 'jhi-document-type-detail',
  templateUrl: './document-type-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentTypeDetailComponent {
  documentType = input<IDocumentType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
