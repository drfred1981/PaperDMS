import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-status.test-samples';

import { ServiceStatusFormService } from './service-status-form.service';

describe('ServiceStatus Form Service', () => {
  let service: ServiceStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceStatusFormService);
  });

  describe('Service methods', () => {
    describe('createServiceStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceStatusFormGroup();

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

      it('passing IServiceStatus should create a new form with FormGroup', () => {
        const formGroup = service.createServiceStatusFormGroup(sampleWithRequiredData);

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

    describe('getServiceStatus', () => {
      it('should return NewServiceStatus for default ServiceStatus initial value', () => {
        const formGroup = service.createServiceStatusFormGroup(sampleWithNewData);

        const serviceStatus = service.getServiceStatus(formGroup) as any;

        expect(serviceStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceStatus for empty ServiceStatus initial value', () => {
        const formGroup = service.createServiceStatusFormGroup();

        const serviceStatus = service.getServiceStatus(formGroup) as any;

        expect(serviceStatus).toMatchObject({});
      });

      it('should return IServiceStatus', () => {
        const formGroup = service.createServiceStatusFormGroup(sampleWithRequiredData);

        const serviceStatus = service.getServiceStatus(formGroup) as any;

        expect(serviceStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceStatus should not enable id FormControl', () => {
        const formGroup = service.createServiceStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceStatus should disable id FormControl', () => {
        const formGroup = service.createServiceStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
