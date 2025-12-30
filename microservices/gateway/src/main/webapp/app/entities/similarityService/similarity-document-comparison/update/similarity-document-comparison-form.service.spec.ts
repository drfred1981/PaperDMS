import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../similarity-document-comparison.test-samples';

import { SimilarityDocumentComparisonFormService } from './similarity-document-comparison-form.service';

describe('SimilarityDocumentComparison Form Service', () => {
  let service: SimilarityDocumentComparisonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimilarityDocumentComparisonFormService);
  });

  describe('Service methods', () => {
    describe('createSimilarityDocumentComparisonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentSha256: expect.any(Object),
            targetDocumentSha256: expect.any(Object),
            similarityScore: expect.any(Object),
            algorithm: expect.any(Object),
            features: expect.any(Object),
            computedDate: expect.any(Object),
            isRelevant: expect.any(Object),
            reviewedBy: expect.any(Object),
            reviewedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });

      it('passing ISimilarityDocumentComparison should create a new form with FormGroup', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentSha256: expect.any(Object),
            targetDocumentSha256: expect.any(Object),
            similarityScore: expect.any(Object),
            algorithm: expect.any(Object),
            features: expect.any(Object),
            computedDate: expect.any(Object),
            isRelevant: expect.any(Object),
            reviewedBy: expect.any(Object),
            reviewedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });
    });

    describe('getSimilarityDocumentComparison', () => {
      it('should return NewSimilarityDocumentComparison for default SimilarityDocumentComparison initial value', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup(sampleWithNewData);

        const similarityDocumentComparison = service.getSimilarityDocumentComparison(formGroup) as any;

        expect(similarityDocumentComparison).toMatchObject(sampleWithNewData);
      });

      it('should return NewSimilarityDocumentComparison for empty SimilarityDocumentComparison initial value', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup();

        const similarityDocumentComparison = service.getSimilarityDocumentComparison(formGroup) as any;

        expect(similarityDocumentComparison).toMatchObject({});
      });

      it('should return ISimilarityDocumentComparison', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup(sampleWithRequiredData);

        const similarityDocumentComparison = service.getSimilarityDocumentComparison(formGroup) as any;

        expect(similarityDocumentComparison).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISimilarityDocumentComparison should not enable id FormControl', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSimilarityDocumentComparison should disable id FormControl', () => {
        const formGroup = service.createSimilarityDocumentComparisonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
