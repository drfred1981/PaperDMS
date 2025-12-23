import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../import-mapping.test-samples';

import { ImportMappingFormService } from './import-mapping-form.service';

describe('ImportMapping Form Service', () => {
  let service: ImportMappingFormService;

  beforeEach(() => {
    service = TestBed.inject(ImportMappingFormService);
  });

  describe('Service methods', () => {
    describe('createImportMappingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImportMappingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ruleId: expect.any(Object),
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

      it('passing IImportMapping should create a new form with FormGroup', () => {
        const formGroup = service.createImportMappingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ruleId: expect.any(Object),
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

    describe('getImportMapping', () => {
      it('should return NewImportMapping for default ImportMapping initial value', () => {
        const formGroup = service.createImportMappingFormGroup(sampleWithNewData);

        const importMapping = service.getImportMapping(formGroup);

        expect(importMapping).toMatchObject(sampleWithNewData);
      });

      it('should return NewImportMapping for empty ImportMapping initial value', () => {
        const formGroup = service.createImportMappingFormGroup();

        const importMapping = service.getImportMapping(formGroup);

        expect(importMapping).toMatchObject({});
      });

      it('should return IImportMapping', () => {
        const formGroup = service.createImportMappingFormGroup(sampleWithRequiredData);

        const importMapping = service.getImportMapping(formGroup);

        expect(importMapping).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImportMapping should not enable id FormControl', () => {
        const formGroup = service.createImportMappingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImportMapping should disable id FormControl', () => {
        const formGroup = service.createImportMappingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
