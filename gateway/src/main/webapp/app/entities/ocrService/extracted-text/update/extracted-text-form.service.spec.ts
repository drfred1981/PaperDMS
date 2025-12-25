import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../extracted-text.test-samples';

import { ExtractedTextFormService } from './extracted-text-form.service';

describe('ExtractedText Form Service', () => {
  let service: ExtractedTextFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExtractedTextFormService);
  });

  describe('Service methods', () => {
    describe('createExtractedTextFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtractedTextFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            contentSha256: expect.any(Object),
            s3ContentKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            pageNumber: expect.any(Object),
            language: expect.any(Object),
            wordCount: expect.any(Object),
            hasStructuredData: expect.any(Object),
            structuredData: expect.any(Object),
            structuredDataS3Key: expect.any(Object),
            extractedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });

      it('passing IExtractedText should create a new form with FormGroup', () => {
        const formGroup = service.createExtractedTextFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            contentSha256: expect.any(Object),
            s3ContentKey: expect.any(Object),
            s3Bucket: expect.any(Object),
            pageNumber: expect.any(Object),
            language: expect.any(Object),
            wordCount: expect.any(Object),
            hasStructuredData: expect.any(Object),
            structuredData: expect.any(Object),
            structuredDataS3Key: expect.any(Object),
            extractedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });
    });

    describe('getExtractedText', () => {
      it('should return NewExtractedText for default ExtractedText initial value', () => {
        const formGroup = service.createExtractedTextFormGroup(sampleWithNewData);

        const extractedText = service.getExtractedText(formGroup) as any;

        expect(extractedText).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtractedText for empty ExtractedText initial value', () => {
        const formGroup = service.createExtractedTextFormGroup();

        const extractedText = service.getExtractedText(formGroup) as any;

        expect(extractedText).toMatchObject({});
      });

      it('should return IExtractedText', () => {
        const formGroup = service.createExtractedTextFormGroup(sampleWithRequiredData);

        const extractedText = service.getExtractedText(formGroup) as any;

        expect(extractedText).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtractedText should not enable id FormControl', () => {
        const formGroup = service.createExtractedTextFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtractedText should disable id FormControl', () => {
        const formGroup = service.createExtractedTextFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
