import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reporting-system-metric.test-samples';

import { ReportingSystemMetricFormService } from './reporting-system-metric-form.service';

describe('ReportingSystemMetric Form Service', () => {
  let service: ReportingSystemMetricFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingSystemMetricFormService);
  });

  describe('Service methods', () => {
    describe('createReportingSystemMetricFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingSystemMetricFormGroup();

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

      it('passing IReportingSystemMetric should create a new form with FormGroup', () => {
        const formGroup = service.createReportingSystemMetricFormGroup(sampleWithRequiredData);

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

    describe('getReportingSystemMetric', () => {
      it('should return NewReportingSystemMetric for default ReportingSystemMetric initial value', () => {
        const formGroup = service.createReportingSystemMetricFormGroup(sampleWithNewData);

        const reportingSystemMetric = service.getReportingSystemMetric(formGroup) as any;

        expect(reportingSystemMetric).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportingSystemMetric for empty ReportingSystemMetric initial value', () => {
        const formGroup = service.createReportingSystemMetricFormGroup();

        const reportingSystemMetric = service.getReportingSystemMetric(formGroup) as any;

        expect(reportingSystemMetric).toMatchObject({});
      });

      it('should return IReportingSystemMetric', () => {
        const formGroup = service.createReportingSystemMetricFormGroup(sampleWithRequiredData);

        const reportingSystemMetric = service.getReportingSystemMetric(formGroup) as any;

        expect(reportingSystemMetric).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportingSystemMetric should not enable id FormControl', () => {
        const formGroup = service.createReportingSystemMetricFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportingSystemMetric should disable id FormControl', () => {
        const formGroup = service.createReportingSystemMetricFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
