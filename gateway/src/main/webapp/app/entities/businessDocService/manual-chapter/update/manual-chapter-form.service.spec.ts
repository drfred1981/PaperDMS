import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../manual-chapter.test-samples';

import { ManualChapterFormService } from './manual-chapter-form.service';

describe('ManualChapter Form Service', () => {
  let service: ManualChapterFormService;

  beforeEach(() => {
    service = TestBed.inject(ManualChapterFormService);
  });

  describe('Service methods', () => {
    describe('createManualChapterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createManualChapterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            manualId: expect.any(Object),
            chapterNumber: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            pageStart: expect.any(Object),
            pageEnd: expect.any(Object),
            level: expect.any(Object),
            displayOrder: expect.any(Object),
            manual: expect.any(Object),
            parentChapter: expect.any(Object),
          }),
        );
      });

      it('passing IManualChapter should create a new form with FormGroup', () => {
        const formGroup = service.createManualChapterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            manualId: expect.any(Object),
            chapterNumber: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            pageStart: expect.any(Object),
            pageEnd: expect.any(Object),
            level: expect.any(Object),
            displayOrder: expect.any(Object),
            manual: expect.any(Object),
            parentChapter: expect.any(Object),
          }),
        );
      });
    });

    describe('getManualChapter', () => {
      it('should return NewManualChapter for default ManualChapter initial value', () => {
        const formGroup = service.createManualChapterFormGroup(sampleWithNewData);

        const manualChapter = service.getManualChapter(formGroup);

        expect(manualChapter).toMatchObject(sampleWithNewData);
      });

      it('should return NewManualChapter for empty ManualChapter initial value', () => {
        const formGroup = service.createManualChapterFormGroup();

        const manualChapter = service.getManualChapter(formGroup);

        expect(manualChapter).toMatchObject({});
      });

      it('should return IManualChapter', () => {
        const formGroup = service.createManualChapterFormGroup(sampleWithRequiredData);

        const manualChapter = service.getManualChapter(formGroup);

        expect(manualChapter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IManualChapter should not enable id FormControl', () => {
        const formGroup = service.createManualChapterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewManualChapter should disable id FormControl', () => {
        const formGroup = service.createManualChapterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
