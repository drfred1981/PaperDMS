import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentStatistics } from '../document-statistics.model';

@Component({
  selector: 'jhi-document-statistics-detail',
  templateUrl: './document-statistics-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentStatisticsDetailComponent {
  documentStatistics = input<IDocumentStatistics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
