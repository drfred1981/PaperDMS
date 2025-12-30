import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IReportingSystemMetric } from '../reporting-system-metric.model';

@Component({
  selector: 'jhi-reporting-system-metric-detail',
  templateUrl: './reporting-system-metric-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ReportingSystemMetricDetailComponent {
  reportingSystemMetric = input<IReportingSystemMetric | null>(null);

  previousState(): void {
    window.history.back();
  }
}
