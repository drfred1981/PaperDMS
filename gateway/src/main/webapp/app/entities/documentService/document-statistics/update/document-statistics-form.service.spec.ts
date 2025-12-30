import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-statistics.test-samples';

import { DocumentStatisticsFormService } from './document-statistics-form.service';

describe('DocumentStatistics Form Service', () => {
  let service: DocumentStatisticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentStatisticsFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentStatisticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentStatisticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            viewsTotal: expect.any(Object),
            downloadsTotal: expect.any(Object),
            uniqueViewers: expect.any(Object),
            lastUpdated: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentStatistics should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentStatisticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            viewsTotal: expect.any(Object),
            downloadsTotal: expect.any(Object),
            uniqueViewers: expect.any(Object),
            lastUpdated: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentStatistics', () => {
      it('should return NewDocumentStatistics for default DocumentStatistics initial value', () => {
        const formGroup = service.createDocumentStatisticsFormGroup(sampleWithNewData);

        const documentStatistics = service.getDocumentStatistics(formGroup) as any;

        expect(documentStatistics).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentStatistics for empty DocumentStatistics initial value', () => {
        const formGroup = service.createDocumentStatisticsFormGroup();

        const documentStatistics = service.getDocumentStatistics(formGroup) as any;

        expect(documentStatistics).toMatchObject({});
      });

      it('should return IDocumentStatistics', () => {
        const formGroup = service.createDocumentStatisticsFormGroup(sampleWithRequiredData);

        const documentStatistics = service.getDocumentStatistics(formGroup) as any;

        expect(documentStatistics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentStatistics should not enable id FormControl', () => {
        const formGroup = service.createDocumentStatisticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentStatistics should disable id FormControl', () => {
        const formGroup = service.createDocumentStatisticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
