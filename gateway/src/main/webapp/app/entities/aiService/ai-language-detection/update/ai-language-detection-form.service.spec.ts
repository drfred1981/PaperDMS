import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-language-detection.test-samples';

import { AILanguageDetectionFormService } from './ai-language-detection-form.service';

describe('AILanguageDetection Form Service', () => {
  let service: AILanguageDetectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AILanguageDetectionFormService);
  });

  describe('Service methods', () => {
    describe('createAILanguageDetectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAILanguageDetectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
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

      it('passing IAILanguageDetection should create a new form with FormGroup', () => {
        const formGroup = service.createAILanguageDetectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
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

    describe('getAILanguageDetection', () => {
      it('should return NewAILanguageDetection for default AILanguageDetection initial value', () => {
        const formGroup = service.createAILanguageDetectionFormGroup(sampleWithNewData);

        const aILanguageDetection = service.getAILanguageDetection(formGroup) as any;

        expect(aILanguageDetection).toMatchObject(sampleWithNewData);
      });

      it('should return NewAILanguageDetection for empty AILanguageDetection initial value', () => {
        const formGroup = service.createAILanguageDetectionFormGroup();

        const aILanguageDetection = service.getAILanguageDetection(formGroup) as any;

        expect(aILanguageDetection).toMatchObject({});
      });

      it('should return IAILanguageDetection', () => {
        const formGroup = service.createAILanguageDetectionFormGroup(sampleWithRequiredData);

        const aILanguageDetection = service.getAILanguageDetection(formGroup) as any;

        expect(aILanguageDetection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAILanguageDetection should not enable id FormControl', () => {
        const formGroup = service.createAILanguageDetectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAILanguageDetection should disable id FormControl', () => {
        const formGroup = service.createAILanguageDetectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
