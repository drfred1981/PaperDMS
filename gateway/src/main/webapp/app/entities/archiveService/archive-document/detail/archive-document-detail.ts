import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IArchiveDocument } from '../archive-document.model';

@Component({
  selector: 'jhi-archive-document-detail',
  templateUrl: './archive-document-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class ArchiveDocumentDetail {
  archiveDocument = input<IArchiveDocument | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
