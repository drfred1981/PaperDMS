import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentTag } from '../document-tag.model';

@Component({
  selector: 'jhi-document-tag-detail',
  templateUrl: './document-tag-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentTagDetailComponent {
  documentTag = input<IDocumentTag | null>(null);

  previousState(): void {
    window.history.back();
  }
}
