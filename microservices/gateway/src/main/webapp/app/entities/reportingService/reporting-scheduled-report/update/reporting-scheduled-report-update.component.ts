import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ReportFormat } from 'app/entities/enumerations/report-format.model';
import { ReportingScheduledReportService } from '../service/reporting-scheduled-report.service';
import { IReportingScheduledReport } from '../reporting-scheduled-report.model';
import { ReportingScheduledReportFormGroup, ReportingScheduledReportFormService } from './reporting-scheduled-report-form.service';

@Component({
  selector: 'jhi-reporting-scheduled-report-update',
  templateUrl: './reporting-scheduled-report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportingScheduledReportUpdateComponent implements OnInit {
  isSaving = false;
  reportingScheduledReport: IReportingScheduledReport | null = null;
  reportTypeValues = Object.keys(ReportType);
  reportFormatValues = Object.keys(ReportFormat);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportingScheduledReportService = inject(ReportingScheduledReportService);
  protected reportingScheduledReportFormService = inject(ReportingScheduledReportFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportingScheduledReportFormGroup = this.reportingScheduledReportFormService.createReportingScheduledReportFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingScheduledReport }) => {
      this.reportingScheduledReport = reportingScheduledReport;
      if (reportingScheduledReport) {
        this.updateForm(reportingScheduledReport);
      }
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
    const reportingScheduledReport = this.reportingScheduledReportFormService.getReportingScheduledReport(this.editForm);
    if (reportingScheduledReport.id !== null) {
      this.subscribeToSaveResponse(this.reportingScheduledReportService.update(reportingScheduledReport));
    } else {
      this.subscribeToSaveResponse(this.reportingScheduledReportService.create(reportingScheduledReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingScheduledReport>>): void {
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

  protected updateForm(reportingScheduledReport: IReportingScheduledReport): void {
    this.reportingScheduledReport = reportingScheduledReport;
    this.reportingScheduledReportFormService.resetForm(this.editForm, reportingScheduledReport);
  }
}
