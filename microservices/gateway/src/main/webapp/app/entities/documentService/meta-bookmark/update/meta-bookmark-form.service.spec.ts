import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-bookmark.test-samples';

import { MetaBookmarkFormService } from './meta-bookmark-form.service';

describe('MetaBookmark Form Service', () => {
  let service: MetaBookmarkFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaBookmarkFormService);
  });

  describe('Service methods', () => {
    describe('createMetaBookmarkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaBookmarkFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            entityType: expect.any(Object),
            entityName: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IMetaBookmark should create a new form with FormGroup', () => {
        const formGroup = service.createMetaBookmarkFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            entityType: expect.any(Object),
            entityName: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetaBookmark', () => {
      it('should return NewMetaBookmark for default MetaBookmark initial value', () => {
        const formGroup = service.createMetaBookmarkFormGroup(sampleWithNewData);

        const metaBookmark = service.getMetaBookmark(formGroup) as any;

        expect(metaBookmark).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaBookmark for empty MetaBookmark initial value', () => {
        const formGroup = service.createMetaBookmarkFormGroup();

        const metaBookmark = service.getMetaBookmark(formGroup) as any;

        expect(metaBookmark).toMatchObject({});
      });

      it('should return IMetaBookmark', () => {
        const formGroup = service.createMetaBookmarkFormGroup(sampleWithRequiredData);

        const metaBookmark = service.getMetaBookmark(formGroup) as any;

        expect(metaBookmark).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaBookmark should not enable id FormControl', () => {
        const formGroup = service.createMetaBookmarkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaBookmark should disable id FormControl', () => {
        const formGroup = service.createMetaBookmarkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
