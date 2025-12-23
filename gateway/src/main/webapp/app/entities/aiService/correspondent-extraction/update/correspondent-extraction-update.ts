import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { ICorrespondentExtraction } from '../correspondent-extraction.model';
import { CorrespondentExtractionService } from '../service/correspondent-extraction.service';

import { CorrespondentExtractionFormGroup, CorrespondentExtractionFormService } from './correspondent-extraction-form.service';

@Component({
  selector: 'jhi-correspondent-extraction-update',
  templateUrl: './correspondent-extraction-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CorrespondentExtractionUpdate implements OnInit {
  isSaving = false;
  correspondentExtraction: ICorrespondentExtraction | null = null;
  aiJobStatusValues = Object.keys(AiJobStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected correspondentExtractionService = inject(CorrespondentExtractionService);
  protected correspondentExtractionFormService = inject(CorrespondentExtractionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorrespondentExtractionFormGroup = this.correspondentExtractionFormService.createCorrespondentExtractionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ correspondentExtraction }) => {
      this.correspondentExtraction = correspondentExtraction;
      if (correspondentExtraction) {
        this.updateForm(correspondentExtraction);
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
    const correspondentExtraction = this.correspondentExtractionFormService.getCorrespondentExtraction(this.editForm);
    if (correspondentExtraction.id === null) {
      this.subscribeToSaveResponse(this.correspondentExtractionService.create(correspondentExtraction));
    } else {
      this.subscribeToSaveResponse(this.correspondentExtractionService.update(correspondentExtraction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorrespondentExtraction>>): void {
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

  protected updateForm(correspondentExtraction: ICorrespondentExtraction): void {
    this.correspondentExtraction = correspondentExtraction;
    this.correspondentExtractionFormService.resetForm(this.editForm, correspondentExtraction);
  }
}
