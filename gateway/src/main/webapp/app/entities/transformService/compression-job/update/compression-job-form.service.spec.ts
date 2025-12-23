import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compression-job.test-samples';

import { CompressionJobFormService } from './compression-job-form.service';

describe('CompressionJob Form Service', () => {
  let service: CompressionJobFormService;

  beforeEach(() => {
    service = TestBed.inject(CompressionJobFormService);
  });

  describe('Service methods', () => {
    describe('createCompressionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompressionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            compressionType: expect.any(Object),
            quality: expect.any(Object),
            targetSizeKb: expect.any(Object),
            originalSize: expect.any(Object),
            compressedSize: expect.any(Object),
            compressionRatio: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentId: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ICompressionJob should create a new form with FormGroup', () => {
        const formGroup = service.createCompressionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            compressionType: expect.any(Object),
            quality: expect.any(Object),
            targetSizeKb: expect.any(Object),
            originalSize: expect.any(Object),
            compressedSize: expect.any(Object),
            compressionRatio: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentId: expect.any(Object),
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

    describe('getCompressionJob', () => {
      it('should return NewCompressionJob for default CompressionJob initial value', () => {
        const formGroup = service.createCompressionJobFormGroup(sampleWithNewData);

        const compressionJob = service.getCompressionJob(formGroup);

        expect(compressionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompressionJob for empty CompressionJob initial value', () => {
        const formGroup = service.createCompressionJobFormGroup();

        const compressionJob = service.getCompressionJob(formGroup);

        expect(compressionJob).toMatchObject({});
      });

      it('should return ICompressionJob', () => {
        const formGroup = service.createCompressionJobFormGroup(sampleWithRequiredData);

        const compressionJob = service.getCompressionJob(formGroup);

        expect(compressionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompressionJob should not enable id FormControl', () => {
        const formGroup = service.createCompressionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompressionJob should disable id FormControl', () => {
        const formGroup = service.createCompressionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
