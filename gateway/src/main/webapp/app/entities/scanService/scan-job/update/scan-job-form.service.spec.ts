import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scan-job.test-samples';

import { ScanJobFormService } from './scan-job-form.service';

describe('ScanJob Form Service', () => {
  let service: ScanJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScanJobFormService);
  });

  describe('Service methods', () => {
    describe('createScanJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScanJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            scannerConfigId: expect.any(Object),
            batchId: expect.any(Object),
            documentTypeId: expect.any(Object),
            folderId: expect.any(Object),
            pageCount: expect.any(Object),
            status: expect.any(Object),
            colorMode: expect.any(Object),
            resolution: expect.any(Object),
            fileFormat: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            scannerConfig: expect.any(Object),
            batch: expect.any(Object),
          }),
        );
      });

      it('passing IScanJob should create a new form with FormGroup', () => {
        const formGroup = service.createScanJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            scannerConfigId: expect.any(Object),
            batchId: expect.any(Object),
            documentTypeId: expect.any(Object),
            folderId: expect.any(Object),
            pageCount: expect.any(Object),
            status: expect.any(Object),
            colorMode: expect.any(Object),
            resolution: expect.any(Object),
            fileFormat: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            scannerConfig: expect.any(Object),
            batch: expect.any(Object),
          }),
        );
      });
    });

    describe('getScanJob', () => {
      it('should return NewScanJob for default ScanJob initial value', () => {
        const formGroup = service.createScanJobFormGroup(sampleWithNewData);

        const scanJob = service.getScanJob(formGroup) as any;

        expect(scanJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewScanJob for empty ScanJob initial value', () => {
        const formGroup = service.createScanJobFormGroup();

        const scanJob = service.getScanJob(formGroup) as any;

        expect(scanJob).toMatchObject({});
      });

      it('should return IScanJob', () => {
        const formGroup = service.createScanJobFormGroup(sampleWithRequiredData);

        const scanJob = service.getScanJob(formGroup) as any;

        expect(scanJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScanJob should not enable id FormControl', () => {
        const formGroup = service.createScanJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScanJob should disable id FormControl', () => {
        const formGroup = service.createScanJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
