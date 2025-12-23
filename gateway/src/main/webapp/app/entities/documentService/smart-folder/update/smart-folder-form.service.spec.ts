import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../smart-folder.test-samples';

import { SmartFolderFormService } from './smart-folder-form.service';

describe('SmartFolder Form Service', () => {
  let service: SmartFolderFormService;

  beforeEach(() => {
    service = TestBed.inject(SmartFolderFormService);
  });

  describe('Service methods', () => {
    describe('createSmartFolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSmartFolderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            queryJson: expect.any(Object),
            autoRefresh: expect.any(Object),
            isPublic: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ISmartFolder should create a new form with FormGroup', () => {
        const formGroup = service.createSmartFolderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            queryJson: expect.any(Object),
            autoRefresh: expect.any(Object),
            isPublic: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getSmartFolder', () => {
      it('should return NewSmartFolder for default SmartFolder initial value', () => {
        const formGroup = service.createSmartFolderFormGroup(sampleWithNewData);

        const smartFolder = service.getSmartFolder(formGroup);

        expect(smartFolder).toMatchObject(sampleWithNewData);
      });

      it('should return NewSmartFolder for empty SmartFolder initial value', () => {
        const formGroup = service.createSmartFolderFormGroup();

        const smartFolder = service.getSmartFolder(formGroup);

        expect(smartFolder).toMatchObject({});
      });

      it('should return ISmartFolder', () => {
        const formGroup = service.createSmartFolderFormGroup(sampleWithRequiredData);

        const smartFolder = service.getSmartFolder(formGroup);

        expect(smartFolder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISmartFolder should not enable id FormControl', () => {
        const formGroup = service.createSmartFolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSmartFolder should disable id FormControl', () => {
        const formGroup = service.createSmartFolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
