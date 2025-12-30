import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../email-import-import-rule.test-samples';

import { EmailImportImportRuleFormService } from './email-import-import-rule-form.service';

describe('EmailImportImportRule Form Service', () => {
  let service: EmailImportImportRuleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailImportImportRuleFormService);
  });

  describe('Service methods', () => {
    describe('createEmailImportImportRuleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            isActive: expect.any(Object),
            conditions: expect.any(Object),
            actions: expect.any(Object),
            notifyUsers: expect.any(Object),
            matchCount: expect.any(Object),
            lastMatchDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IEmailImportImportRule should create a new form with FormGroup', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            isActive: expect.any(Object),
            conditions: expect.any(Object),
            actions: expect.any(Object),
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

    describe('getEmailImportImportRule', () => {
      it('should return NewEmailImportImportRule for default EmailImportImportRule initial value', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup(sampleWithNewData);

        const emailImportImportRule = service.getEmailImportImportRule(formGroup) as any;

        expect(emailImportImportRule).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmailImportImportRule for empty EmailImportImportRule initial value', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup();

        const emailImportImportRule = service.getEmailImportImportRule(formGroup) as any;

        expect(emailImportImportRule).toMatchObject({});
      });

      it('should return IEmailImportImportRule', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup(sampleWithRequiredData);

        const emailImportImportRule = service.getEmailImportImportRule(formGroup) as any;

        expect(emailImportImportRule).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmailImportImportRule should not enable id FormControl', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmailImportImportRule should disable id FormControl', () => {
        const formGroup = service.createEmailImportImportRuleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
