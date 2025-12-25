import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ocr-comparison.test-samples';

import { OcrComparisonFormService } from './ocr-comparison-form.service';

describe('OcrComparison Form Service', () => {
  let service: OcrComparisonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OcrComparisonFormService);
  });

  describe('Service methods', () => {
    describe('createOcrComparisonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOcrComparisonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            pageNumber: expect.any(Object),
            tikaText: expect.any(Object),
            tikaConfidence: expect.any(Object),
            aiText: expect.any(Object),
            aiConfidence: expect.any(Object),
            similarity: expect.any(Object),
            differences: expect.any(Object),
            differencesS3Key: expect.any(Object),
            selectedEngine: expect.any(Object),
            selectedBy: expect.any(Object),
            selectedDate: expect.any(Object),
            comparisonDate: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IOcrComparison should create a new form with FormGroup', () => {
        const formGroup = service.createOcrComparisonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            pageNumber: expect.any(Object),
            tikaText: expect.any(Object),
            tikaConfidence: expect.any(Object),
            aiText: expect.any(Object),
            aiConfidence: expect.any(Object),
            similarity: expect.any(Object),
            differences: expect.any(Object),
            differencesS3Key: expect.any(Object),
            selectedEngine: expect.any(Object),
            selectedBy: expect.any(Object),
            selectedDate: expect.any(Object),
            comparisonDate: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getOcrComparison', () => {
      it('should return NewOcrComparison for default OcrComparison initial value', () => {
        const formGroup = service.createOcrComparisonFormGroup(sampleWithNewData);

        const ocrComparison = service.getOcrComparison(formGroup) as any;

        expect(ocrComparison).toMatchObject(sampleWithNewData);
      });

      it('should return NewOcrComparison for empty OcrComparison initial value', () => {
        const formGroup = service.createOcrComparisonFormGroup();

        const ocrComparison = service.getOcrComparison(formGroup) as any;

        expect(ocrComparison).toMatchObject({});
      });

      it('should return IOcrComparison', () => {
        const formGroup = service.createOcrComparisonFormGroup(sampleWithRequiredData);

        const ocrComparison = service.getOcrComparison(formGroup) as any;

        expect(ocrComparison).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOcrComparison should not enable id FormControl', () => {
        const formGroup = service.createOcrComparisonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOcrComparison should disable id FormControl', () => {
        const formGroup = service.createOcrComparisonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
