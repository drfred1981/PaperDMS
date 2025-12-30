import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IReportingScheduledReport } from 'app/entities/reportingService/reporting-scheduled-report/reporting-scheduled-report.model';
import { ReportingScheduledReportService } from 'app/entities/reportingService/reporting-scheduled-report/service/reporting-scheduled-report.service';
import { ReportingExecutionStatus } from 'app/entities/enumerations/reporting-execution-status.model';
import { ReportingExecutionService } from '../service/reporting-execution.service';
import { IReportingExecution } from '../reporting-execution.model';
import { ReportingExecutionFormGroup, ReportingExecutionFormService } from './reporting-execution-form.service';

@Component({
  selector: 'jhi-reporting-execution-update',
  templateUrl: './reporting-execution-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportingExecutionUpdateComponent implements OnInit {
  isSaving = false;
  reportingExecution: IReportingExecution | null = null;
  reportingExecutionStatusValues = Object.keys(ReportingExecutionStatus);

  reportingScheduledReportsSharedCollection: IReportingScheduledReport[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportingExecutionService = inject(ReportingExecutionService);
  protected reportingExecutionFormService = inject(ReportingExecutionFormService);
  protected reportingScheduledReportService = inject(ReportingScheduledReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportingExecutionFormGroup = this.reportingExecutionFormService.createReportingExecutionFormGroup();

  compareReportingScheduledReport = (o1: IReportingScheduledReport | null, o2: IReportingScheduledReport | null): boolean =>
    this.reportingScheduledReportService.compareReportingScheduledReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingExecution }) => {
      this.reportingExecution = reportingExecution;
      if (reportingExecution) {
        this.updateForm(reportingExecution);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportingExecution = this.reportingExecutionFormService.getReportingExecution(this.editForm);
    if (reportingExecution.id !== null) {
      this.subscribeToSaveResponse(this.reportingExecutionService.update(reportingExecution));
    } else {
      this.subscribeToSaveResponse(this.reportingExecutionService.create(reportingExecution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingExecution>>): void {
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

  protected updateForm(reportingExecution: IReportingExecution): void {
    this.reportingExecution = reportingExecution;
    this.reportingExecutionFormService.resetForm(this.editForm, reportingExecution);

    this.reportingScheduledReportsSharedCollection =
      this.reportingScheduledReportService.addReportingScheduledReportToCollectionIfMissing<IReportingScheduledReport>(
        this.reportingScheduledReportsSharedCollection,
        reportingExecution.scheduledReport,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.reportingScheduledReportService
      .query()
      .pipe(map((res: HttpResponse<IReportingScheduledReport[]>) => res.body ?? []))
      .pipe(
        map((reportingScheduledReports: IReportingScheduledReport[]) =>
          this.reportingScheduledReportService.addReportingScheduledReportToCollectionIfMissing<IReportingScheduledReport>(
            reportingScheduledReports,
            this.reportingExecution?.scheduledReport,
          ),
        ),
      )
      .subscribe(
        (reportingScheduledReports: IReportingScheduledReport[]) =>
          (this.reportingScheduledReportsSharedCollection = reportingScheduledReports),
      );
  }
}
