import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../merge-job.test-samples';

import { MergeJobFormService } from './merge-job-form.service';

describe('MergeJob Form Service', () => {
  let service: MergeJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MergeJobFormService);
  });

  describe('Service methods', () => {
    describe('createMergeJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMergeJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sourceDocumentIds: expect.any(Object),
            mergeOrder: expect.any(Object),
            includeBookmarks: expect.any(Object),
            includeToc: expect.any(Object),
            addPageNumbers: expect.any(Object),
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

      it('passing IMergeJob should create a new form with FormGroup', () => {
        const formGroup = service.createMergeJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sourceDocumentIds: expect.any(Object),
            mergeOrder: expect.any(Object),
            includeBookmarks: expect.any(Object),
            includeToc: expect.any(Object),
            addPageNumbers: expect.any(Object),
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

    describe('getMergeJob', () => {
      it('should return NewMergeJob for default MergeJob initial value', () => {
        const formGroup = service.createMergeJobFormGroup(sampleWithNewData);

        const mergeJob = service.getMergeJob(formGroup) as any;

        expect(mergeJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewMergeJob for empty MergeJob initial value', () => {
        const formGroup = service.createMergeJobFormGroup();

        const mergeJob = service.getMergeJob(formGroup) as any;

        expect(mergeJob).toMatchObject({});
      });

      it('should return IMergeJob', () => {
        const formGroup = service.createMergeJobFormGroup(sampleWithRequiredData);

        const mergeJob = service.getMergeJob(formGroup) as any;

        expect(mergeJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMergeJob should not enable id FormControl', () => {
        const formGroup = service.createMergeJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMergeJob should disable id FormControl', () => {
        const formGroup = service.createMergeJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
