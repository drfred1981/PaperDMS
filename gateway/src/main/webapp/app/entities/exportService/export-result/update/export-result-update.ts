import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ExportResultStatus } from 'app/entities/enumerations/export-result-status.model';
import { IExportJob } from 'app/entities/exportService/export-job/export-job.model';
import { ExportJobService } from 'app/entities/exportService/export-job/service/export-job.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IExportResult } from '../export-result.model';
import { ExportResultService } from '../service/export-result.service';

import { ExportResultFormGroup, ExportResultFormService } from './export-result-form.service';

@Component({
  selector: 'jhi-export-result-update',
  templateUrl: './export-result-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ExportResultUpdate implements OnInit {
  isSaving = false;
  exportResult: IExportResult | null = null;
  exportResultStatusValues = Object.keys(ExportResultStatus);

  exportJobsSharedCollection = signal<IExportJob[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected exportResultService = inject(ExportResultService);
  protected exportResultFormService = inject(ExportResultFormService);
  protected exportJobService = inject(ExportJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExportResultFormGroup = this.exportResultFormService.createExportResultFormGroup();

  compareExportJob = (o1: IExportJob | null, o2: IExportJob | null): boolean => this.exportJobService.compareExportJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exportResult }) => {
      this.exportResult = exportResult;
      if (exportResult) {
        this.updateForm(exportResult);
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
    const exportResult = this.exportResultFormService.getExportResult(this.editForm);
    if (exportResult.id === null) {
      this.subscribeToSaveResponse(this.exportResultService.create(exportResult));
    } else {
      this.subscribeToSaveResponse(this.exportResultService.update(exportResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExportResult>>): void {
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

  protected updateForm(exportResult: IExportResult): void {
    this.exportResult = exportResult;
    this.exportResultFormService.resetForm(this.editForm, exportResult);

    this.exportJobsSharedCollection.set(
      this.exportJobService.addExportJobToCollectionIfMissing<IExportJob>(this.exportJobsSharedCollection(), exportResult.exportJob),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exportJobService
      .query()
      .pipe(map((res: HttpResponse<IExportJob[]>) => res.body ?? []))
      .pipe(
        map((exportJobs: IExportJob[]) =>
          this.exportJobService.addExportJobToCollectionIfMissing<IExportJob>(exportJobs, this.exportResult?.exportJob),
        ),
      )
      .subscribe((exportJobs: IExportJob[]) => this.exportJobsSharedCollection.set(exportJobs));
  }
}
