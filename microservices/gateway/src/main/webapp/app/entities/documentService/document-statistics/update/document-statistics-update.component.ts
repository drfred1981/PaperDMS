import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsService } from '../service/document-statistics.service';
import { DocumentStatisticsFormGroup, DocumentStatisticsFormService } from './document-statistics-form.service';

@Component({
  selector: 'jhi-document-statistics-update',
  templateUrl: './document-statistics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentStatisticsUpdateComponent implements OnInit {
  isSaving = false;
  documentStatistics: IDocumentStatistics | null = null;

  documentsSharedCollection: IDocument[] = [];

  protected documentStatisticsService = inject(DocumentStatisticsService);
  protected documentStatisticsFormService = inject(DocumentStatisticsFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentStatisticsFormGroup = this.documentStatisticsFormService.createDocumentStatisticsFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentStatistics }) => {
      this.documentStatistics = documentStatistics;
      if (documentStatistics) {
        this.updateForm(documentStatistics);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentStatistics = this.documentStatisticsFormService.getDocumentStatistics(this.editForm);
    if (documentStatistics.id !== null) {
      this.subscribeToSaveResponse(this.documentStatisticsService.update(documentStatistics));
    } else {
      this.subscribeToSaveResponse(this.documentStatisticsService.create(documentStatistics));
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

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentStatistics.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentStatistics?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
