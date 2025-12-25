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
import { AiCacheService } from '../service/ai-cache.service';
import { IAiCache } from '../ai-cache.model';
import { AiCacheFormGroup, AiCacheFormService } from './ai-cache-form.service';

@Component({
  selector: 'jhi-ai-cache-update',
  templateUrl: './ai-cache-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AiCacheUpdateComponent implements OnInit {
  isSaving = false;
  aiCache: IAiCache | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected aiCacheService = inject(AiCacheService);
  protected aiCacheFormService = inject(AiCacheFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AiCacheFormGroup = this.aiCacheFormService.createAiCacheFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aiCache }) => {
      this.aiCache = aiCache;
      if (aiCache) {
        this.updateForm(aiCache);
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
    const aiCache = this.aiCacheFormService.getAiCache(this.editForm);
    if (aiCache.id !== null) {
      this.subscribeToSaveResponse(this.aiCacheService.update(aiCache));
    } else {
      this.subscribeToSaveResponse(this.aiCacheService.create(aiCache));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAiCache>>): void {
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

  protected updateForm(aiCache: IAiCache): void {
    this.aiCache = aiCache;
    this.aiCacheFormService.resetForm(this.editForm, aiCache);
  }
}
