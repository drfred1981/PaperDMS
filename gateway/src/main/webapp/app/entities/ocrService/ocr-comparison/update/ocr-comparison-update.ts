import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IOcrComparison } from '../ocr-comparison.model';
import { OcrComparisonService } from '../service/ocr-comparison.service';

import { OcrComparisonFormGroup, OcrComparisonFormService } from './ocr-comparison-form.service';

@Component({
  selector: 'jhi-ocr-comparison-update',
  templateUrl: './ocr-comparison-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class OcrComparisonUpdate implements OnInit {
  isSaving = false;
  ocrComparison: IOcrComparison | null = null;
  ocrEngineValues = Object.keys(OcrEngine);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ocrComparisonService = inject(OcrComparisonService);
  protected ocrComparisonFormService = inject(OcrComparisonFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OcrComparisonFormGroup = this.ocrComparisonFormService.createOcrComparisonFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ocrComparison }) => {
      this.ocrComparison = ocrComparison;
      if (ocrComparison) {
        this.updateForm(ocrComparison);
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
    const ocrComparison = this.ocrComparisonFormService.getOcrComparison(this.editForm);
    if (ocrComparison.id === null) {
      this.subscribeToSaveResponse(this.ocrComparisonService.create(ocrComparison));
    } else {
      this.subscribeToSaveResponse(this.ocrComparisonService.update(ocrComparison));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOcrComparison>>): void {
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

  protected updateForm(ocrComparison: IOcrComparison): void {
    this.ocrComparison = ocrComparison;
    this.ocrComparisonFormService.resetForm(this.editForm, ocrComparison);
  }
}
