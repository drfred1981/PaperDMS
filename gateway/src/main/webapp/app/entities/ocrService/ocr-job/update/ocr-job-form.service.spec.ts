import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ocr-job.test-samples';

import { OcrJobFormService } from './ocr-job-form.service';

describe('OcrJob Form Service', () => {
  let service: OcrJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OcrJobFormService);
  });

  describe('Service methods', () => {
    describe('createOcrJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOcrJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            s3Bucket: expect.any(Object),
            requestedLanguage: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            ocrEngine: expect.any(Object),
            tikaEndpoint: expect.any(Object),
            aiProvider: expect.any(Object),
            aiModel: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            pageCount: expect.any(Object),
            progress: expect.any(Object),
            retryCount: expect.any(Object),
            priority: expect.any(Object),
            processingTime: expect.any(Object),
            costEstimate: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });

      it('passing IOcrJob should create a new form with FormGroup', () => {
        const formGroup = service.createOcrJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            s3Key: expect.any(Object),
            s3Bucket: expect.any(Object),
            requestedLanguage: expect.any(Object),
            detectedLanguage: expect.any(Object),
            languageConfidence: expect.any(Object),
            ocrEngine: expect.any(Object),
            tikaEndpoint: expect.any(Object),
            aiProvider: expect.any(Object),
            aiModel: expect.any(Object),
            resultCacheKey: expect.any(Object),
            isCached: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            pageCount: expect.any(Object),
            progress: expect.any(Object),
            retryCount: expect.any(Object),
            priority: expect.any(Object),
            processingTime: expect.any(Object),
            costEstimate: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getOcrJob', () => {
      it('should return NewOcrJob for default OcrJob initial value', () => {
        const formGroup = service.createOcrJobFormGroup(sampleWithNewData);

        const ocrJob = service.getOcrJob(formGroup) as any;

        expect(ocrJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewOcrJob for empty OcrJob initial value', () => {
        const formGroup = service.createOcrJobFormGroup();

        const ocrJob = service.getOcrJob(formGroup) as any;

        expect(ocrJob).toMatchObject({});
      });

      it('should return IOcrJob', () => {
        const formGroup = service.createOcrJobFormGroup(sampleWithRequiredData);

        const ocrJob = service.getOcrJob(formGroup) as any;

        expect(ocrJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOcrJob should not enable id FormControl', () => {
        const formGroup = service.createOcrJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOcrJob should disable id FormControl', () => {
        const formGroup = service.createOcrJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
