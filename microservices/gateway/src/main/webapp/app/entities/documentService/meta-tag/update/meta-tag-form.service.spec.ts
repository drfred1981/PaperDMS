import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-tag.test-samples';

import { MetaTagFormService } from './meta-tag-form.service';

describe('MetaTag Form Service', () => {
  let service: MetaTagFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaTagFormService);
  });

  describe('Service methods', () => {
    describe('createMetaTagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaTagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
            description: expect.any(Object),
            usageCount: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            metaMetaTagCategory: expect.any(Object),
          }),
        );
      });

      it('passing IMetaTag should create a new form with FormGroup', () => {
        const formGroup = service.createMetaTagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            color: expect.any(Object),
            description: expect.any(Object),
            usageCount: expect.any(Object),
            isSystem: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            metaMetaTagCategory: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetaTag', () => {
      it('should return NewMetaTag for default MetaTag initial value', () => {
        const formGroup = service.createMetaTagFormGroup(sampleWithNewData);

        const metaTag = service.getMetaTag(formGroup) as any;

        expect(metaTag).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaTag for empty MetaTag initial value', () => {
        const formGroup = service.createMetaTagFormGroup();

        const metaTag = service.getMetaTag(formGroup) as any;

        expect(metaTag).toMatchObject({});
      });

      it('should return IMetaTag', () => {
        const formGroup = service.createMetaTagFormGroup(sampleWithRequiredData);

        const metaTag = service.getMetaTag(formGroup) as any;

        expect(metaTag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaTag should not enable id FormControl', () => {
        const formGroup = service.createMetaTagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaTag should disable id FormControl', () => {
        const formGroup = service.createMetaTagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
