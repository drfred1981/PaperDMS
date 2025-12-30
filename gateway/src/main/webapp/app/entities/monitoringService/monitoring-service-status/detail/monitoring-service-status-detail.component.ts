import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMonitoringServiceStatus } from '../monitoring-service-status.model';

@Component({
  selector: 'jhi-monitoring-service-status-detail',
  templateUrl: './monitoring-service-status-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MonitoringServiceStatusDetailComponent {
  monitoringServiceStatus = input<IMonitoringServiceStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
