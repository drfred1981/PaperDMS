import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-folder.test-samples';

import { MetaFolderFormService } from './meta-folder-form.service';

describe('MetaFolder Form Service', () => {
  let service: MetaFolderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaFolderFormService);
  });

  describe('Service methods', () => {
    describe('createMetaFolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaFolderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            path: expect.any(Object),
            isShared: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IMetaFolder should create a new form with FormGroup', () => {
        const formGroup = service.createMetaFolderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            path: expect.any(Object),
            isShared: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetaFolder', () => {
      it('should return NewMetaFolder for default MetaFolder initial value', () => {
        const formGroup = service.createMetaFolderFormGroup(sampleWithNewData);

        const metaFolder = service.getMetaFolder(formGroup) as any;

        expect(metaFolder).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaFolder for empty MetaFolder initial value', () => {
        const formGroup = service.createMetaFolderFormGroup();

        const metaFolder = service.getMetaFolder(formGroup) as any;

        expect(metaFolder).toMatchObject({});
      });

      it('should return IMetaFolder', () => {
        const formGroup = service.createMetaFolderFormGroup(sampleWithRequiredData);

        const metaFolder = service.getMetaFolder(formGroup) as any;

        expect(metaFolder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaFolder should not enable id FormControl', () => {
        const formGroup = service.createMetaFolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaFolder should disable id FormControl', () => {
        const formGroup = service.createMetaFolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
