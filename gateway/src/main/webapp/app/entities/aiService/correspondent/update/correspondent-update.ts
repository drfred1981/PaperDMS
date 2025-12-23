import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ICorrespondentExtraction } from 'app/entities/aiService/correspondent-extraction/correspondent-extraction.model';
import { CorrespondentExtractionService } from 'app/entities/aiService/correspondent-extraction/service/correspondent-extraction.service';
import { CorrespondentRole } from 'app/entities/enumerations/correspondent-role.model';
import { CorrespondentType } from 'app/entities/enumerations/correspondent-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { ICorrespondent } from '../correspondent.model';
import { CorrespondentService } from '../service/correspondent.service';

import { CorrespondentFormGroup, CorrespondentFormService } from './correspondent-form.service';

@Component({
  selector: 'jhi-correspondent-update',
  templateUrl: './correspondent-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CorrespondentUpdate implements OnInit {
  isSaving = false;
  correspondent: ICorrespondent | null = null;
  correspondentTypeValues = Object.keys(CorrespondentType);
  correspondentRoleValues = Object.keys(CorrespondentRole);

  correspondentExtractionsSharedCollection = signal<ICorrespondentExtraction[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected correspondentService = inject(CorrespondentService);
  protected correspondentFormService = inject(CorrespondentFormService);
  protected correspondentExtractionService = inject(CorrespondentExtractionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorrespondentFormGroup = this.correspondentFormService.createCorrespondentFormGroup();

  compareCorrespondentExtraction = (o1: ICorrespondentExtraction | null, o2: ICorrespondentExtraction | null): boolean =>
    this.correspondentExtractionService.compareCorrespondentExtraction(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ correspondent }) => {
      this.correspondent = correspondent;
      if (correspondent) {
        this.updateForm(correspondent);
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
    const correspondent = this.correspondentFormService.getCorrespondent(this.editForm);
    if (correspondent.id === null) {
      this.subscribeToSaveResponse(this.correspondentService.create(correspondent));
    } else {
      this.subscribeToSaveResponse(this.correspondentService.update(correspondent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorrespondent>>): void {
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

  protected updateForm(correspondent: ICorrespondent): void {
    this.correspondent = correspondent;
    this.correspondentFormService.resetForm(this.editForm, correspondent);

    this.correspondentExtractionsSharedCollection.set(
      this.correspondentExtractionService.addCorrespondentExtractionToCollectionIfMissing<ICorrespondentExtraction>(
        this.correspondentExtractionsSharedCollection(),
        correspondent.extraction,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.correspondentExtractionService
      .query()
      .pipe(map((res: HttpResponse<ICorrespondentExtraction[]>) => res.body ?? []))
      .pipe(
        map((correspondentExtractions: ICorrespondentExtraction[]) =>
          this.correspondentExtractionService.addCorrespondentExtractionToCollectionIfMissing<ICorrespondentExtraction>(
            correspondentExtractions,
            this.correspondent?.extraction,
          ),
        ),
      )
      .subscribe((correspondentExtractions: ICorrespondentExtraction[]) =>
        this.correspondentExtractionsSharedCollection.set(correspondentExtractions),
      );
  }
}
