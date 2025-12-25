import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentWatch } from '../document-watch.model';

@Component({
  selector: 'jhi-document-watch-detail',
  templateUrl: './document-watch-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentWatchDetailComponent {
  documentWatch = input<IDocumentWatch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
