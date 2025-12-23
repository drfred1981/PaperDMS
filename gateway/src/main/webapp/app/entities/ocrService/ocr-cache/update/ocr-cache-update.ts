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
import { IOcrCache } from '../ocr-cache.model';
import { OcrCacheService } from '../service/ocr-cache.service';

import { OcrCacheFormGroup, OcrCacheFormService } from './ocr-cache-form.service';

@Component({
  selector: 'jhi-ocr-cache-update',
  templateUrl: './ocr-cache-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class OcrCacheUpdate implements OnInit {
  isSaving = false;
  ocrCache: IOcrCache | null = null;
  ocrEngineValues = Object.keys(OcrEngine);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ocrCacheService = inject(OcrCacheService);
  protected ocrCacheFormService = inject(OcrCacheFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OcrCacheFormGroup = this.ocrCacheFormService.createOcrCacheFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ocrCache }) => {
      this.ocrCache = ocrCache;
      if (ocrCache) {
        this.updateForm(ocrCache);
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
    const ocrCache = this.ocrCacheFormService.getOcrCache(this.editForm);
    if (ocrCache.id === null) {
      this.subscribeToSaveResponse(this.ocrCacheService.create(ocrCache));
    } else {
      this.subscribeToSaveResponse(this.ocrCacheService.update(ocrCache));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOcrCache>>): void {
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

  protected updateForm(ocrCache: IOcrCache): void {
    this.ocrCache = ocrCache;
    this.ocrCacheFormService.resetForm(this.editForm, ocrCache);
  }
}
