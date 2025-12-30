import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';

@Component({
  selector: 'jhi-monitoring-document-watch-detail',
  templateUrl: './monitoring-document-watch-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MonitoringDocumentWatchDetailComponent {
  monitoringDocumentWatch = input<IMonitoringDocumentWatch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
