import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-auto-tag-job.test-samples';

import { AIAutoTagJobFormService } from './ai-auto-tag-job-form.service';

describe('AIAutoTagJob Form Service', () => {
  let service: AIAutoTagJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AIAutoTagJobFormService);
  });

  describe('Service methods', () => {
    describe('createAIAutoTagJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAIAutoTagJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            status: expect.any(Object),
            modelVersion: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdDate: expect.any(Object),
            aITypePrediction: expect.any(Object),
            languagePrediction: expect.any(Object),
          }),
        );
      });

      it('passing IAIAutoTagJob should create a new form with FormGroup', () => {
        const formGroup = service.createAIAutoTagJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            status: expect.any(Object),
            modelVersion: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdDate: expect.any(Object),
            aITypePrediction: expect.any(Object),
            languagePrediction: expect.any(Object),
          }),
        );
      });
    });

    describe('getAIAutoTagJob', () => {
      it('should return NewAIAutoTagJob for default AIAutoTagJob initial value', () => {
        const formGroup = service.createAIAutoTagJobFormGroup(sampleWithNewData);

        const aIAutoTagJob = service.getAIAutoTagJob(formGroup) as any;

        expect(aIAutoTagJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewAIAutoTagJob for empty AIAutoTagJob initial value', () => {
        const formGroup = service.createAIAutoTagJobFormGroup();

        const aIAutoTagJob = service.getAIAutoTagJob(formGroup) as any;

        expect(aIAutoTagJob).toMatchObject({});
      });

      it('should return IAIAutoTagJob', () => {
        const formGroup = service.createAIAutoTagJobFormGroup(sampleWithRequiredData);

        const aIAutoTagJob = service.getAIAutoTagJob(formGroup) as any;

        expect(aIAutoTagJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAIAutoTagJob should not enable id FormControl', () => {
        const formGroup = service.createAIAutoTagJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAIAutoTagJob should disable id FormControl', () => {
        const formGroup = service.createAIAutoTagJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
