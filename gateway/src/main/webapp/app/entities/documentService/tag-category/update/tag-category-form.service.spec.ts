import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tag-category.test-samples';

import { TagCategoryFormService } from './tag-category-form.service';

describe('TagCategory Form Service', () => {
  let service: TagCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TagCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createTagCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTagCategoryFormGroup();

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

      it('passing ITagCategory should create a new form with FormGroup', () => {
        const formGroup = service.createTagCategoryFormGroup(sampleWithRequiredData);

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

    describe('getTagCategory', () => {
      it('should return NewTagCategory for default TagCategory initial value', () => {
        const formGroup = service.createTagCategoryFormGroup(sampleWithNewData);

        const tagCategory = service.getTagCategory(formGroup) as any;

        expect(tagCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewTagCategory for empty TagCategory initial value', () => {
        const formGroup = service.createTagCategoryFormGroup();

        const tagCategory = service.getTagCategory(formGroup) as any;

        expect(tagCategory).toMatchObject({});
      });

      it('should return ITagCategory', () => {
        const formGroup = service.createTagCategoryFormGroup(sampleWithRequiredData);

        const tagCategory = service.getTagCategory(formGroup) as any;

        expect(tagCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITagCategory should not enable id FormControl', () => {
        const formGroup = service.createTagCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTagCategory should disable id FormControl', () => {
        const formGroup = service.createTagCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
