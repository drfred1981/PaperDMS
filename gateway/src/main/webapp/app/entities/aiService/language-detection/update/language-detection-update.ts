import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { LanguageDetectionMethod } from 'app/entities/enumerations/language-detection-method.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { ILanguageDetection } from '../language-detection.model';
import { LanguageDetectionService } from '../service/language-detection.service';

import { LanguageDetectionFormGroup, LanguageDetectionFormService } from './language-detection-form.service';

@Component({
  selector: 'jhi-language-detection-update',
  templateUrl: './language-detection-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class LanguageDetectionUpdate implements OnInit {
  isSaving = false;
  languageDetection: ILanguageDetection | null = null;
  languageDetectionMethodValues = Object.keys(LanguageDetectionMethod);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected languageDetectionService = inject(LanguageDetectionService);
  protected languageDetectionFormService = inject(LanguageDetectionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LanguageDetectionFormGroup = this.languageDetectionFormService.createLanguageDetectionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ languageDetection }) => {
      this.languageDetection = languageDetection;
      if (languageDetection) {
        this.updateForm(languageDetection);
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
    const languageDetection = this.languageDetectionFormService.getLanguageDetection(this.editForm);
    if (languageDetection.id === null) {
      this.subscribeToSaveResponse(this.languageDetectionService.create(languageDetection));
    } else {
      this.subscribeToSaveResponse(this.languageDetectionService.update(languageDetection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILanguageDetection>>): void {
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

  protected updateForm(languageDetection: ILanguageDetection): void {
    this.languageDetection = languageDetection;
    this.languageDetectionFormService.resetForm(this.editForm, languageDetection);
  }
}
