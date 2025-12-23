import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bookmark.test-samples';

import { BookmarkFormService } from './bookmark-form.service';

describe('Bookmark Form Service', () => {
  let service: BookmarkFormService;

  beforeEach(() => {
    service = TestBed.inject(BookmarkFormService);
  });

  describe('Service methods', () => {
    describe('createBookmarkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookmarkFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            entityType: expect.any(Object),
            entityId: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IBookmark should create a new form with FormGroup', () => {
        const formGroup = service.createBookmarkFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            entityType: expect.any(Object),
            entityId: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookmark', () => {
      it('should return NewBookmark for default Bookmark initial value', () => {
        const formGroup = service.createBookmarkFormGroup(sampleWithNewData);

        const bookmark = service.getBookmark(formGroup);

        expect(bookmark).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookmark for empty Bookmark initial value', () => {
        const formGroup = service.createBookmarkFormGroup();

        const bookmark = service.getBookmark(formGroup);

        expect(bookmark).toMatchObject({});
      });

      it('should return IBookmark', () => {
        const formGroup = service.createBookmarkFormGroup(sampleWithRequiredData);

        const bookmark = service.getBookmark(formGroup);

        expect(bookmark).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookmark should not enable id FormControl', () => {
        const formGroup = service.createBookmarkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookmark should disable id FormControl', () => {
        const formGroup = service.createBookmarkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
