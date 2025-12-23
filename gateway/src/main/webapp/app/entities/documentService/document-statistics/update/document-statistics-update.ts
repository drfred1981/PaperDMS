import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsService } from '../service/document-statistics.service';

import { DocumentStatisticsFormGroup, DocumentStatisticsFormService } from './document-statistics-form.service';

@Component({
  selector: 'jhi-document-statistics-update',
  templateUrl: './document-statistics-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentStatisticsUpdate implements OnInit {
  isSaving = false;
  documentStatistics: IDocumentStatistics | null = null;

  protected documentStatisticsService = inject(DocumentStatisticsService);
  protected documentStatisticsFormService = inject(DocumentStatisticsFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentStatisticsFormGroup = this.documentStatisticsFormService.createDocumentStatisticsFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentStatistics }) => {
      this.documentStatistics = documentStatistics;
      if (documentStatistics) {
        this.updateForm(documentStatistics);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentStatistics = this.documentStatisticsFormService.getDocumentStatistics(this.editForm);
    if (documentStatistics.id === null) {
      this.subscribeToSaveResponse(this.documentStatisticsService.create(documentStatistics));
    } else {
      this.subscribeToSaveResponse(this.documentStatisticsService.update(documentStatistics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentStatistics>>): void {
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

  protected updateForm(documentStatistics: IDocumentStatistics): void {
    this.documentStatistics = documentStatistics;
    this.documentStatisticsFormService.resetForm(this.editForm, documentStatistics);
  }
}
