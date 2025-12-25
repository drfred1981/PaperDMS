import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../export-pattern.test-samples';

import { ExportPatternFormService } from './export-pattern-form.service';

describe('ExportPattern Form Service', () => {
  let service: ExportPatternFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExportPatternFormService);
  });

  describe('Service methods', () => {
    describe('createExportPatternFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExportPatternFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            pathTemplate: expect.any(Object),
            fileNameTemplate: expect.any(Object),
            variables: expect.any(Object),
            examples: expect.any(Object),
            isSystem: expect.any(Object),
            isActive: expect.any(Object),
            usageCount: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IExportPattern should create a new form with FormGroup', () => {
        const formGroup = service.createExportPatternFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            pathTemplate: expect.any(Object),
            fileNameTemplate: expect.any(Object),
            variables: expect.any(Object),
            examples: expect.any(Object),
            isSystem: expect.any(Object),
            isActive: expect.any(Object),
            usageCount: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getExportPattern', () => {
      it('should return NewExportPattern for default ExportPattern initial value', () => {
        const formGroup = service.createExportPatternFormGroup(sampleWithNewData);

        const exportPattern = service.getExportPattern(formGroup) as any;

        expect(exportPattern).toMatchObject(sampleWithNewData);
      });

      it('should return NewExportPattern for empty ExportPattern initial value', () => {
        const formGroup = service.createExportPatternFormGroup();

        const exportPattern = service.getExportPattern(formGroup) as any;

        expect(exportPattern).toMatchObject({});
      });

      it('should return IExportPattern', () => {
        const formGroup = service.createExportPatternFormGroup(sampleWithRequiredData);

        const exportPattern = service.getExportPattern(formGroup) as any;

        expect(exportPattern).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExportPattern should not enable id FormControl', () => {
        const formGroup = service.createExportPatternFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExportPattern should disable id FormControl', () => {
        const formGroup = service.createExportPatternFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
