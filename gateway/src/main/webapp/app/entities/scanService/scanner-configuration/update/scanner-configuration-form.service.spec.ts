import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scanner-configuration.test-samples';

import { ScannerConfigurationFormService } from './scanner-configuration-form.service';

describe('ScannerConfiguration Form Service', () => {
  let service: ScannerConfigurationFormService;

  beforeEach(() => {
    service = TestBed.inject(ScannerConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createScannerConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScannerConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            scannerType: expect.any(Object),
            ipAddress: expect.any(Object),
            port: expect.any(Object),
            protocol: expect.any(Object),
            manufacturer: expect.any(Object),
            model: expect.any(Object),
            defaultColorMode: expect.any(Object),
            defaultResolution: expect.any(Object),
            defaultFormat: expect.any(Object),
            capabilities: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IScannerConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createScannerConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            scannerType: expect.any(Object),
            ipAddress: expect.any(Object),
            port: expect.any(Object),
            protocol: expect.any(Object),
            manufacturer: expect.any(Object),
            model: expect.any(Object),
            defaultColorMode: expect.any(Object),
            defaultResolution: expect.any(Object),
            defaultFormat: expect.any(Object),
            capabilities: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getScannerConfiguration', () => {
      it('should return NewScannerConfiguration for default ScannerConfiguration initial value', () => {
        const formGroup = service.createScannerConfigurationFormGroup(sampleWithNewData);

        const scannerConfiguration = service.getScannerConfiguration(formGroup);

        expect(scannerConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewScannerConfiguration for empty ScannerConfiguration initial value', () => {
        const formGroup = service.createScannerConfigurationFormGroup();

        const scannerConfiguration = service.getScannerConfiguration(formGroup);

        expect(scannerConfiguration).toMatchObject({});
      });

      it('should return IScannerConfiguration', () => {
        const formGroup = service.createScannerConfigurationFormGroup(sampleWithRequiredData);

        const scannerConfiguration = service.getScannerConfiguration(formGroup);

        expect(scannerConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScannerConfiguration should not enable id FormControl', () => {
        const formGroup = service.createScannerConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScannerConfiguration should disable id FormControl', () => {
        const formGroup = service.createScannerConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
