import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../import-rule.test-samples';

import { ImportRuleFormService } from './import-rule-form.service';

describe('ImportRule Form Service', () => {
  let service: ImportRuleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImportRuleFormService);
  });

  describe('Service methods', () => {
    describe('createImportRuleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImportRuleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            isActive: expect.any(Object),
            conditions: expect.any(Object),
            actions: expect.any(Object),
            folderId: expect.any(Object),
            documentTypeId: expect.any(Object),
            applyTags: expect.any(Object),
            notifyUsers: expect.any(Object),
            matchCount: expect.any(Object),
            lastMatchDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IImportRule should create a new form with FormGroup', () => {
        const formGroup = service.createImportRuleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            isActive: expect.any(Object),
            conditions: expect.any(Object),
            actions: expect.any(Object),
            folderId: expect.any(Object),
            documentTypeId: expect.any(Object),
            applyTags: expect.any(Object),
            notifyUsers: expect.any(Object),
            matchCount: expect.any(Object),
            lastMatchDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getImportRule', () => {
      it('should return NewImportRule for default ImportRule initial value', () => {
        const formGroup = service.createImportRuleFormGroup(sampleWithNewData);

        const importRule = service.getImportRule(formGroup) as any;

        expect(importRule).toMatchObject(sampleWithNewData);
      });

      it('should return NewImportRule for empty ImportRule initial value', () => {
        const formGroup = service.createImportRuleFormGroup();

        const importRule = service.getImportRule(formGroup) as any;

        expect(importRule).toMatchObject({});
      });

      it('should return IImportRule', () => {
        const formGroup = service.createImportRuleFormGroup(sampleWithRequiredData);

        const importRule = service.getImportRule(formGroup) as any;

        expect(importRule).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImportRule should not enable id FormControl', () => {
        const formGroup = service.createImportRuleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImportRule should disable id FormControl', () => {
        const formGroup = service.createImportRuleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
