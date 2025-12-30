import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../search-semantic.test-samples';

import { SearchSemanticFormService } from './search-semantic-form.service';

describe('SearchSemantic Form Service', () => {
  let service: SearchSemanticFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchSemanticFormService);
  });

  describe('Service methods', () => {
    describe('createSearchSemanticFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSearchSemanticFormGroup();

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

      it('passing ISearchSemantic should create a new form with FormGroup', () => {
        const formGroup = service.createSearchSemanticFormGroup(sampleWithRequiredData);

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

    describe('getSearchSemantic', () => {
      it('should return NewSearchSemantic for default SearchSemantic initial value', () => {
        const formGroup = service.createSearchSemanticFormGroup(sampleWithNewData);

        const searchSemantic = service.getSearchSemantic(formGroup) as any;

        expect(searchSemantic).toMatchObject(sampleWithNewData);
      });

      it('should return NewSearchSemantic for empty SearchSemantic initial value', () => {
        const formGroup = service.createSearchSemanticFormGroup();

        const searchSemantic = service.getSearchSemantic(formGroup) as any;

        expect(searchSemantic).toMatchObject({});
      });

      it('should return ISearchSemantic', () => {
        const formGroup = service.createSearchSemanticFormGroup(sampleWithRequiredData);

        const searchSemantic = service.getSearchSemantic(formGroup) as any;

        expect(searchSemantic).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISearchSemantic should not enable id FormControl', () => {
        const formGroup = service.createSearchSemanticFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSearchSemantic should disable id FormControl', () => {
        const formGroup = service.createSearchSemanticFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
