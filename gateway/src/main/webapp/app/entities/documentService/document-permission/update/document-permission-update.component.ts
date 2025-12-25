import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPermissionGroup } from 'app/entities/documentService/permission-group/permission-group.model';
import { PermissionGroupService } from 'app/entities/documentService/permission-group/service/permission-group.service';
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

  permissionGroupsSharedCollection: IPermissionGroup[] = [];

  protected documentPermissionService = inject(DocumentPermissionService);
  protected documentPermissionFormService = inject(DocumentPermissionFormService);
  protected permissionGroupService = inject(PermissionGroupService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentPermissionFormGroup = this.documentPermissionFormService.createDocumentPermissionFormGroup();

  comparePermissionGroup = (o1: IPermissionGroup | null, o2: IPermissionGroup | null): boolean =>
    this.permissionGroupService.comparePermissionGroup(o1, o2);

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

    this.permissionGroupsSharedCollection = this.permissionGroupService.addPermissionGroupToCollectionIfMissing<IPermissionGroup>(
      this.permissionGroupsSharedCollection,
      documentPermission.permissionGroup,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.permissionGroupService
      .query()
      .pipe(map((res: HttpResponse<IPermissionGroup[]>) => res.body ?? []))
      .pipe(
        map((permissionGroups: IPermissionGroup[]) =>
          this.permissionGroupService.addPermissionGroupToCollectionIfMissing<IPermissionGroup>(
            permissionGroups,
            this.documentPermission?.permissionGroup,
          ),
        ),
      )
      .subscribe((permissionGroups: IPermissionGroup[]) => (this.permissionGroupsSharedCollection = permissionGroups));
  }
}
