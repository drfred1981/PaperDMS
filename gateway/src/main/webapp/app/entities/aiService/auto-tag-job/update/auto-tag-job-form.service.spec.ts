import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../auto-tag-job.test-samples';

import { AutoTagJobFormService } from './auto-tag-job-form.service';

describe('AutoTagJob Form Service', () => {
  let service: AutoTagJobFormService;

  beforeEach(() => {
    service = TestBed.inject(AutoTagJobFormService);
  });

  describe('Service methods', () => {
    describe('createAutoTagJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAutoTagJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            status: expect.any(Object),
            modelVersion: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            confidence: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IAutoTagJob should create a new form with FormGroup', () => {
        const formGroup = service.createAutoTagJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            status: expect.any(Object),
            modelVersion: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            confidence: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAutoTagJob', () => {
      it('should return NewAutoTagJob for default AutoTagJob initial value', () => {
        const formGroup = service.createAutoTagJobFormGroup(sampleWithNewData);

        const autoTagJob = service.getAutoTagJob(formGroup);

        expect(autoTagJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewAutoTagJob for empty AutoTagJob initial value', () => {
        const formGroup = service.createAutoTagJobFormGroup();

        const autoTagJob = service.getAutoTagJob(formGroup);

        expect(autoTagJob).toMatchObject({});
      });

      it('should return IAutoTagJob', () => {
        const formGroup = service.createAutoTagJobFormGroup(sampleWithRequiredData);

        const autoTagJob = service.getAutoTagJob(formGroup);

        expect(autoTagJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAutoTagJob should not enable id FormControl', () => {
        const formGroup = service.createAutoTagJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAutoTagJob should disable id FormControl', () => {
        const formGroup = service.createAutoTagJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
