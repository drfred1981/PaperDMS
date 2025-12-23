import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../email-import.test-samples';

import { EmailImportFormService } from './email-import-form.service';

describe('EmailImport Form Service', () => {
  let service: EmailImportFormService;

  beforeEach(() => {
    service = TestBed.inject(EmailImportFormService);
  });

  describe('Service methods', () => {
    describe('createEmailImportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmailImportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromEmail: expect.any(Object),
            toEmail: expect.any(Object),
            subject: expect.any(Object),
            body: expect.any(Object),
            bodyHtml: expect.any(Object),
            receivedDate: expect.any(Object),
            processedDate: expect.any(Object),
            status: expect.any(Object),
            folderId: expect.any(Object),
            documentTypeId: expect.any(Object),
            attachmentCount: expect.any(Object),
            documentsCreated: expect.any(Object),
            appliedRuleId: expect.any(Object),
            errorMessage: expect.any(Object),
            metadata: expect.any(Object),
            appliedRule: expect.any(Object),
          }),
        );
      });

      it('passing IEmailImport should create a new form with FormGroup', () => {
        const formGroup = service.createEmailImportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromEmail: expect.any(Object),
            toEmail: expect.any(Object),
            subject: expect.any(Object),
            body: expect.any(Object),
            bodyHtml: expect.any(Object),
            receivedDate: expect.any(Object),
            processedDate: expect.any(Object),
            status: expect.any(Object),
            folderId: expect.any(Object),
            documentTypeId: expect.any(Object),
            attachmentCount: expect.any(Object),
            documentsCreated: expect.any(Object),
            appliedRuleId: expect.any(Object),
            errorMessage: expect.any(Object),
            metadata: expect.any(Object),
            appliedRule: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmailImport', () => {
      it('should return NewEmailImport for default EmailImport initial value', () => {
        const formGroup = service.createEmailImportFormGroup(sampleWithNewData);

        const emailImport = service.getEmailImport(formGroup);

        expect(emailImport).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmailImport for empty EmailImport initial value', () => {
        const formGroup = service.createEmailImportFormGroup();

        const emailImport = service.getEmailImport(formGroup);

        expect(emailImport).toMatchObject({});
      });

      it('should return IEmailImport', () => {
        const formGroup = service.createEmailImportFormGroup(sampleWithRequiredData);

        const emailImport = service.getEmailImport(formGroup);

        expect(emailImport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmailImport should not enable id FormControl', () => {
        const formGroup = service.createEmailImportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmailImport should disable id FormControl', () => {
        const formGroup = service.createEmailImportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
