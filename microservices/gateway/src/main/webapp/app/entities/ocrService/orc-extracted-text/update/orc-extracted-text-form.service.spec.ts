import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../orc-extracted-text.test-samples';

import { OrcExtractedTextFormService } from './orc-extracted-text-form.service';

describe('OrcExtractedText Form Service', () => {
  let service: OrcExtractedTextFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrcExtractedTextFormService);
  });

  describe('Service methods', () => {
    describe('createOrcExtractedTextFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrcExtractedTextFormGroup();

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

      it('passing IOrcExtractedText should create a new form with FormGroup', () => {
        const formGroup = service.createOrcExtractedTextFormGroup(sampleWithRequiredData);

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

    describe('getOrcExtractedText', () => {
      it('should return NewOrcExtractedText for default OrcExtractedText initial value', () => {
        const formGroup = service.createOrcExtractedTextFormGroup(sampleWithNewData);

        const orcExtractedText = service.getOrcExtractedText(formGroup) as any;

        expect(orcExtractedText).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrcExtractedText for empty OrcExtractedText initial value', () => {
        const formGroup = service.createOrcExtractedTextFormGroup();

        const orcExtractedText = service.getOrcExtractedText(formGroup) as any;

        expect(orcExtractedText).toMatchObject({});
      });

      it('should return IOrcExtractedText', () => {
        const formGroup = service.createOrcExtractedTextFormGroup(sampleWithRequiredData);

        const orcExtractedText = service.getOrcExtractedText(formGroup) as any;

        expect(orcExtractedText).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrcExtractedText should not enable id FormControl', () => {
        const formGroup = service.createOrcExtractedTextFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrcExtractedText should disable id FormControl', () => {
        const formGroup = service.createOrcExtractedTextFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
