import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../report-execution.test-samples';

import { ReportExecutionFormService } from './report-execution-form.service';

describe('ReportExecution Form Service', () => {
  let service: ReportExecutionFormService;

  beforeEach(() => {
    service = TestBed.inject(ReportExecutionFormService);
  });

  describe('Service methods', () => {
    describe('createReportExecutionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportExecutionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scheduledReportId: expect.any(Object),
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

      it('passing IReportExecution should create a new form with FormGroup', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scheduledReportId: expect.any(Object),
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

    describe('getReportExecution', () => {
      it('should return NewReportExecution for default ReportExecution initial value', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithNewData);

        const reportExecution = service.getReportExecution(formGroup);

        expect(reportExecution).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportExecution for empty ReportExecution initial value', () => {
        const formGroup = service.createReportExecutionFormGroup();

        const reportExecution = service.getReportExecution(formGroup);

        expect(reportExecution).toMatchObject({});
      });

      it('should return IReportExecution', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithRequiredData);

        const reportExecution = service.getReportExecution(formGroup);

        expect(reportExecution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportExecution should not enable id FormControl', () => {
        const formGroup = service.createReportExecutionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportExecution should disable id FormControl', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
