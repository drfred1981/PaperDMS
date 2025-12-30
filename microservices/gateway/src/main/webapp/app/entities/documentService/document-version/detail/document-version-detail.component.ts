import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentVersion } from '../document-version.model';

@Component({
  selector: 'jhi-document-version-detail',
  templateUrl: './document-version-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentVersionDetailComponent {
  documentVersion = input<IDocumentVersion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
