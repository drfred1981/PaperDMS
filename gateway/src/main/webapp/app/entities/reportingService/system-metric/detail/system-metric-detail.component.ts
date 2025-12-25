import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ISystemMetric } from '../system-metric.model';

@Component({
  selector: 'jhi-system-metric-detail',
  templateUrl: './system-metric-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class SystemMetricDetailComponent {
  systemMetric = input<ISystemMetric | null>(null);

  previousState(): void {
    window.history.back();
  }
}
