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
import { ScheduledReportService } from '../service/scheduled-report.service';
import { IScheduledReport } from '../scheduled-report.model';
import { ScheduledReportFormGroup, ScheduledReportFormService } from './scheduled-report-form.service';

@Component({
  selector: 'jhi-scheduled-report-update',
  templateUrl: './scheduled-report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ScheduledReportUpdateComponent implements OnInit {
  isSaving = false;
  scheduledReport: IScheduledReport | null = null;
  reportTypeValues = Object.keys(ReportType);
  reportFormatValues = Object.keys(ReportFormat);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected scheduledReportService = inject(ScheduledReportService);
  protected scheduledReportFormService = inject(ScheduledReportFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScheduledReportFormGroup = this.scheduledReportFormService.createScheduledReportFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scheduledReport }) => {
      this.scheduledReport = scheduledReport;
      if (scheduledReport) {
        this.updateForm(scheduledReport);
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
    const scheduledReport = this.scheduledReportFormService.getScheduledReport(this.editForm);
    if (scheduledReport.id !== null) {
      this.subscribeToSaveResponse(this.scheduledReportService.update(scheduledReport));
    } else {
      this.subscribeToSaveResponse(this.scheduledReportService.create(scheduledReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScheduledReport>>): void {
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

  protected updateForm(scheduledReport: IScheduledReport): void {
    this.scheduledReport = scheduledReport;
    this.scheduledReportFormService.resetForm(this.editForm, scheduledReport);
  }
}
