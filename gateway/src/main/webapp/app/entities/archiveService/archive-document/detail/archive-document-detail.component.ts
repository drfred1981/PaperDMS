import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IArchiveDocument } from '../archive-document.model';

@Component({
  selector: 'jhi-archive-document-detail',
  templateUrl: './archive-document-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ArchiveDocumentDetailComponent {
  archiveDocument = input<IArchiveDocument | null>(null);

  previousState(): void {
    window.history.back();
  }
}
