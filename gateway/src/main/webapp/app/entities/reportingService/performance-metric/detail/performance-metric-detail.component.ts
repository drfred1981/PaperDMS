import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IPerformanceMetric } from '../performance-metric.model';

@Component({
  selector: 'jhi-performance-metric-detail',
  templateUrl: './performance-metric-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class PerformanceMetricDetailComponent {
  performanceMetric = input<IPerformanceMetric | null>(null);

  previousState(): void {
    window.history.back();
  }
}
