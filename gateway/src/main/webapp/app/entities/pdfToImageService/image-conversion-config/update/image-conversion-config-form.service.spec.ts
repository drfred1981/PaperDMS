import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-conversion-config.test-samples';

import { ImageConversionConfigFormService } from './image-conversion-config-form.service';

describe('ImageConversionConfig Form Service', () => {
  let service: ImageConversionConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageConversionConfigFormService);
  });

  describe('Service methods', () => {
    describe('createImageConversionConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageConversionConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            configName: expect.any(Object),
            description: expect.any(Object),
            defaultQuality: expect.any(Object),
            defaultFormat: expect.any(Object),
            defaultDpi: expect.any(Object),
            defaultConversionType: expect.any(Object),
            defaultPriority: expect.any(Object),
            isActive: expect.any(Object),
            isDefault: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IImageConversionConfig should create a new form with FormGroup', () => {
        const formGroup = service.createImageConversionConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            configName: expect.any(Object),
            description: expect.any(Object),
            defaultQuality: expect.any(Object),
            defaultFormat: expect.any(Object),
            defaultDpi: expect.any(Object),
            defaultConversionType: expect.any(Object),
            defaultPriority: expect.any(Object),
            isActive: expect.any(Object),
            isDefault: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getImageConversionConfig', () => {
      it('should return NewImageConversionConfig for default ImageConversionConfig initial value', () => {
        const formGroup = service.createImageConversionConfigFormGroup(sampleWithNewData);

        const imageConversionConfig = service.getImageConversionConfig(formGroup) as any;

        expect(imageConversionConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageConversionConfig for empty ImageConversionConfig initial value', () => {
        const formGroup = service.createImageConversionConfigFormGroup();

        const imageConversionConfig = service.getImageConversionConfig(formGroup) as any;

        expect(imageConversionConfig).toMatchObject({});
      });

      it('should return IImageConversionConfig', () => {
        const formGroup = service.createImageConversionConfigFormGroup(sampleWithRequiredData);

        const imageConversionConfig = service.getImageConversionConfig(formGroup) as any;

        expect(imageConversionConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageConversionConfig should not enable id FormControl', () => {
        const formGroup = service.createImageConversionConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageConversionConfig should disable id FormControl', () => {
        const formGroup = service.createImageConversionConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
