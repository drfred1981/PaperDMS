import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../system-health.test-samples';

import { SystemHealthFormService } from './system-health-form.service';

describe('SystemHealth Form Service', () => {
  let service: SystemHealthFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SystemHealthFormService);
  });

  describe('Service methods', () => {
    describe('createSystemHealthFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSystemHealthFormGroup();

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

      it('passing ISystemHealth should create a new form with FormGroup', () => {
        const formGroup = service.createSystemHealthFormGroup(sampleWithRequiredData);

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

    describe('getSystemHealth', () => {
      it('should return NewSystemHealth for default SystemHealth initial value', () => {
        const formGroup = service.createSystemHealthFormGroup(sampleWithNewData);

        const systemHealth = service.getSystemHealth(formGroup) as any;

        expect(systemHealth).toMatchObject(sampleWithNewData);
      });

      it('should return NewSystemHealth for empty SystemHealth initial value', () => {
        const formGroup = service.createSystemHealthFormGroup();

        const systemHealth = service.getSystemHealth(formGroup) as any;

        expect(systemHealth).toMatchObject({});
      });

      it('should return ISystemHealth', () => {
        const formGroup = service.createSystemHealthFormGroup(sampleWithRequiredData);

        const systemHealth = service.getSystemHealth(formGroup) as any;

        expect(systemHealth).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISystemHealth should not enable id FormControl', () => {
        const formGroup = service.createSystemHealthFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSystemHealth should disable id FormControl', () => {
        const formGroup = service.createSystemHealthFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
