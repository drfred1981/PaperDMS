import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../similarity-document-fingerprint.test-samples';

import { SimilarityDocumentFingerprintFormService } from './similarity-document-fingerprint-form.service';

describe('SimilarityDocumentFingerprint Form Service', () => {
  let service: SimilarityDocumentFingerprintFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimilarityDocumentFingerprintFormService);
  });

  describe('Service methods', () => {
    describe('createSimilarityDocumentFingerprintFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fingerprintType: expect.any(Object),
            fingerprint: expect.any(Object),
            vectorEmbedding: expect.any(Object),
            metadata: expect.any(Object),
            computedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });

      it('passing ISimilarityDocumentFingerprint should create a new form with FormGroup', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fingerprintType: expect.any(Object),
            fingerprint: expect.any(Object),
            vectorEmbedding: expect.any(Object),
            metadata: expect.any(Object),
            computedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });
    });

    describe('getSimilarityDocumentFingerprint', () => {
      it('should return NewSimilarityDocumentFingerprint for default SimilarityDocumentFingerprint initial value', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup(sampleWithNewData);

        const similarityDocumentFingerprint = service.getSimilarityDocumentFingerprint(formGroup) as any;

        expect(similarityDocumentFingerprint).toMatchObject(sampleWithNewData);
      });

      it('should return NewSimilarityDocumentFingerprint for empty SimilarityDocumentFingerprint initial value', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup();

        const similarityDocumentFingerprint = service.getSimilarityDocumentFingerprint(formGroup) as any;

        expect(similarityDocumentFingerprint).toMatchObject({});
      });

      it('should return ISimilarityDocumentFingerprint', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup(sampleWithRequiredData);

        const similarityDocumentFingerprint = service.getSimilarityDocumentFingerprint(formGroup) as any;

        expect(similarityDocumentFingerprint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISimilarityDocumentFingerprint should not enable id FormControl', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSimilarityDocumentFingerprint should disable id FormControl', () => {
        const formGroup = service.createSimilarityDocumentFingerprintFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
