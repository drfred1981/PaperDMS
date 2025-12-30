import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-service-status.test-samples';

import { MonitoringServiceStatusFormService } from './monitoring-service-status-form.service';

describe('MonitoringServiceStatus Form Service', () => {
  let service: MonitoringServiceStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringServiceStatusFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringServiceStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceName: expect.any(Object),
            serviceType: expect.any(Object),
            status: expect.any(Object),
            endpoint: expect.any(Object),
            port: expect.any(Object),
            version: expect.any(Object),
            lastPing: expect.any(Object),
            isHealthy: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringServiceStatus should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceName: expect.any(Object),
            serviceType: expect.any(Object),
            status: expect.any(Object),
            endpoint: expect.any(Object),
            port: expect.any(Object),
            version: expect.any(Object),
            lastPing: expect.any(Object),
            isHealthy: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringServiceStatus', () => {
      it('should return NewMonitoringServiceStatus for default MonitoringServiceStatus initial value', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup(sampleWithNewData);

        const monitoringServiceStatus = service.getMonitoringServiceStatus(formGroup) as any;

        expect(monitoringServiceStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringServiceStatus for empty MonitoringServiceStatus initial value', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup();

        const monitoringServiceStatus = service.getMonitoringServiceStatus(formGroup) as any;

        expect(monitoringServiceStatus).toMatchObject({});
      });

      it('should return IMonitoringServiceStatus', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup(sampleWithRequiredData);

        const monitoringServiceStatus = service.getMonitoringServiceStatus(formGroup) as any;

        expect(monitoringServiceStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringServiceStatus should not enable id FormControl', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringServiceStatus should disable id FormControl', () => {
        const formGroup = service.createMonitoringServiceStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
