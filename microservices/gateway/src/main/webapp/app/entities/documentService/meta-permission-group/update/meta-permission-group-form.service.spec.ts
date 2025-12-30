import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-permission-group.test-samples';

import { MetaPermissionGroupFormService } from './meta-permission-group-form.service';

describe('MetaPermissionGroup Form Service', () => {
  let service: MetaPermissionGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaPermissionGroupFormService);
  });

  describe('Service methods', () => {
    describe('createMetaPermissionGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup();

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

      it('passing IMetaPermissionGroup should create a new form with FormGroup', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup(sampleWithRequiredData);

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

    describe('getMetaPermissionGroup', () => {
      it('should return NewMetaPermissionGroup for default MetaPermissionGroup initial value', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup(sampleWithNewData);

        const metaPermissionGroup = service.getMetaPermissionGroup(formGroup) as any;

        expect(metaPermissionGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaPermissionGroup for empty MetaPermissionGroup initial value', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup();

        const metaPermissionGroup = service.getMetaPermissionGroup(formGroup) as any;

        expect(metaPermissionGroup).toMatchObject({});
      });

      it('should return IMetaPermissionGroup', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup(sampleWithRequiredData);

        const metaPermissionGroup = service.getMetaPermissionGroup(formGroup) as any;

        expect(metaPermissionGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaPermissionGroup should not enable id FormControl', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaPermissionGroup should disable id FormControl', () => {
        const formGroup = service.createMetaPermissionGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
