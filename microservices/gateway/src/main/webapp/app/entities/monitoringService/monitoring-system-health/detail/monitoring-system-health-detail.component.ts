import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMonitoringSystemHealth } from '../monitoring-system-health.model';

@Component({
  selector: 'jhi-monitoring-system-health-detail',
  templateUrl: './monitoring-system-health-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MonitoringSystemHealthDetailComponent {
  monitoringSystemHealth = input<IMonitoringSystemHealth | null>(null);

  previousState(): void {
    window.history.back();
  }
}
