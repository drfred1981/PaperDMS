import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentTemplate } from '../document-template.model';

@Component({
  selector: 'jhi-document-template-detail',
  templateUrl: './document-template-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentTemplateDetailComponent {
  documentTemplate = input<IDocumentTemplate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
