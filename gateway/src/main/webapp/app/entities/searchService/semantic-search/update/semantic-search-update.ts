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
import { ISemanticSearch } from '../semantic-search.model';
import { SemanticSearchService } from '../service/semantic-search.service';

import { SemanticSearchFormGroup, SemanticSearchFormService } from './semantic-search-form.service';

@Component({
  selector: 'jhi-semantic-search-update',
  templateUrl: './semantic-search-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SemanticSearchUpdate implements OnInit {
  isSaving = false;
  semanticSearch: ISemanticSearch | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected semanticSearchService = inject(SemanticSearchService);
  protected semanticSearchFormService = inject(SemanticSearchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SemanticSearchFormGroup = this.semanticSearchFormService.createSemanticSearchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semanticSearch }) => {
      this.semanticSearch = semanticSearch;
      if (semanticSearch) {
        this.updateForm(semanticSearch);
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
    const semanticSearch = this.semanticSearchFormService.getSemanticSearch(this.editForm);
    if (semanticSearch.id === null) {
      this.subscribeToSaveResponse(this.semanticSearchService.create(semanticSearch));
    } else {
      this.subscribeToSaveResponse(this.semanticSearchService.update(semanticSearch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemanticSearch>>): void {
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

  protected updateForm(semanticSearch: ISemanticSearch): void {
    this.semanticSearch = semanticSearch;
    this.semanticSearchFormService.resetForm(this.editForm, semanticSearch);
  }
}
