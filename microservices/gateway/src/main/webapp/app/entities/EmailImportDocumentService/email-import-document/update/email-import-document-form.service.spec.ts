import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../email-import-document.test-samples';

import { EmailImportDocumentFormService } from './email-import-document-form.service';

describe('EmailImportDocument Form Service', () => {
  let service: EmailImportDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailImportDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createEmailImportDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmailImportDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sha256: expect.any(Object),
            fromEmail: expect.any(Object),
            toEmail: expect.any(Object),
            subject: expect.any(Object),
            body: expect.any(Object),
            bodyHtml: expect.any(Object),
            receivedDate: expect.any(Object),
            processedDate: expect.any(Object),
            status: expect.any(Object),
            attachmentCount: expect.any(Object),
            documentsCreated: expect.any(Object),
            errorMessage: expect.any(Object),
            metadata: expect.any(Object),
            documentSha256: expect.any(Object),
            appliedRule: expect.any(Object),
          }),
        );
      });

      it('passing IEmailImportDocument should create a new form with FormGroup', () => {
        const formGroup = service.createEmailImportDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sha256: expect.any(Object),
            fromEmail: expect.any(Object),
            toEmail: expect.any(Object),
            subject: expect.any(Object),
            body: expect.any(Object),
            bodyHtml: expect.any(Object),
            receivedDate: expect.any(Object),
            processedDate: expect.any(Object),
            status: expect.any(Object),
            attachmentCount: expect.any(Object),
            documentsCreated: expect.any(Object),
            errorMessage: expect.any(Object),
            metadata: expect.any(Object),
            documentSha256: expect.any(Object),
            appliedRule: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmailImportDocument', () => {
      it('should return NewEmailImportDocument for default EmailImportDocument initial value', () => {
        const formGroup = service.createEmailImportDocumentFormGroup(sampleWithNewData);

        const emailImportDocument = service.getEmailImportDocument(formGroup) as any;

        expect(emailImportDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmailImportDocument for empty EmailImportDocument initial value', () => {
        const formGroup = service.createEmailImportDocumentFormGroup();

        const emailImportDocument = service.getEmailImportDocument(formGroup) as any;

        expect(emailImportDocument).toMatchObject({});
      });

      it('should return IEmailImportDocument', () => {
        const formGroup = service.createEmailImportDocumentFormGroup(sampleWithRequiredData);

        const emailImportDocument = service.getEmailImportDocument(formGroup) as any;

        expect(emailImportDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmailImportDocument should not enable id FormControl', () => {
        const formGroup = service.createEmailImportDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmailImportDocument should disable id FormControl', () => {
        const formGroup = service.createEmailImportDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
