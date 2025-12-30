import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ocr-cache.test-samples';

import { OcrCacheFormService } from './ocr-cache-form.service';

describe('OcrCache Form Service', () => {
  let service: OcrCacheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OcrCacheFormService);
  });

  describe('Service methods', () => {
    describe('createOcrCacheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOcrCacheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            ocrEngine: expect.any(Object),
            language: expect.any(Object),
            pageCount: expect.any(Object),
            totalConfidence: expect.any(Object),
            s3ResultKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            orcExtractedTextS3Key: expect.any(Object),
            metadata: expect.any(Object),
            hits: expect.any(Object),
            lastAccessDate: expect.any(Object),
            createdDate: expect.any(Object),
            expirationDate: expect.any(Object),
          }),
        );
      });

      it('passing IOcrCache should create a new form with FormGroup', () => {
        const formGroup = service.createOcrCacheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            ocrEngine: expect.any(Object),
            language: expect.any(Object),
            pageCount: expect.any(Object),
            totalConfidence: expect.any(Object),
            s3ResultKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            orcExtractedTextS3Key: expect.any(Object),
            metadata: expect.any(Object),
            hits: expect.any(Object),
            lastAccessDate: expect.any(Object),
            createdDate: expect.any(Object),
            expirationDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getOcrCache', () => {
      it('should return NewOcrCache for default OcrCache initial value', () => {
        const formGroup = service.createOcrCacheFormGroup(sampleWithNewData);

        const ocrCache = service.getOcrCache(formGroup) as any;

        expect(ocrCache).toMatchObject(sampleWithNewData);
      });

      it('should return NewOcrCache for empty OcrCache initial value', () => {
        const formGroup = service.createOcrCacheFormGroup();

        const ocrCache = service.getOcrCache(formGroup) as any;

        expect(ocrCache).toMatchObject({});
      });

      it('should return IOcrCache', () => {
        const formGroup = service.createOcrCacheFormGroup(sampleWithRequiredData);

        const ocrCache = service.getOcrCache(formGroup) as any;

        expect(ocrCache).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOcrCache should not enable id FormControl', () => {
        const formGroup = service.createOcrCacheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOcrCache should disable id FormControl', () => {
        const formGroup = service.createOcrCacheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
