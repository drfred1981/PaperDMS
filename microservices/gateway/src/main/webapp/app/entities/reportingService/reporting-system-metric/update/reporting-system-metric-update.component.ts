import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReportingSystemMetric } from '../reporting-system-metric.model';
import { ReportingSystemMetricService } from '../service/reporting-system-metric.service';
import { ReportingSystemMetricFormGroup, ReportingSystemMetricFormService } from './reporting-system-metric-form.service';

@Component({
  selector: 'jhi-reporting-system-metric-update',
  templateUrl: './reporting-system-metric-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportingSystemMetricUpdateComponent implements OnInit {
  isSaving = false;
  reportingSystemMetric: IReportingSystemMetric | null = null;

  protected reportingSystemMetricService = inject(ReportingSystemMetricService);
  protected reportingSystemMetricFormService = inject(ReportingSystemMetricFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportingSystemMetricFormGroup = this.reportingSystemMetricFormService.createReportingSystemMetricFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingSystemMetric }) => {
      this.reportingSystemMetric = reportingSystemMetric;
      if (reportingSystemMetric) {
        this.updateForm(reportingSystemMetric);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportingSystemMetric = this.reportingSystemMetricFormService.getReportingSystemMetric(this.editForm);
    if (reportingSystemMetric.id !== null) {
      this.subscribeToSaveResponse(this.reportingSystemMetricService.update(reportingSystemMetric));
    } else {
      this.subscribeToSaveResponse(this.reportingSystemMetricService.create(reportingSystemMetric));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingSystemMetric>>): void {
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

  protected updateForm(reportingSystemMetric: IReportingSystemMetric): void {
    this.reportingSystemMetric = reportingSystemMetric;
    this.reportingSystemMetricFormService.resetForm(this.editForm, reportingSystemMetric);
  }
}
