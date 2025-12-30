import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-smart-folder.test-samples';

import { MetaSmartFolderFormService } from './meta-smart-folder-form.service';

describe('MetaSmartFolder Form Service', () => {
  let service: MetaSmartFolderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaSmartFolderFormService);
  });

  describe('Service methods', () => {
    describe('createMetaSmartFolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaSmartFolderFormGroup();

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

      it('passing IMetaSmartFolder should create a new form with FormGroup', () => {
        const formGroup = service.createMetaSmartFolderFormGroup(sampleWithRequiredData);

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

    describe('getMetaSmartFolder', () => {
      it('should return NewMetaSmartFolder for default MetaSmartFolder initial value', () => {
        const formGroup = service.createMetaSmartFolderFormGroup(sampleWithNewData);

        const metaSmartFolder = service.getMetaSmartFolder(formGroup) as any;

        expect(metaSmartFolder).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaSmartFolder for empty MetaSmartFolder initial value', () => {
        const formGroup = service.createMetaSmartFolderFormGroup();

        const metaSmartFolder = service.getMetaSmartFolder(formGroup) as any;

        expect(metaSmartFolder).toMatchObject({});
      });

      it('should return IMetaSmartFolder', () => {
        const formGroup = service.createMetaSmartFolderFormGroup(sampleWithRequiredData);

        const metaSmartFolder = service.getMetaSmartFolder(formGroup) as any;

        expect(metaSmartFolder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaSmartFolder should not enable id FormControl', () => {
        const formGroup = service.createMetaSmartFolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaSmartFolder should disable id FormControl', () => {
        const formGroup = service.createMetaSmartFolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
