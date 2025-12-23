import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDocumentProcess } from '../document-process.model';

@Component({
  selector: 'jhi-document-process-detail',
  templateUrl: './document-process-detail.html',
  imports: [SharedModule, RouterLink],
})
export class DocumentProcessDetail {
  documentProcess = input<IDocumentProcess | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
