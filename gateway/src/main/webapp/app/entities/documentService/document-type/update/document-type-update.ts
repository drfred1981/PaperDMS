import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { IDocumentType } from '../document-type.model';
import { DocumentTypeService } from '../service/document-type.service';

import { DocumentTypeFormGroup, DocumentTypeFormService } from './document-type-form.service';

@Component({
  selector: 'jhi-document-type-update',
  templateUrl: './document-type-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentTypeUpdate implements OnInit {
  isSaving = false;
  documentType: IDocumentType | null = null;

  protected documentTypeService = inject(DocumentTypeService);
  protected documentTypeFormService = inject(DocumentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentTypeFormGroup = this.documentTypeFormService.createDocumentTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentType }) => {
      this.documentType = documentType;
      if (documentType) {
        this.updateForm(documentType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentType = this.documentTypeFormService.getDocumentType(this.editForm);
    if (documentType.id === null) {
      this.subscribeToSaveResponse(this.documentTypeService.create(documentType));
    } else {
      this.subscribeToSaveResponse(this.documentTypeService.update(documentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentType>>): void {
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

  protected updateForm(documentType: IDocumentType): void {
    this.documentType = documentType;
    this.documentTypeFormService.resetForm(this.editForm, documentType);
  }
}
