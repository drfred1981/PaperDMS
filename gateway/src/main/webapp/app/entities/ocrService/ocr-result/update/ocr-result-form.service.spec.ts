import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ocr-result.test-samples';

import { OcrResultFormService } from './ocr-result-form.service';

describe('OcrResult Form Service', () => {
  let service: OcrResultFormService;

  beforeEach(() => {
    service = TestBed.inject(OcrResultFormService);
  });

  describe('Service methods', () => {
    describe('createOcrResultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOcrResultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pageNumber: expect.any(Object),
            pageSha256: expect.any(Object),
            confidence: expect.any(Object),
            s3ResultKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            s3BoundingBoxKey: expect.any(Object),
            boundingBoxes: expect.any(Object),
            metadata: expect.any(Object),
            language: expect.any(Object),
            wordCount: expect.any(Object),
            ocrEngine: expect.any(Object),
            processingTime: expect.any(Object),
            rawResponse: expect.any(Object),
            rawResponseS3Key: expect.any(Object),
            processedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });

      it('passing IOcrResult should create a new form with FormGroup', () => {
        const formGroup = service.createOcrResultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pageNumber: expect.any(Object),
            pageSha256: expect.any(Object),
            confidence: expect.any(Object),
            s3ResultKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            s3BoundingBoxKey: expect.any(Object),
            boundingBoxes: expect.any(Object),
            metadata: expect.any(Object),
            language: expect.any(Object),
            wordCount: expect.any(Object),
            ocrEngine: expect.any(Object),
            processingTime: expect.any(Object),
            rawResponse: expect.any(Object),
            rawResponseS3Key: expect.any(Object),
            processedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });
    });

    describe('getOcrResult', () => {
      it('should return NewOcrResult for default OcrResult initial value', () => {
        const formGroup = service.createOcrResultFormGroup(sampleWithNewData);

        const ocrResult = service.getOcrResult(formGroup);

        expect(ocrResult).toMatchObject(sampleWithNewData);
      });

      it('should return NewOcrResult for empty OcrResult initial value', () => {
        const formGroup = service.createOcrResultFormGroup();

        const ocrResult = service.getOcrResult(formGroup);

        expect(ocrResult).toMatchObject({});
      });

      it('should return IOcrResult', () => {
        const formGroup = service.createOcrResultFormGroup(sampleWithRequiredData);

        const ocrResult = service.getOcrResult(formGroup);

        expect(ocrResult).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOcrResult should not enable id FormControl', () => {
        const formGroup = service.createOcrResultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOcrResult should disable id FormControl', () => {
        const formGroup = service.createOcrResultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
