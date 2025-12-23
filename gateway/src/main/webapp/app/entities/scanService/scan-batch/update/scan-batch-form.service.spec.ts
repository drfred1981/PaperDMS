import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scan-batch.test-samples';

import { ScanBatchFormService } from './scan-batch-form.service';

describe('ScanBatch Form Service', () => {
  let service: ScanBatchFormService;

  beforeEach(() => {
    service = TestBed.inject(ScanBatchFormService);
  });

  describe('Service methods', () => {
    describe('createScanBatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScanBatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            totalJobs: expect.any(Object),
            completedJobs: expect.any(Object),
            totalPages: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IScanBatch should create a new form with FormGroup', () => {
        const formGroup = service.createScanBatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            totalJobs: expect.any(Object),
            completedJobs: expect.any(Object),
            totalPages: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getScanBatch', () => {
      it('should return NewScanBatch for default ScanBatch initial value', () => {
        const formGroup = service.createScanBatchFormGroup(sampleWithNewData);

        const scanBatch = service.getScanBatch(formGroup);

        expect(scanBatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewScanBatch for empty ScanBatch initial value', () => {
        const formGroup = service.createScanBatchFormGroup();

        const scanBatch = service.getScanBatch(formGroup);

        expect(scanBatch).toMatchObject({});
      });

      it('should return IScanBatch', () => {
        const formGroup = service.createScanBatchFormGroup(sampleWithRequiredData);

        const scanBatch = service.getScanBatch(formGroup);

        expect(scanBatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScanBatch should not enable id FormControl', () => {
        const formGroup = service.createScanBatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScanBatch should disable id FormControl', () => {
        const formGroup = service.createScanBatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
