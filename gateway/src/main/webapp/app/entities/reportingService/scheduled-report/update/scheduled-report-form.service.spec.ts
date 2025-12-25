import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scheduled-report.test-samples';

import { ScheduledReportFormService } from './scheduled-report-form.service';

describe('ScheduledReport Form Service', () => {
  let service: ScheduledReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScheduledReportFormService);
  });

  describe('Service methods', () => {
    describe('createScheduledReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScheduledReportFormGroup();

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

      it('passing IScheduledReport should create a new form with FormGroup', () => {
        const formGroup = service.createScheduledReportFormGroup(sampleWithRequiredData);

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

    describe('getScheduledReport', () => {
      it('should return NewScheduledReport for default ScheduledReport initial value', () => {
        const formGroup = service.createScheduledReportFormGroup(sampleWithNewData);

        const scheduledReport = service.getScheduledReport(formGroup) as any;

        expect(scheduledReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewScheduledReport for empty ScheduledReport initial value', () => {
        const formGroup = service.createScheduledReportFormGroup();

        const scheduledReport = service.getScheduledReport(formGroup) as any;

        expect(scheduledReport).toMatchObject({});
      });

      it('should return IScheduledReport', () => {
        const formGroup = service.createScheduledReportFormGroup(sampleWithRequiredData);

        const scheduledReport = service.getScheduledReport(formGroup) as any;

        expect(scheduledReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScheduledReport should not enable id FormControl', () => {
        const formGroup = service.createScheduledReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScheduledReport should disable id FormControl', () => {
        const formGroup = service.createScheduledReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
