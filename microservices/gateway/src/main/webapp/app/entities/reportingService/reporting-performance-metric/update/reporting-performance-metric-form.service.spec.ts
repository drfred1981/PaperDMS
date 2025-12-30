import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-performance-metric.test-samples';

import { ReportingPerformanceMetricFormService } from './reporting-performance-metric-form.service';

describe('ReportingPerformanceMetric Form Service', () => {
  let service: ReportingPerformanceMetricFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingPerformanceMetricFormService);
  });

  describe('Service methods', () => {
    describe('createReportingPerformanceMetricFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup();

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

      it('passing IReportingPerformanceMetric should create a new form with FormGroup', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup(sampleWithRequiredData);

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

    describe('getReportingPerformanceMetric', () => {
      it('should return NewReportingPerformanceMetric for default ReportingPerformanceMetric initial value', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup(sampleWithNewData);

        const reportingPerformanceMetric = service.getReportingPerformanceMetric(formGroup) as any;

        expect(reportingPerformanceMetric).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingPerformanceMetric for empty ReportingPerformanceMetric initial value', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup();

        const reportingPerformanceMetric = service.getReportingPerformanceMetric(formGroup) as any;

        expect(reportingPerformanceMetric).toMatchObject({});
      });

      it('should return IReportingPerformanceMetric', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup(sampleWithRequiredData);

        const reportingPerformanceMetric = service.getReportingPerformanceMetric(formGroup) as any;

        expect(reportingPerformanceMetric).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingPerformanceMetric should not enable id FormControl', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingPerformanceMetric should disable id FormControl', () => {
        const formGroup = service.createReportingPerformanceMetricFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
