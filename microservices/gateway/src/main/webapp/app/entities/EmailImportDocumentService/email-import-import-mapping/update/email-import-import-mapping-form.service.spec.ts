import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../email-import-import-mapping.test-samples';

import { EmailImportImportMappingFormService } from './email-import-import-mapping-form.service';

describe('EmailImportImportMapping Form Service', () => {
  let service: EmailImportImportMappingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailImportImportMappingFormService);
  });

  describe('Service methods', () => {
    describe('createEmailImportImportMappingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            emailField: expect.any(Object),
            documentField: expect.any(Object),
            transformation: expect.any(Object),
            transformationConfig: expect.any(Object),
            isRequired: expect.any(Object),
            defaultValue: expect.any(Object),
            validationRegex: expect.any(Object),
            rule: expect.any(Object),
          }),
        );
      });

      it('passing IEmailImportImportMapping should create a new form with FormGroup', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            emailField: expect.any(Object),
            documentField: expect.any(Object),
            transformation: expect.any(Object),
            transformationConfig: expect.any(Object),
            isRequired: expect.any(Object),
            defaultValue: expect.any(Object),
            validationRegex: expect.any(Object),
            rule: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmailImportImportMapping', () => {
      it('should return NewEmailImportImportMapping for default EmailImportImportMapping initial value', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup(sampleWithNewData);

        const emailImportImportMapping = service.getEmailImportImportMapping(formGroup) as any;

        expect(emailImportImportMapping).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmailImportImportMapping for empty EmailImportImportMapping initial value', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup();

        const emailImportImportMapping = service.getEmailImportImportMapping(formGroup) as any;

        expect(emailImportImportMapping).toMatchObject({});
      });

      it('should return IEmailImportImportMapping', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup(sampleWithRequiredData);

        const emailImportImportMapping = service.getEmailImportImportMapping(formGroup) as any;

        expect(emailImportImportMapping).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmailImportImportMapping should not enable id FormControl', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmailImportImportMapping should disable id FormControl', () => {
        const formGroup = service.createEmailImportImportMappingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
