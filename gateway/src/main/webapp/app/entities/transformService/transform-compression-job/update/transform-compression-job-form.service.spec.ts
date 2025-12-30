import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transform-compression-job.test-samples';

import { TransformCompressionJobFormService } from './transform-compression-job-form.service';

describe('TransformCompressionJob Form Service', () => {
  let service: TransformCompressionJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransformCompressionJobFormService);
  });

  describe('Service methods', () => {
    describe('createTransformCompressionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransformCompressionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            compressionType: expect.any(Object),
            quality: expect.any(Object),
            targetSizeKb: expect.any(Object),
            originalSize: expect.any(Object),
            compressedSize: expect.any(Object),
            compressionRatio: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentSha256: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ITransformCompressionJob should create a new form with FormGroup', () => {
        const formGroup = service.createTransformCompressionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            compressionType: expect.any(Object),
            quality: expect.any(Object),
            targetSizeKb: expect.any(Object),
            originalSize: expect.any(Object),
            compressedSize: expect.any(Object),
            compressionRatio: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentSha256: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransformCompressionJob', () => {
      it('should return NewTransformCompressionJob for default TransformCompressionJob initial value', () => {
        const formGroup = service.createTransformCompressionJobFormGroup(sampleWithNewData);

        const transformCompressionJob = service.getTransformCompressionJob(formGroup) as any;

        expect(transformCompressionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransformCompressionJob for empty TransformCompressionJob initial value', () => {
        const formGroup = service.createTransformCompressionJobFormGroup();

        const transformCompressionJob = service.getTransformCompressionJob(formGroup) as any;

        expect(transformCompressionJob).toMatchObject({});
      });

      it('should return ITransformCompressionJob', () => {
        const formGroup = service.createTransformCompressionJobFormGroup(sampleWithRequiredData);

        const transformCompressionJob = service.getTransformCompressionJob(formGroup) as any;

        expect(transformCompressionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransformCompressionJob should not enable id FormControl', () => {
        const formGroup = service.createTransformCompressionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransformCompressionJob should disable id FormControl', () => {
        const formGroup = service.createTransformCompressionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
