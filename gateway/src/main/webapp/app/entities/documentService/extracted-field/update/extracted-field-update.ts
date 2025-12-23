import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ExtractionMethod } from 'app/entities/enumerations/extraction-method.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IExtractedField } from '../extracted-field.model';
import { ExtractedFieldService } from '../service/extracted-field.service';

import { ExtractedFieldFormGroup, ExtractedFieldFormService } from './extracted-field-form.service';

@Component({
  selector: 'jhi-extracted-field-update',
  templateUrl: './extracted-field-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ExtractedFieldUpdate implements OnInit {
  isSaving = false;
  extractedField: IExtractedField | null = null;
  extractionMethodValues = Object.keys(ExtractionMethod);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected extractedFieldService = inject(ExtractedFieldService);
  protected extractedFieldFormService = inject(ExtractedFieldFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExtractedFieldFormGroup = this.extractedFieldFormService.createExtractedFieldFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extractedField }) => {
      this.extractedField = extractedField;
      if (extractedField) {
        this.updateForm(extractedField);
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
    const extractedField = this.extractedFieldFormService.getExtractedField(this.editForm);
    if (extractedField.id === null) {
      this.subscribeToSaveResponse(this.extractedFieldService.create(extractedField));
    } else {
      this.subscribeToSaveResponse(this.extractedFieldService.update(extractedField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtractedField>>): void {
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

  protected updateForm(extractedField: IExtractedField): void {
    this.extractedField = extractedField;
    this.extractedFieldFormService.resetForm(this.editForm, extractedField);
  }
}
