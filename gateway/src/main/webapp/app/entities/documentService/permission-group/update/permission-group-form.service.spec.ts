import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../permission-group.test-samples';

import { PermissionGroupFormService } from './permission-group-form.service';

describe('PermissionGroup Form Service', () => {
  let service: PermissionGroupFormService;

  beforeEach(() => {
    service = TestBed.inject(PermissionGroupFormService);
  });

  describe('Service methods', () => {
    describe('createPermissionGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPermissionGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            permissions: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });

      it('passing IPermissionGroup should create a new form with FormGroup', () => {
        const formGroup = service.createPermissionGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            permissions: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getPermissionGroup', () => {
      it('should return NewPermissionGroup for default PermissionGroup initial value', () => {
        const formGroup = service.createPermissionGroupFormGroup(sampleWithNewData);

        const permissionGroup = service.getPermissionGroup(formGroup);

        expect(permissionGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewPermissionGroup for empty PermissionGroup initial value', () => {
        const formGroup = service.createPermissionGroupFormGroup();

        const permissionGroup = service.getPermissionGroup(formGroup);

        expect(permissionGroup).toMatchObject({});
      });

      it('should return IPermissionGroup', () => {
        const formGroup = service.createPermissionGroupFormGroup(sampleWithRequiredData);

        const permissionGroup = service.getPermissionGroup(formGroup);

        expect(permissionGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPermissionGroup should not enable id FormControl', () => {
        const formGroup = service.createPermissionGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPermissionGroup should disable id FormControl', () => {
        const formGroup = service.createPermissionGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
