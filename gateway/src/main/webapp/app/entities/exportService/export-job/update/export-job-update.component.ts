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
import { IExportPattern } from 'app/entities/exportService/export-pattern/export-pattern.model';
import { ExportPatternService } from 'app/entities/exportService/export-pattern/service/export-pattern.service';
import { ExportFormat } from 'app/entities/enumerations/export-format.model';
import { ExportStatus } from 'app/entities/enumerations/export-status.model';
import { ExportJobService } from '../service/export-job.service';
import { IExportJob } from '../export-job.model';
import { ExportJobFormGroup, ExportJobFormService } from './export-job-form.service';

@Component({
  selector: 'jhi-export-job-update',
  templateUrl: './export-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExportJobUpdateComponent implements OnInit {
  isSaving = false;
  exportJob: IExportJob | null = null;
  exportFormatValues = Object.keys(ExportFormat);
  exportStatusValues = Object.keys(ExportStatus);

  exportPatternsSharedCollection: IExportPattern[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected exportJobService = inject(ExportJobService);
  protected exportJobFormService = inject(ExportJobFormService);
  protected exportPatternService = inject(ExportPatternService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExportJobFormGroup = this.exportJobFormService.createExportJobFormGroup();

  compareExportPattern = (o1: IExportPattern | null, o2: IExportPattern | null): boolean =>
    this.exportPatternService.compareExportPattern(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exportJob }) => {
      this.exportJob = exportJob;
      if (exportJob) {
        this.updateForm(exportJob);
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
    const exportJob = this.exportJobFormService.getExportJob(this.editForm);
    if (exportJob.id !== null) {
      this.subscribeToSaveResponse(this.exportJobService.update(exportJob));
    } else {
      this.subscribeToSaveResponse(this.exportJobService.create(exportJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExportJob>>): void {
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

  protected updateForm(exportJob: IExportJob): void {
    this.exportJob = exportJob;
    this.exportJobFormService.resetForm(this.editForm, exportJob);

    this.exportPatternsSharedCollection = this.exportPatternService.addExportPatternToCollectionIfMissing<IExportPattern>(
      this.exportPatternsSharedCollection,
      exportJob.exportPattern,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exportPatternService
      .query()
      .pipe(map((res: HttpResponse<IExportPattern[]>) => res.body ?? []))
      .pipe(
        map((exportPatterns: IExportPattern[]) =>
          this.exportPatternService.addExportPatternToCollectionIfMissing<IExportPattern>(exportPatterns, this.exportJob?.exportPattern),
        ),
      )
      .subscribe((exportPatterns: IExportPattern[]) => (this.exportPatternsSharedCollection = exportPatterns));
  }
}
