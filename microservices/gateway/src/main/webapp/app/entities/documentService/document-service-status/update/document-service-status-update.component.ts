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
import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { ServiceType } from 'app/entities/enumerations/service-type.model';
import { ServiceStatus } from 'app/entities/enumerations/service-status.model';
import { DocumentServiceStatusService } from '../service/document-service-status.service';
import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusFormGroup, DocumentServiceStatusFormService } from './document-service-status-form.service';

@Component({
  selector: 'jhi-document-service-status-update',
  templateUrl: './document-service-status-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentServiceStatusUpdateComponent implements OnInit {
  isSaving = false;
  documentServiceStatus: IDocumentServiceStatus | null = null;
  serviceTypeValues = Object.keys(ServiceType);
  serviceStatusValues = Object.keys(ServiceStatus);

  documentsSharedCollection: IDocument[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentServiceStatusService = inject(DocumentServiceStatusService);
  protected documentServiceStatusFormService = inject(DocumentServiceStatusFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentServiceStatusFormGroup = this.documentServiceStatusFormService.createDocumentServiceStatusFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentServiceStatus }) => {
      this.documentServiceStatus = documentServiceStatus;
      if (documentServiceStatus) {
        this.updateForm(documentServiceStatus);
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
    const documentServiceStatus = this.documentServiceStatusFormService.getDocumentServiceStatus(this.editForm);
    if (documentServiceStatus.id !== null) {
      this.subscribeToSaveResponse(this.documentServiceStatusService.update(documentServiceStatus));
    } else {
      this.subscribeToSaveResponse(this.documentServiceStatusService.create(documentServiceStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentServiceStatus>>): void {
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

  protected updateForm(documentServiceStatus: IDocumentServiceStatus): void {
    this.documentServiceStatus = documentServiceStatus;
    this.documentServiceStatusFormService.resetForm(this.editForm, documentServiceStatus);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentServiceStatus.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentServiceStatus?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
