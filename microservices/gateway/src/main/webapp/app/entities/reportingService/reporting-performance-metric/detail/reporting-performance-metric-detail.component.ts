import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';

@Component({
  selector: 'jhi-reporting-performance-metric-detail',
  templateUrl: './reporting-performance-metric-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ReportingPerformanceMetricDetailComponent {
  reportingPerformanceMetric = input<IReportingPerformanceMetric | null>(null);

  previousState(): void {
    window.history.back();
  }
}
