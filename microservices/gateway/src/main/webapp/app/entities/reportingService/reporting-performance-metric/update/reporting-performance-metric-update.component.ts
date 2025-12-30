import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MetricType } from 'app/entities/enumerations/metric-type.model';
import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';
import { ReportingPerformanceMetricService } from '../service/reporting-performance-metric.service';
import { ReportingPerformanceMetricFormGroup, ReportingPerformanceMetricFormService } from './reporting-performance-metric-form.service';

@Component({
  selector: 'jhi-reporting-performance-metric-update',
  templateUrl: './reporting-performance-metric-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportingPerformanceMetricUpdateComponent implements OnInit {
  isSaving = false;
  reportingPerformanceMetric: IReportingPerformanceMetric | null = null;
  metricTypeValues = Object.keys(MetricType);

  protected reportingPerformanceMetricService = inject(ReportingPerformanceMetricService);
  protected reportingPerformanceMetricFormService = inject(ReportingPerformanceMetricFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportingPerformanceMetricFormGroup = this.reportingPerformanceMetricFormService.createReportingPerformanceMetricFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingPerformanceMetric }) => {
      this.reportingPerformanceMetric = reportingPerformanceMetric;
      if (reportingPerformanceMetric) {
        this.updateForm(reportingPerformanceMetric);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportingPerformanceMetric = this.reportingPerformanceMetricFormService.getReportingPerformanceMetric(this.editForm);
    if (reportingPerformanceMetric.id !== null) {
      this.subscribeToSaveResponse(this.reportingPerformanceMetricService.update(reportingPerformanceMetric));
    } else {
      this.subscribeToSaveResponse(this.reportingPerformanceMetricService.create(reportingPerformanceMetric));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingPerformanceMetric>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reportingPerformanceMetric: IReportingPerformanceMetric): void {
    this.reportingPerformanceMetric = reportingPerformanceMetric;
    this.reportingPerformanceMetricFormService.resetForm(this.editForm, reportingPerformanceMetric);
  }
}
