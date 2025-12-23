import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertFrequency } from 'app/entities/enumerations/alert-frequency.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { ISavedSearch } from '../saved-search.model';
import { SavedSearchService } from '../service/saved-search.service';

import { SavedSearchFormGroup, SavedSearchFormService } from './saved-search-form.service';

@Component({
  selector: 'jhi-saved-search-update',
  templateUrl: './saved-search-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SavedSearchUpdate implements OnInit {
  isSaving = false;
  savedSearch: ISavedSearch | null = null;
  alertFrequencyValues = Object.keys(AlertFrequency);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected savedSearchService = inject(SavedSearchService);
  protected savedSearchFormService = inject(SavedSearchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SavedSearchFormGroup = this.savedSearchFormService.createSavedSearchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ savedSearch }) => {
      this.savedSearch = savedSearch;
      if (savedSearch) {
        this.updateForm(savedSearch);
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
    const savedSearch = this.savedSearchFormService.getSavedSearch(this.editForm);
    if (savedSearch.id === null) {
      this.subscribeToSaveResponse(this.savedSearchService.create(savedSearch));
    } else {
      this.subscribeToSaveResponse(this.savedSearchService.update(savedSearch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISavedSearch>>): void {
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

  protected updateForm(savedSearch: ISavedSearch): void {
    this.savedSearch = savedSearch;
    this.savedSearchFormService.resetForm(this.editForm, savedSearch);
  }
}
