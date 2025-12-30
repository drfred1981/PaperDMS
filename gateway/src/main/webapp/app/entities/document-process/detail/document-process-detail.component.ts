import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDocumentProcess } from '../document-process.model';

@Component({
  selector: 'jhi-document-process-detail',
  templateUrl: './document-process-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DocumentProcessDetailComponent {
  documentProcess = input<IDocumentProcess | null>(null);

  previousState(): void {
    window.history.back();
  }
}
