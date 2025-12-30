import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-meta-tag-category.test-samples';

import { MetaMetaTagCategoryFormService } from './meta-meta-tag-category-form.service';

describe('MetaMetaTagCategory Form Service', () => {
  let service: MetaMetaTagCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaMetaTagCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createMetaMetaTagCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
            displayOrder: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IMetaMetaTagCategory should create a new form with FormGroup', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
            displayOrder: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetaMetaTagCategory', () => {
      it('should return NewMetaMetaTagCategory for default MetaMetaTagCategory initial value', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup(sampleWithNewData);

        const metaMetaTagCategory = service.getMetaMetaTagCategory(formGroup) as any;

        expect(metaMetaTagCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaMetaTagCategory for empty MetaMetaTagCategory initial value', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup();

        const metaMetaTagCategory = service.getMetaMetaTagCategory(formGroup) as any;

        expect(metaMetaTagCategory).toMatchObject({});
      });

      it('should return IMetaMetaTagCategory', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup(sampleWithRequiredData);

        const metaMetaTagCategory = service.getMetaMetaTagCategory(formGroup) as any;

        expect(metaMetaTagCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaMetaTagCategory should not enable id FormControl', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaMetaTagCategory should disable id FormControl', () => {
        const formGroup = service.createMetaMetaTagCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
