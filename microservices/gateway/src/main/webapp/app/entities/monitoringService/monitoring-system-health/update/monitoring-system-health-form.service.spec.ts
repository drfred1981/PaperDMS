import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-system-health.test-samples';

import { MonitoringSystemHealthFormService } from './monitoring-system-health-form.service';

describe('MonitoringSystemHealth Form Service', () => {
  let service: MonitoringSystemHealthFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringSystemHealthFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringSystemHealthFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceName: expect.any(Object),
            status: expect.any(Object),
            version: expect.any(Object),
            uptime: expect.any(Object),
            cpuUsage: expect.any(Object),
            memoryUsage: expect.any(Object),
            errorRate: expect.any(Object),
            lastCheck: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringSystemHealth should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceName: expect.any(Object),
            status: expect.any(Object),
            version: expect.any(Object),
            uptime: expect.any(Object),
            cpuUsage: expect.any(Object),
            memoryUsage: expect.any(Object),
            errorRate: expect.any(Object),
            lastCheck: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringSystemHealth', () => {
      it('should return NewMonitoringSystemHealth for default MonitoringSystemHealth initial value', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup(sampleWithNewData);

        const monitoringSystemHealth = service.getMonitoringSystemHealth(formGroup) as any;

        expect(monitoringSystemHealth).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringSystemHealth for empty MonitoringSystemHealth initial value', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup();

        const monitoringSystemHealth = service.getMonitoringSystemHealth(formGroup) as any;

        expect(monitoringSystemHealth).toMatchObject({});
      });

      it('should return IMonitoringSystemHealth', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup(sampleWithRequiredData);

        const monitoringSystemHealth = service.getMonitoringSystemHealth(formGroup) as any;

        expect(monitoringSystemHealth).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringSystemHealth should not enable id FormControl', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringSystemHealth should disable id FormControl', () => {
        const formGroup = service.createMonitoringSystemHealthFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
