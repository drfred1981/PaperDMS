import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ISystemMetric } from '../system-metric.model';

@Component({
  selector: 'jhi-system-metric-detail',
  templateUrl: './system-metric-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class SystemMetricDetail {
  systemMetric = input<ISystemMetric | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
