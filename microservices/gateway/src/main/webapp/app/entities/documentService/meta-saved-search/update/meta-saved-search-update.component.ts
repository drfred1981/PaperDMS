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
import { AlertFrequency } from 'app/entities/enumerations/alert-frequency.model';
import { MetaSavedSearchService } from '../service/meta-saved-search.service';
import { IMetaSavedSearch } from '../meta-saved-search.model';
import { MetaSavedSearchFormGroup, MetaSavedSearchFormService } from './meta-saved-search-form.service';

@Component({
  selector: 'jhi-meta-saved-search-update',
  templateUrl: './meta-saved-search-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaSavedSearchUpdateComponent implements OnInit {
  isSaving = false;
  metaSavedSearch: IMetaSavedSearch | null = null;
  alertFrequencyValues = Object.keys(AlertFrequency);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected metaSavedSearchService = inject(MetaSavedSearchService);
  protected metaSavedSearchFormService = inject(MetaSavedSearchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaSavedSearchFormGroup = this.metaSavedSearchFormService.createMetaSavedSearchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaSavedSearch }) => {
      this.metaSavedSearch = metaSavedSearch;
      if (metaSavedSearch) {
        this.updateForm(metaSavedSearch);
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
    const metaSavedSearch = this.metaSavedSearchFormService.getMetaSavedSearch(this.editForm);
    if (metaSavedSearch.id !== null) {
      this.subscribeToSaveResponse(this.metaSavedSearchService.update(metaSavedSearch));
    } else {
      this.subscribeToSaveResponse(this.metaSavedSearchService.create(metaSavedSearch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaSavedSearch>>): void {
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

  protected updateForm(metaSavedSearch: IMetaSavedSearch): void {
    this.metaSavedSearch = metaSavedSearch;
    this.metaSavedSearchFormService.resetForm(this.editForm, metaSavedSearch);
  }
}
