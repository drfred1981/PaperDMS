import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IPerformanceMetric } from '../performance-metric.model';

@Component({
  selector: 'jhi-performance-metric-detail',
  templateUrl: './performance-metric-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class PerformanceMetricDetail {
  performanceMetric = input<IPerformanceMetric | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
