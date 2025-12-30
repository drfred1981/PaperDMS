import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-dashboard-widget.test-samples';

import { ReportingDashboardWidgetFormService } from './reporting-dashboard-widget-form.service';

describe('ReportingDashboardWidget Form Service', () => {
  let service: ReportingDashboardWidgetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingDashboardWidgetFormService);
  });

  describe('Service methods', () => {
    describe('createReportingDashboardWidgetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            widgetType: expect.any(Object),
            title: expect.any(Object),
            configuration: expect.any(Object),
            dataSource: expect.any(Object),
            position: expect.any(Object),
            sizeX: expect.any(Object),
            sizeY: expect.any(Object),
            dashboar: expect.any(Object),
          }),
        );
      });

      it('passing IReportingDashboardWidget should create a new form with FormGroup', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            widgetType: expect.any(Object),
            title: expect.any(Object),
            configuration: expect.any(Object),
            dataSource: expect.any(Object),
            position: expect.any(Object),
            sizeX: expect.any(Object),
            sizeY: expect.any(Object),
            dashboar: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportingDashboardWidget', () => {
      it('should return NewReportingDashboardWidget for default ReportingDashboardWidget initial value', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup(sampleWithNewData);

        const reportingDashboardWidget = service.getReportingDashboardWidget(formGroup) as any;

        expect(reportingDashboardWidget).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingDashboardWidget for empty ReportingDashboardWidget initial value', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup();

        const reportingDashboardWidget = service.getReportingDashboardWidget(formGroup) as any;

        expect(reportingDashboardWidget).toMatchObject({});
      });

      it('should return IReportingDashboardWidget', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup(sampleWithRequiredData);

        const reportingDashboardWidget = service.getReportingDashboardWidget(formGroup) as any;

        expect(reportingDashboardWidget).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingDashboardWidget should not enable id FormControl', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingDashboardWidget should disable id FormControl', () => {
        const formGroup = service.createReportingDashboardWidgetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
