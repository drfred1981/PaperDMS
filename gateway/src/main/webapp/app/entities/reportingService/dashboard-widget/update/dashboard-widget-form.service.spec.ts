import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../dashboard-widget.test-samples';

import { DashboardWidgetFormService } from './dashboard-widget-form.service';

describe('DashboardWidget Form Service', () => {
  let service: DashboardWidgetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardWidgetFormService);
  });

  describe('Service methods', () => {
    describe('createDashboardWidgetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDashboardWidgetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dashboardId: expect.any(Object),
            widgetType: expect.any(Object),
            title: expect.any(Object),
            configuration: expect.any(Object),
            dataSource: expect.any(Object),
            position: expect.any(Object),
            sizeX: expect.any(Object),
            sizeY: expect.any(Object),
            dashboard: expect.any(Object),
          }),
        );
      });

      it('passing IDashboardWidget should create a new form with FormGroup', () => {
        const formGroup = service.createDashboardWidgetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dashboardId: expect.any(Object),
            widgetType: expect.any(Object),
            title: expect.any(Object),
            configuration: expect.any(Object),
            dataSource: expect.any(Object),
            position: expect.any(Object),
            sizeX: expect.any(Object),
            sizeY: expect.any(Object),
            dashboard: expect.any(Object),
          }),
        );
      });
    });

    describe('getDashboardWidget', () => {
      it('should return NewDashboardWidget for default DashboardWidget initial value', () => {
        const formGroup = service.createDashboardWidgetFormGroup(sampleWithNewData);

        const dashboardWidget = service.getDashboardWidget(formGroup) as any;

        expect(dashboardWidget).toMatchObject(sampleWithNewData);
      });

      it('should return NewDashboardWidget for empty DashboardWidget initial value', () => {
        const formGroup = service.createDashboardWidgetFormGroup();

        const dashboardWidget = service.getDashboardWidget(formGroup) as any;

        expect(dashboardWidget).toMatchObject({});
      });

      it('should return IDashboardWidget', () => {
        const formGroup = service.createDashboardWidgetFormGroup(sampleWithRequiredData);

        const dashboardWidget = service.getDashboardWidget(formGroup) as any;

        expect(dashboardWidget).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDashboardWidget should not enable id FormControl', () => {
        const formGroup = service.createDashboardWidgetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDashboardWidget should disable id FormControl', () => {
        const formGroup = service.createDashboardWidgetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
