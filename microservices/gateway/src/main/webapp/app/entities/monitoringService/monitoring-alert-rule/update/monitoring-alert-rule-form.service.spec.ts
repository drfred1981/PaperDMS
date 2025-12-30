import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-alert-rule.test-samples';

import { MonitoringAlertRuleFormService } from './monitoring-alert-rule-form.service';

describe('MonitoringAlertRule Form Service', () => {
  let service: MonitoringAlertRuleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringAlertRuleFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringAlertRuleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            alertType: expect.any(Object),
            conditions: expect.any(Object),
            severity: expect.any(Object),
            recipients: expect.any(Object),
            isActive: expect.any(Object),
            triggerCount: expect.any(Object),
            lastTriggered: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringAlertRule should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            alertType: expect.any(Object),
            conditions: expect.any(Object),
            severity: expect.any(Object),
            recipients: expect.any(Object),
            isActive: expect.any(Object),
            triggerCount: expect.any(Object),
            lastTriggered: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringAlertRule', () => {
      it('should return NewMonitoringAlertRule for default MonitoringAlertRule initial value', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup(sampleWithNewData);

        const monitoringAlertRule = service.getMonitoringAlertRule(formGroup) as any;

        expect(monitoringAlertRule).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringAlertRule for empty MonitoringAlertRule initial value', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup();

        const monitoringAlertRule = service.getMonitoringAlertRule(formGroup) as any;

        expect(monitoringAlertRule).toMatchObject({});
      });

      it('should return IMonitoringAlertRule', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup(sampleWithRequiredData);

        const monitoringAlertRule = service.getMonitoringAlertRule(formGroup) as any;

        expect(monitoringAlertRule).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringAlertRule should not enable id FormControl', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringAlertRule should disable id FormControl', () => {
        const formGroup = service.createMonitoringAlertRuleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
