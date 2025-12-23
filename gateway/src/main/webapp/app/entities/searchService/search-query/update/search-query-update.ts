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
import { ISearchQuery } from '../search-query.model';
import { SearchQueryService } from '../service/search-query.service';

import { SearchQueryFormGroup, SearchQueryFormService } from './search-query-form.service';

@Component({
  selector: 'jhi-search-query-update',
  templateUrl: './search-query-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SearchQueryUpdate implements OnInit {
  isSaving = false;
  searchQuery: ISearchQuery | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected searchQueryService = inject(SearchQueryService);
  protected searchQueryFormService = inject(SearchQueryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SearchQueryFormGroup = this.searchQueryFormService.createSearchQueryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ searchQuery }) => {
      this.searchQuery = searchQuery;
      if (searchQuery) {
        this.updateForm(searchQuery);
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
    const searchQuery = this.searchQueryFormService.getSearchQuery(this.editForm);
    if (searchQuery.id === null) {
      this.subscribeToSaveResponse(this.searchQueryService.create(searchQuery));
    } else {
      this.subscribeToSaveResponse(this.searchQueryService.update(searchQuery));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISearchQuery>>): void {
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

  protected updateForm(searchQuery: ISearchQuery): void {
    this.searchQuery = searchQuery;
    this.searchQueryFormService.resetForm(this.editForm, searchQuery);
  }
}
