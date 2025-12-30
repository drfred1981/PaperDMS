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
import { SearchIndexService } from '../service/search-index.service';
import { ISearchIndex } from '../search-index.model';
import { SearchIndexFormGroup, SearchIndexFormService } from './search-index-form.service';

@Component({
  selector: 'jhi-search-index-update',
  templateUrl: './search-index-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SearchIndexUpdateComponent implements OnInit {
  isSaving = false;
  searchIndex: ISearchIndex | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected searchIndexService = inject(SearchIndexService);
  protected searchIndexFormService = inject(SearchIndexFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SearchIndexFormGroup = this.searchIndexFormService.createSearchIndexFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ searchIndex }) => {
      this.searchIndex = searchIndex;
      if (searchIndex) {
        this.updateForm(searchIndex);
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
    const searchIndex = this.searchIndexFormService.getSearchIndex(this.editForm);
    if (searchIndex.id !== null) {
      this.subscribeToSaveResponse(this.searchIndexService.update(searchIndex));
    } else {
      this.subscribeToSaveResponse(this.searchIndexService.create(searchIndex));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISearchIndex>>): void {
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

  protected updateForm(searchIndex: ISearchIndex): void {
    this.searchIndex = searchIndex;
    this.searchIndexFormService.resetForm(this.editForm, searchIndex);
  }
}
