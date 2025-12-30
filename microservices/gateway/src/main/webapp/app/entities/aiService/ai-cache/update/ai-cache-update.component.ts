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
import { AICacheService } from '../service/ai-cache.service';
import { IAICache } from '../ai-cache.model';
import { AICacheFormGroup, AICacheFormService } from './ai-cache-form.service';

@Component({
  selector: 'jhi-ai-cache-update',
  templateUrl: './ai-cache-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AICacheUpdateComponent implements OnInit {
  isSaving = false;
  aICache: IAICache | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected aICacheService = inject(AICacheService);
  protected aICacheFormService = inject(AICacheFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AICacheFormGroup = this.aICacheFormService.createAICacheFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aICache }) => {
      this.aICache = aICache;
      if (aICache) {
        this.updateForm(aICache);
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
    const aICache = this.aICacheFormService.getAICache(this.editForm);
    if (aICache.id !== null) {
      this.subscribeToSaveResponse(this.aICacheService.update(aICache));
    } else {
      this.subscribeToSaveResponse(this.aICacheService.create(aICache));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAICache>>): void {
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

  protected updateForm(aICache: IAICache): void {
    this.aICache = aICache;
    this.aICacheFormService.resetForm(this.editForm, aICache);
  }
}
