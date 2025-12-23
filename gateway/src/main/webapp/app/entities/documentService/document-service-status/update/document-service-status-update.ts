import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ServiceStatus } from 'app/entities/enumerations/service-status.model';
import { ServiceType } from 'app/entities/enumerations/service-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusService } from '../service/document-service-status.service';

import { DocumentServiceStatusFormGroup, DocumentServiceStatusFormService } from './document-service-status-form.service';

@Component({
  selector: 'jhi-document-service-status-update',
  templateUrl: './document-service-status-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentServiceStatusUpdate implements OnInit {
  isSaving = false;
  documentServiceStatus: IDocumentServiceStatus | null = null;
  serviceTypeValues = Object.keys(ServiceType);
  serviceStatusValues = Object.keys(ServiceStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentServiceStatusService = inject(DocumentServiceStatusService);
  protected documentServiceStatusFormService = inject(DocumentServiceStatusFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentServiceStatusFormGroup = this.documentServiceStatusFormService.createDocumentServiceStatusFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentServiceStatus }) => {
      this.documentServiceStatus = documentServiceStatus;
      if (documentServiceStatus) {
        this.updateForm(documentServiceStatus);
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
    const documentServiceStatus = this.documentServiceStatusFormService.getDocumentServiceStatus(this.editForm);
    if (documentServiceStatus.id === null) {
      this.subscribeToSaveResponse(this.documentServiceStatusService.create(documentServiceStatus));
    } else {
      this.subscribeToSaveResponse(this.documentServiceStatusService.update(documentServiceStatus));
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
  }
}
