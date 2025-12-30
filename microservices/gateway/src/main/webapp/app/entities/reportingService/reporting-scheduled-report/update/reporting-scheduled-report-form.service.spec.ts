import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-scheduled-report.test-samples';

import { ReportingScheduledReportFormService } from './reporting-scheduled-report-form.service';

describe('ReportingScheduledReport Form Service', () => {
  let service: ReportingScheduledReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingScheduledReportFormService);
  });

  describe('Service methods', () => {
    describe('createReportingScheduledReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingScheduledReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            reportType: expect.any(Object),
            query: expect.any(Object),
            schedule: expect.any(Object),
            format: expect.any(Object),
            recipients: expect.any(Object),
            isActive: expect.any(Object),
            lastRun: expect.any(Object),
            nextRun: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IReportingScheduledReport should create a new form with FormGroup', () => {
        const formGroup = service.createReportingScheduledReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            reportType: expect.any(Object),
            query: expect.any(Object),
            schedule: expect.any(Object),
            format: expect.any(Object),
            recipients: expect.any(Object),
            isActive: expect.any(Object),
            lastRun: expect.any(Object),
            nextRun: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportingScheduledReport', () => {
      it('should return NewReportingScheduledReport for default ReportingScheduledReport initial value', () => {
        const formGroup = service.createReportingScheduledReportFormGroup(sampleWithNewData);

        const reportingScheduledReport = service.getReportingScheduledReport(formGroup) as any;

        expect(reportingScheduledReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingScheduledReport for empty ReportingScheduledReport initial value', () => {
        const formGroup = service.createReportingScheduledReportFormGroup();

        const reportingScheduledReport = service.getReportingScheduledReport(formGroup) as any;

        expect(reportingScheduledReport).toMatchObject({});
      });

      it('should return IReportingScheduledReport', () => {
        const formGroup = service.createReportingScheduledReportFormGroup(sampleWithRequiredData);

        const reportingScheduledReport = service.getReportingScheduledReport(formGroup) as any;

        expect(reportingScheduledReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingScheduledReport should not enable id FormControl', () => {
        const formGroup = service.createReportingScheduledReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingScheduledReport should disable id FormControl', () => {
        const formGroup = service.createReportingScheduledReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
