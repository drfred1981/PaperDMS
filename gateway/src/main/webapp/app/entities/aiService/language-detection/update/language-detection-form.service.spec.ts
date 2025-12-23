import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../language-detection.test-samples';

import { LanguageDetectionFormService } from './language-detection-form.service';

describe('LanguageDetection Form Service', () => {
  let service: LanguageDetectionFormService;

  beforeEach(() => {
    service = TestBed.inject(LanguageDetectionFormService);
  });

  describe('Service methods', () => {
    describe('createLanguageDetectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLanguageDetectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            confidence: expect.any(Object),
            detectionMethod: expect.any(Object),
            alternativeLanguages: expect.any(Object),
            textSample: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            detectedDate: expect.any(Object),
            modelVersion: expect.any(Object),
          }),
        );
      });

      it('passing ILanguageDetection should create a new form with FormGroup', () => {
        const formGroup = service.createLanguageDetectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            detectedLanguage: expect.any(Object),
            confidence: expect.any(Object),
            detectionMethod: expect.any(Object),
            alternativeLanguages: expect.any(Object),
            textSample: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            detectedDate: expect.any(Object),
            modelVersion: expect.any(Object),
          }),
        );
      });
    });

    describe('getLanguageDetection', () => {
      it('should return NewLanguageDetection for default LanguageDetection initial value', () => {
        const formGroup = service.createLanguageDetectionFormGroup(sampleWithNewData);

        const languageDetection = service.getLanguageDetection(formGroup);

        expect(languageDetection).toMatchObject(sampleWithNewData);
      });

      it('should return NewLanguageDetection for empty LanguageDetection initial value', () => {
        const formGroup = service.createLanguageDetectionFormGroup();

        const languageDetection = service.getLanguageDetection(formGroup);

        expect(languageDetection).toMatchObject({});
      });

      it('should return ILanguageDetection', () => {
        const formGroup = service.createLanguageDetectionFormGroup(sampleWithRequiredData);

        const languageDetection = service.getLanguageDetection(formGroup);

        expect(languageDetection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILanguageDetection should not enable id FormControl', () => {
        const formGroup = service.createLanguageDetectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLanguageDetection should disable id FormControl', () => {
        const formGroup = service.createLanguageDetectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
