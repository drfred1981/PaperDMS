import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MetricType } from 'app/entities/enumerations/metric-type.model';
import SharedModule from 'app/shared/shared.module';
import { IPerformanceMetric } from '../performance-metric.model';
import { PerformanceMetricService } from '../service/performance-metric.service';

import { PerformanceMetricFormGroup, PerformanceMetricFormService } from './performance-metric-form.service';

@Component({
  selector: 'jhi-performance-metric-update',
  templateUrl: './performance-metric-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class PerformanceMetricUpdate implements OnInit {
  isSaving = false;
  performanceMetric: IPerformanceMetric | null = null;
  metricTypeValues = Object.keys(MetricType);

  protected performanceMetricService = inject(PerformanceMetricService);
  protected performanceMetricFormService = inject(PerformanceMetricFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PerformanceMetricFormGroup = this.performanceMetricFormService.createPerformanceMetricFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ performanceMetric }) => {
      this.performanceMetric = performanceMetric;
      if (performanceMetric) {
        this.updateForm(performanceMetric);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const performanceMetric = this.performanceMetricFormService.getPerformanceMetric(this.editForm);
    if (performanceMetric.id === null) {
      this.subscribeToSaveResponse(this.performanceMetricService.create(performanceMetric));
    } else {
      this.subscribeToSaveResponse(this.performanceMetricService.update(performanceMetric));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerformanceMetric>>): void {
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

  protected updateForm(performanceMetric: IPerformanceMetric): void {
    this.performanceMetric = performanceMetric;
    this.performanceMetricFormService.resetForm(this.editForm, performanceMetric);
  }
}
