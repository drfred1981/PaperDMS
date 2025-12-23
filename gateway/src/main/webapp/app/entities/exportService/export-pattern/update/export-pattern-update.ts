import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IExportPattern } from '../export-pattern.model';
import { ExportPatternService } from '../service/export-pattern.service';

import { ExportPatternFormGroup, ExportPatternFormService } from './export-pattern-form.service';

@Component({
  selector: 'jhi-export-pattern-update',
  templateUrl: './export-pattern-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ExportPatternUpdate implements OnInit {
  isSaving = false;
  exportPattern: IExportPattern | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected exportPatternService = inject(ExportPatternService);
  protected exportPatternFormService = inject(ExportPatternFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExportPatternFormGroup = this.exportPatternFormService.createExportPatternFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exportPattern }) => {
      this.exportPattern = exportPattern;
      if (exportPattern) {
        this.updateForm(exportPattern);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exportPattern = this.exportPatternFormService.getExportPattern(this.editForm);
    if (exportPattern.id === null) {
      this.subscribeToSaveResponse(this.exportPatternService.create(exportPattern));
    } else {
      this.subscribeToSaveResponse(this.exportPatternService.update(exportPattern));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExportPattern>>): void {
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

  protected updateForm(exportPattern: IExportPattern): void {
    this.exportPattern = exportPattern;
    this.exportPatternFormService.resetForm(this.editForm, exportPattern);
  }
}
