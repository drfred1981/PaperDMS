import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISearchQuery } from 'app/entities/searchService/search-query/search-query.model';
import { SearchQueryService } from 'app/entities/searchService/search-query/service/search-query.service';
import { FacetType } from 'app/entities/enumerations/facet-type.model';
import { SearchFacetService } from '../service/search-facet.service';
import { ISearchFacet } from '../search-facet.model';
import { SearchFacetFormGroup, SearchFacetFormService } from './search-facet-form.service';

@Component({
  selector: 'jhi-search-facet-update',
  templateUrl: './search-facet-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SearchFacetUpdateComponent implements OnInit {
  isSaving = false;
  searchFacet: ISearchFacet | null = null;
  facetTypeValues = Object.keys(FacetType);

  searchQueriesSharedCollection: ISearchQuery[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected searchFacetService = inject(SearchFacetService);
  protected searchFacetFormService = inject(SearchFacetFormService);
  protected searchQueryService = inject(SearchQueryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SearchFacetFormGroup = this.searchFacetFormService.createSearchFacetFormGroup();

  compareSearchQuery = (o1: ISearchQuery | null, o2: ISearchQuery | null): boolean => this.searchQueryService.compareSearchQuery(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ searchFacet }) => {
      this.searchFacet = searchFacet;
      if (searchFacet) {
        this.updateForm(searchFacet);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const searchFacet = this.searchFacetFormService.getSearchFacet(this.editForm);
    if (searchFacet.id !== null) {
      this.subscribeToSaveResponse(this.searchFacetService.update(searchFacet));
    } else {
      this.subscribeToSaveResponse(this.searchFacetService.create(searchFacet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISearchFacet>>): void {
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

  protected updateForm(searchFacet: ISearchFacet): void {
    this.searchFacet = searchFacet;
    this.searchFacetFormService.resetForm(this.editForm, searchFacet);

    this.searchQueriesSharedCollection = this.searchQueryService.addSearchQueryToCollectionIfMissing<ISearchQuery>(
      this.searchQueriesSharedCollection,
      searchFacet.searchQuery,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.searchQueryService
      .query()
      .pipe(map((res: HttpResponse<ISearchQuery[]>) => res.body ?? []))
      .pipe(
        map((searchQueries: ISearchQuery[]) =>
          this.searchQueryService.addSearchQueryToCollectionIfMissing<ISearchQuery>(searchQueries, this.searchFacet?.searchQuery),
        ),
      )
      .subscribe((searchQueries: ISearchQuery[]) => (this.searchQueriesSharedCollection = searchQueries));
  }
}
