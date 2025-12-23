import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ReportExecutionStatus } from 'app/entities/enumerations/report-execution-status.model';
import { IScheduledReport } from 'app/entities/reportingService/scheduled-report/scheduled-report.model';
import { ScheduledReportService } from 'app/entities/reportingService/scheduled-report/service/scheduled-report.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';

import { ReportExecutionFormGroup, ReportExecutionFormService } from './report-execution-form.service';

@Component({
  selector: 'jhi-report-execution-update',
  templateUrl: './report-execution-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ReportExecutionUpdate implements OnInit {
  isSaving = false;
  reportExecution: IReportExecution | null = null;
  reportExecutionStatusValues = Object.keys(ReportExecutionStatus);

  scheduledReportsSharedCollection = signal<IScheduledReport[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportExecutionService = inject(ReportExecutionService);
  protected reportExecutionFormService = inject(ReportExecutionFormService);
  protected scheduledReportService = inject(ScheduledReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportExecutionFormGroup = this.reportExecutionFormService.createReportExecutionFormGroup();

  compareScheduledReport = (o1: IScheduledReport | null, o2: IScheduledReport | null): boolean =>
    this.scheduledReportService.compareScheduledReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportExecution }) => {
      this.reportExecution = reportExecution;
      if (reportExecution) {
        this.updateForm(reportExecution);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportExecution = this.reportExecutionFormService.getReportExecution(this.editForm);
    if (reportExecution.id === null) {
      this.subscribeToSaveResponse(this.reportExecutionService.create(reportExecution));
    } else {
      this.subscribeToSaveResponse(this.reportExecutionService.update(reportExecution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportExecution>>): void {
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

  protected updateForm(reportExecution: IReportExecution): void {
    this.reportExecution = reportExecution;
    this.reportExecutionFormService.resetForm(this.editForm, reportExecution);

    this.scheduledReportsSharedCollection.set(
      this.scheduledReportService.addScheduledReportToCollectionIfMissing<IScheduledReport>(
        this.scheduledReportsSharedCollection(),
        reportExecution.scheduledReport,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.scheduledReportService
      .query()
      .pipe(map((res: HttpResponse<IScheduledReport[]>) => res.body ?? []))
      .pipe(
        map((scheduledReports: IScheduledReport[]) =>
          this.scheduledReportService.addScheduledReportToCollectionIfMissing<IScheduledReport>(
            scheduledReports,
            this.reportExecution?.scheduledReport,
          ),
        ),
      )
      .subscribe((scheduledReports: IScheduledReport[]) => this.scheduledReportsSharedCollection.set(scheduledReports));
  }
}
