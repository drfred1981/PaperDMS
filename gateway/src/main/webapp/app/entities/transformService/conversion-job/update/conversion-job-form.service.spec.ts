import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../conversion-job.test-samples';

import { ConversionJobFormService } from './conversion-job-form.service';

describe('ConversionJob Form Service', () => {
  let service: ConversionJobFormService;

  beforeEach(() => {
    service = TestBed.inject(ConversionJobFormService);
  });

  describe('Service methods', () => {
    describe('createConversionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConversionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            sourceFormat: expect.any(Object),
            targetFormat: expect.any(Object),
            conversionEngine: expect.any(Object),
            options: expect.any(Object),
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

      it('passing IConversionJob should create a new form with FormGroup', () => {
        const formGroup = service.createConversionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            sourceFormat: expect.any(Object),
            targetFormat: expect.any(Object),
            conversionEngine: expect.any(Object),
            options: expect.any(Object),
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

    describe('getConversionJob', () => {
      it('should return NewConversionJob for default ConversionJob initial value', () => {
        const formGroup = service.createConversionJobFormGroup(sampleWithNewData);

        const conversionJob = service.getConversionJob(formGroup);

        expect(conversionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewConversionJob for empty ConversionJob initial value', () => {
        const formGroup = service.createConversionJobFormGroup();

        const conversionJob = service.getConversionJob(formGroup);

        expect(conversionJob).toMatchObject({});
      });

      it('should return IConversionJob', () => {
        const formGroup = service.createConversionJobFormGroup(sampleWithRequiredData);

        const conversionJob = service.getConversionJob(formGroup);

        expect(conversionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConversionJob should not enable id FormControl', () => {
        const formGroup = service.createConversionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConversionJob should disable id FormControl', () => {
        const formGroup = service.createConversionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
