import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-execution.test-samples';

import { ReportingExecutionFormService } from './reporting-execution-form.service';

describe('ReportingExecution Form Service', () => {
  let service: ReportingExecutionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingExecutionFormService);
  });

  describe('Service methods', () => {
    describe('createReportingExecutionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingExecutionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            recordsProcessed: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputSize: expect.any(Object),
            errorMessage: expect.any(Object),
            scheduledReport: expect.any(Object),
          }),
        );
      });

      it('passing IReportingExecution should create a new form with FormGroup', () => {
        const formGroup = service.createReportingExecutionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            recordsProcessed: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputSize: expect.any(Object),
            errorMessage: expect.any(Object),
            scheduledReport: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportingExecution', () => {
      it('should return NewReportingExecution for default ReportingExecution initial value', () => {
        const formGroup = service.createReportingExecutionFormGroup(sampleWithNewData);

        const reportingExecution = service.getReportingExecution(formGroup) as any;

        expect(reportingExecution).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingExecution for empty ReportingExecution initial value', () => {
        const formGroup = service.createReportingExecutionFormGroup();

        const reportingExecution = service.getReportingExecution(formGroup) as any;

        expect(reportingExecution).toMatchObject({});
      });

      it('should return IReportingExecution', () => {
        const formGroup = service.createReportingExecutionFormGroup(sampleWithRequiredData);

        const reportingExecution = service.getReportingExecution(formGroup) as any;

        expect(reportingExecution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingExecution should not enable id FormControl', () => {
        const formGroup = service.createReportingExecutionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingExecution should disable id FormControl', () => {
        const formGroup = service.createReportingExecutionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
