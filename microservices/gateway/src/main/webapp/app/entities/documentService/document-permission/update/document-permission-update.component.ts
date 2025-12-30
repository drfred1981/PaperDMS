import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IMetaPermissionGroup } from 'app/entities/documentService/meta-permission-group/meta-permission-group.model';
import { MetaPermissionGroupService } from 'app/entities/documentService/meta-permission-group/service/meta-permission-group.service';
import { PrincipalType } from 'app/entities/enumerations/principal-type.model';
import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DocumentPermissionService } from '../service/document-permission.service';
import { IDocumentPermission } from '../document-permission.model';
import { DocumentPermissionFormGroup, DocumentPermissionFormService } from './document-permission-form.service';

@Component({
  selector: 'jhi-document-permission-update',
  templateUrl: './document-permission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentPermissionUpdateComponent implements OnInit {
  isSaving = false;
  documentPermission: IDocumentPermission | null = null;
  principalTypeValues = Object.keys(PrincipalType);
  permissionTypeValues = Object.keys(PermissionType);

  documentsSharedCollection: IDocument[] = [];
  metaPermissionGroupsSharedCollection: IMetaPermissionGroup[] = [];

  protected documentPermissionService = inject(DocumentPermissionService);
  protected documentPermissionFormService = inject(DocumentPermissionFormService);
  protected documentService = inject(DocumentService);
  protected metaPermissionGroupService = inject(MetaPermissionGroupService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentPermissionFormGroup = this.documentPermissionFormService.createDocumentPermissionFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  compareMetaPermissionGroup = (o1: IMetaPermissionGroup | null, o2: IMetaPermissionGroup | null): boolean =>
    this.metaPermissionGroupService.compareMetaPermissionGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentPermission }) => {
      this.documentPermission = documentPermission;
      if (documentPermission) {
        this.updateForm(documentPermission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentPermission = this.documentPermissionFormService.getDocumentPermission(this.editForm);
    if (documentPermission.id !== null) {
      this.subscribeToSaveResponse(this.documentPermissionService.update(documentPermission));
    } else {
      this.subscribeToSaveResponse(this.documentPermissionService.create(documentPermission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentPermission>>): void {
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

  protected updateForm(documentPermission: IDocumentPermission): void {
    this.documentPermission = documentPermission;
    this.documentPermissionFormService.resetForm(this.editForm, documentPermission);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentPermission.document,
    );
    this.metaPermissionGroupsSharedCollection =
      this.metaPermissionGroupService.addMetaPermissionGroupToCollectionIfMissing<IMetaPermissionGroup>(
        this.metaPermissionGroupsSharedCollection,
        documentPermission.metaPermissionGroup,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentPermission?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));

    this.metaPermissionGroupService
      .query()
      .pipe(map((res: HttpResponse<IMetaPermissionGroup[]>) => res.body ?? []))
      .pipe(
        map((metaPermissionGroups: IMetaPermissionGroup[]) =>
          this.metaPermissionGroupService.addMetaPermissionGroupToCollectionIfMissing<IMetaPermissionGroup>(
            metaPermissionGroups,
            this.documentPermission?.metaPermissionGroup,
          ),
        ),
      )
      .subscribe((metaPermissionGroups: IMetaPermissionGroup[]) => (this.metaPermissionGroupsSharedCollection = metaPermissionGroups));
  }
}
