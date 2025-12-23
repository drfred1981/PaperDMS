import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RelationType } from 'app/entities/enumerations/relation-type.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentRelation } from '../document-relation.model';
import { DocumentRelationService } from '../service/document-relation.service';

import { DocumentRelationFormGroup, DocumentRelationFormService } from './document-relation-form.service';

@Component({
  selector: 'jhi-document-relation-update',
  templateUrl: './document-relation-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentRelationUpdate implements OnInit {
  isSaving = false;
  documentRelation: IDocumentRelation | null = null;
  relationTypeValues = Object.keys(RelationType);

  protected documentRelationService = inject(DocumentRelationService);
  protected documentRelationFormService = inject(DocumentRelationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentRelationFormGroup = this.documentRelationFormService.createDocumentRelationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentRelation }) => {
      this.documentRelation = documentRelation;
      if (documentRelation) {
        this.updateForm(documentRelation);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentRelation = this.documentRelationFormService.getDocumentRelation(this.editForm);
    if (documentRelation.id === null) {
      this.subscribeToSaveResponse(this.documentRelationService.create(documentRelation));
    } else {
      this.subscribeToSaveResponse(this.documentRelationService.update(documentRelation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentRelation>>): void {
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

  protected updateForm(documentRelation: IDocumentRelation): void {
    this.documentRelation = documentRelation;
    this.documentRelationFormService.resetForm(this.editForm, documentRelation);
  }
}
