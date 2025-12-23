import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../performance-metric.test-samples';

import { PerformanceMetricFormService } from './performance-metric-form.service';

describe('PerformanceMetric Form Service', () => {
  let service: PerformanceMetricFormService;

  beforeEach(() => {
    service = TestBed.inject(PerformanceMetricFormService);
  });

  describe('Service methods', () => {
    describe('createPerformanceMetricFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPerformanceMetricFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metricName: expect.any(Object),
            metricType: expect.any(Object),
            value: expect.any(Object),
            unit: expect.any(Object),
            serviceName: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });

      it('passing IPerformanceMetric should create a new form with FormGroup', () => {
        const formGroup = service.createPerformanceMetricFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metricName: expect.any(Object),
            metricType: expect.any(Object),
            value: expect.any(Object),
            unit: expect.any(Object),
            serviceName: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });
    });

    describe('getPerformanceMetric', () => {
      it('should return NewPerformanceMetric for default PerformanceMetric initial value', () => {
        const formGroup = service.createPerformanceMetricFormGroup(sampleWithNewData);

        const performanceMetric = service.getPerformanceMetric(formGroup);

        expect(performanceMetric).toMatchObject(sampleWithNewData);
      });

      it('should return NewPerformanceMetric for empty PerformanceMetric initial value', () => {
        const formGroup = service.createPerformanceMetricFormGroup();

        const performanceMetric = service.getPerformanceMetric(formGroup);

        expect(performanceMetric).toMatchObject({});
      });

      it('should return IPerformanceMetric', () => {
        const formGroup = service.createPerformanceMetricFormGroup(sampleWithRequiredData);

        const performanceMetric = service.getPerformanceMetric(formGroup);

        expect(performanceMetric).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPerformanceMetric should not enable id FormControl', () => {
        const formGroup = service.createPerformanceMetricFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPerformanceMetric should disable id FormControl', () => {
        const formGroup = service.createPerformanceMetricFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
