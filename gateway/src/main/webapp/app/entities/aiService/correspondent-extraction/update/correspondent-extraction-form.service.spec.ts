import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../correspondent-extraction.test-samples';

import { CorrespondentExtractionFormService } from './correspondent-extraction-form.service';

describe('CorrespondentExtraction Form Service', () => {
  let service: CorrespondentExtractionFormService;

  beforeEach(() => {
    service = TestBed.inject(CorrespondentExtractionFormService);
  });

  describe('Service methods', () => {
    describe('createCorrespondentExtractionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            status: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            resultS3Key: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            sendersCount: expect.any(Object),
            recipientsCount: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ICorrespondentExtraction should create a new form with FormGroup', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            extractedText: expect.any(Object),
            extractedTextSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            status: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            resultS3Key: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            sendersCount: expect.any(Object),
            recipientsCount: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCorrespondentExtraction', () => {
      it('should return NewCorrespondentExtraction for default CorrespondentExtraction initial value', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup(sampleWithNewData);

        const correspondentExtraction = service.getCorrespondentExtraction(formGroup);

        expect(correspondentExtraction).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorrespondentExtraction for empty CorrespondentExtraction initial value', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup();

        const correspondentExtraction = service.getCorrespondentExtraction(formGroup);

        expect(correspondentExtraction).toMatchObject({});
      });

      it('should return ICorrespondentExtraction', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup(sampleWithRequiredData);

        const correspondentExtraction = service.getCorrespondentExtraction(formGroup);

        expect(correspondentExtraction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorrespondentExtraction should not enable id FormControl', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorrespondentExtraction should disable id FormControl', () => {
        const formGroup = service.createCorrespondentExtractionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
