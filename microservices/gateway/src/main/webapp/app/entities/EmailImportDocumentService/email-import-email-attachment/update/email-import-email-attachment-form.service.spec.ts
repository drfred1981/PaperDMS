import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../email-import-email-attachment.test-samples';

import { EmailImportEmailAttachmentFormService } from './email-import-email-attachment-form.service';

describe('EmailImportEmailAttachment Form Service', () => {
  let service: EmailImportEmailAttachmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailImportEmailAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createEmailImportEmailAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileSize: expect.any(Object),
            mimeType: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            documentSha256: expect.any(Object),
            emailImportDocument: expect.any(Object),
          }),
        );
      });

      it('passing IEmailImportEmailAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileSize: expect.any(Object),
            mimeType: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            documentSha256: expect.any(Object),
            emailImportDocument: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmailImportEmailAttachment', () => {
      it('should return NewEmailImportEmailAttachment for default EmailImportEmailAttachment initial value', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup(sampleWithNewData);

        const emailImportEmailAttachment = service.getEmailImportEmailAttachment(formGroup) as any;

        expect(emailImportEmailAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmailImportEmailAttachment for empty EmailImportEmailAttachment initial value', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup();

        const emailImportEmailAttachment = service.getEmailImportEmailAttachment(formGroup) as any;

        expect(emailImportEmailAttachment).toMatchObject({});
      });

      it('should return IEmailImportEmailAttachment', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup(sampleWithRequiredData);

        const emailImportEmailAttachment = service.getEmailImportEmailAttachment(formGroup) as any;

        expect(emailImportEmailAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmailImportEmailAttachment should not enable id FormControl', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmailImportEmailAttachment should disable id FormControl', () => {
        const formGroup = service.createEmailImportEmailAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
