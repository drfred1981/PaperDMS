import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../system-metric.test-samples';

import { SystemMetricFormService } from './system-metric-form.service';

describe('SystemMetric Form Service', () => {
  let service: SystemMetricFormService;

  beforeEach(() => {
    service = TestBed.inject(SystemMetricFormService);
  });

  describe('Service methods', () => {
    describe('createSystemMetricFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSystemMetricFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metricName: expect.any(Object),
            cpuUsage: expect.any(Object),
            memoryUsage: expect.any(Object),
            diskUsage: expect.any(Object),
            networkIn: expect.any(Object),
            networkOut: expect.any(Object),
            activeConnections: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });

      it('passing ISystemMetric should create a new form with FormGroup', () => {
        const formGroup = service.createSystemMetricFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metricName: expect.any(Object),
            cpuUsage: expect.any(Object),
            memoryUsage: expect.any(Object),
            diskUsage: expect.any(Object),
            networkIn: expect.any(Object),
            networkOut: expect.any(Object),
            activeConnections: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });
    });

    describe('getSystemMetric', () => {
      it('should return NewSystemMetric for default SystemMetric initial value', () => {
        const formGroup = service.createSystemMetricFormGroup(sampleWithNewData);

        const systemMetric = service.getSystemMetric(formGroup);

        expect(systemMetric).toMatchObject(sampleWithNewData);
      });

      it('should return NewSystemMetric for empty SystemMetric initial value', () => {
        const formGroup = service.createSystemMetricFormGroup();

        const systemMetric = service.getSystemMetric(formGroup);

        expect(systemMetric).toMatchObject({});
      });

      it('should return ISystemMetric', () => {
        const formGroup = service.createSystemMetricFormGroup(sampleWithRequiredData);

        const systemMetric = service.getSystemMetric(formGroup);

        expect(systemMetric).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISystemMetric should not enable id FormControl', () => {
        const formGroup = service.createSystemMetricFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSystemMetric should disable id FormControl', () => {
        const formGroup = service.createSystemMetricFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
