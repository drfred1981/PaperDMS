import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentStatistics } from '../document-statistics.model';

@Component({
  selector: 'jhi-document-statistics-detail',
  templateUrl: './document-statistics-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentStatisticsDetail {
  documentStatistics = input<IDocumentStatistics | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
