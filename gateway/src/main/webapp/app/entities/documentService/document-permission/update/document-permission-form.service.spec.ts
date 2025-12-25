import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-permission.test-samples';

import { DocumentPermissionFormService } from './document-permission-form.service';

describe('DocumentPermission Form Service', () => {
  let service: DocumentPermissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentPermissionFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentPermissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentPermissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            principalType: expect.any(Object),
            principalId: expect.any(Object),
            permission: expect.any(Object),
            canDelegate: expect.any(Object),
            grantedBy: expect.any(Object),
            grantedDate: expect.any(Object),
            permissionGroup: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentPermission should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentPermissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            principalType: expect.any(Object),
            principalId: expect.any(Object),
            permission: expect.any(Object),
            canDelegate: expect.any(Object),
            grantedBy: expect.any(Object),
            grantedDate: expect.any(Object),
            permissionGroup: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentPermission', () => {
      it('should return NewDocumentPermission for default DocumentPermission initial value', () => {
        const formGroup = service.createDocumentPermissionFormGroup(sampleWithNewData);

        const documentPermission = service.getDocumentPermission(formGroup) as any;

        expect(documentPermission).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentPermission for empty DocumentPermission initial value', () => {
        const formGroup = service.createDocumentPermissionFormGroup();

        const documentPermission = service.getDocumentPermission(formGroup) as any;

        expect(documentPermission).toMatchObject({});
      });

      it('should return IDocumentPermission', () => {
        const formGroup = service.createDocumentPermissionFormGroup(sampleWithRequiredData);

        const documentPermission = service.getDocumentPermission(formGroup) as any;

        expect(documentPermission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentPermission should not enable id FormControl', () => {
        const formGroup = service.createDocumentPermissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentPermission should disable id FormControl', () => {
        const formGroup = service.createDocumentPermissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
