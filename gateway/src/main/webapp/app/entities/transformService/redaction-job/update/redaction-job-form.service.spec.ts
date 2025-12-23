import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../redaction-job.test-samples';

import { RedactionJobFormService } from './redaction-job-form.service';

describe('RedactionJob Form Service', () => {
  let service: RedactionJobFormService;

  beforeEach(() => {
    service = TestBed.inject(RedactionJobFormService);
  });

  describe('Service methods', () => {
    describe('createRedactionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRedactionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            redactionAreas: expect.any(Object),
            redactionType: expect.any(Object),
            redactionColor: expect.any(Object),
            replaceWith: expect.any(Object),
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

      it('passing IRedactionJob should create a new form with FormGroup', () => {
        const formGroup = service.createRedactionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            redactionAreas: expect.any(Object),
            redactionType: expect.any(Object),
            redactionColor: expect.any(Object),
            replaceWith: expect.any(Object),
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

    describe('getRedactionJob', () => {
      it('should return NewRedactionJob for default RedactionJob initial value', () => {
        const formGroup = service.createRedactionJobFormGroup(sampleWithNewData);

        const redactionJob = service.getRedactionJob(formGroup);

        expect(redactionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewRedactionJob for empty RedactionJob initial value', () => {
        const formGroup = service.createRedactionJobFormGroup();

        const redactionJob = service.getRedactionJob(formGroup);

        expect(redactionJob).toMatchObject({});
      });

      it('should return IRedactionJob', () => {
        const formGroup = service.createRedactionJobFormGroup(sampleWithRequiredData);

        const redactionJob = service.getRedactionJob(formGroup);

        expect(redactionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRedactionJob should not enable id FormControl', () => {
        const formGroup = service.createRedactionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRedactionJob should disable id FormControl', () => {
        const formGroup = service.createRedactionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
