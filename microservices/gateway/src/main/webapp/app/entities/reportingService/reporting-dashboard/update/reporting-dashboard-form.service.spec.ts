import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-dashboard.test-samples';

import { ReportingDashboardFormService } from './reporting-dashboard-form.service';

describe('ReportingDashboard Form Service', () => {
  let service: ReportingDashboardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingDashboardFormService);
  });

  describe('Service methods', () => {
    describe('createReportingDashboardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingDashboardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            userId: expect.any(Object),
            isPublic: expect.any(Object),
            layout: expect.any(Object),
            refreshInterval: expect.any(Object),
            isDefault: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IReportingDashboard should create a new form with FormGroup', () => {
        const formGroup = service.createReportingDashboardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            userId: expect.any(Object),
            isPublic: expect.any(Object),
            layout: expect.any(Object),
            refreshInterval: expect.any(Object),
            isDefault: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportingDashboard', () => {
      it('should return NewReportingDashboard for default ReportingDashboard initial value', () => {
        const formGroup = service.createReportingDashboardFormGroup(sampleWithNewData);

        const reportingDashboard = service.getReportingDashboard(formGroup) as any;

        expect(reportingDashboard).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingDashboard for empty ReportingDashboard initial value', () => {
        const formGroup = service.createReportingDashboardFormGroup();

        const reportingDashboard = service.getReportingDashboard(formGroup) as any;

        expect(reportingDashboard).toMatchObject({});
      });

      it('should return IReportingDashboard', () => {
        const formGroup = service.createReportingDashboardFormGroup(sampleWithRequiredData);

        const reportingDashboard = service.getReportingDashboard(formGroup) as any;

        expect(reportingDashboard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingDashboard should not enable id FormControl', () => {
        const formGroup = service.createReportingDashboardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingDashboard should disable id FormControl', () => {
        const formGroup = service.createReportingDashboardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
