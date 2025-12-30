import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-alert.test-samples';

import { MonitoringAlertFormService } from './monitoring-alert-form.service';

describe('MonitoringAlert Form Service', () => {
  let service: MonitoringAlertFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringAlertFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringAlertFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringAlertFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            severity: expect.any(Object),
            title: expect.any(Object),
            message: expect.any(Object),
            entityType: expect.any(Object),
            entityName: expect.any(Object),
            status: expect.any(Object),
            triggeredDate: expect.any(Object),
            acknowledgedBy: expect.any(Object),
            acknowledgedDate: expect.any(Object),
            resolvedBy: expect.any(Object),
            resolvedDate: expect.any(Object),
            alertRule: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringAlert should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringAlertFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            severity: expect.any(Object),
            title: expect.any(Object),
            message: expect.any(Object),
            entityType: expect.any(Object),
            entityName: expect.any(Object),
            status: expect.any(Object),
            triggeredDate: expect.any(Object),
            acknowledgedBy: expect.any(Object),
            acknowledgedDate: expect.any(Object),
            resolvedBy: expect.any(Object),
            resolvedDate: expect.any(Object),
            alertRule: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringAlert', () => {
      it('should return NewMonitoringAlert for default MonitoringAlert initial value', () => {
        const formGroup = service.createMonitoringAlertFormGroup(sampleWithNewData);

        const monitoringAlert = service.getMonitoringAlert(formGroup) as any;

        expect(monitoringAlert).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringAlert for empty MonitoringAlert initial value', () => {
        const formGroup = service.createMonitoringAlertFormGroup();

        const monitoringAlert = service.getMonitoringAlert(formGroup) as any;

        expect(monitoringAlert).toMatchObject({});
      });

      it('should return IMonitoringAlert', () => {
        const formGroup = service.createMonitoringAlertFormGroup(sampleWithRequiredData);

        const monitoringAlert = service.getMonitoringAlert(formGroup) as any;

        expect(monitoringAlert).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringAlert should not enable id FormControl', () => {
        const formGroup = service.createMonitoringAlertFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringAlert should disable id FormControl', () => {
        const formGroup = service.createMonitoringAlertFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
