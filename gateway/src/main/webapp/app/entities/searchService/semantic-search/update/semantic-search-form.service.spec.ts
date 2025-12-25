import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../semantic-search.test-samples';

import { SemanticSearchFormService } from './semantic-search-form.service';

describe('SemanticSearch Form Service', () => {
  let service: SemanticSearchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SemanticSearchFormService);
  });

  describe('Service methods', () => {
    describe('createSemanticSearchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSemanticSearchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            query: expect.any(Object),
            queryEmbedding: expect.any(Object),
            results: expect.any(Object),
            relevanceScores: expect.any(Object),
            modelUsed: expect.any(Object),
            executionTime: expect.any(Object),
            userId: expect.any(Object),
            searchDate: expect.any(Object),
          }),
        );
      });

      it('passing ISemanticSearch should create a new form with FormGroup', () => {
        const formGroup = service.createSemanticSearchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            query: expect.any(Object),
            queryEmbedding: expect.any(Object),
            results: expect.any(Object),
            relevanceScores: expect.any(Object),
            modelUsed: expect.any(Object),
            executionTime: expect.any(Object),
            userId: expect.any(Object),
            searchDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getSemanticSearch', () => {
      it('should return NewSemanticSearch for default SemanticSearch initial value', () => {
        const formGroup = service.createSemanticSearchFormGroup(sampleWithNewData);

        const semanticSearch = service.getSemanticSearch(formGroup) as any;

        expect(semanticSearch).toMatchObject(sampleWithNewData);
      });

      it('should return NewSemanticSearch for empty SemanticSearch initial value', () => {
        const formGroup = service.createSemanticSearchFormGroup();

        const semanticSearch = service.getSemanticSearch(formGroup) as any;

        expect(semanticSearch).toMatchObject({});
      });

      it('should return ISemanticSearch', () => {
        const formGroup = service.createSemanticSearchFormGroup(sampleWithRequiredData);

        const semanticSearch = service.getSemanticSearch(formGroup) as any;

        expect(semanticSearch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISemanticSearch should not enable id FormControl', () => {
        const formGroup = service.createSemanticSearchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSemanticSearch should disable id FormControl', () => {
        const formGroup = service.createSemanticSearchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
