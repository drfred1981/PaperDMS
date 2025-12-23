import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISearchFacet } from '../search-facet.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../search-facet.test-samples';

import { SearchFacetService } from './search-facet.service';

const requireRestSample: ISearchFacet = {
  ...sampleWithRequiredData,
};

describe('SearchFacet Service', () => {
  let service: SearchFacetService;
  let httpMock: HttpTestingController;
  let expectedResult: ISearchFacet | ISearchFacet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SearchFacetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SearchFacet', () => {
      const searchFacet = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(searchFacet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SearchFacet', () => {
      const searchFacet = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(searchFacet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SearchFacet', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SearchFacet', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SearchFacet', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSearchFacetToCollectionIfMissing', () => {
      it('should add a SearchFacet to an empty array', () => {
        const searchFacet: ISearchFacet = sampleWithRequiredData;
        expectedResult = service.addSearchFacetToCollectionIfMissing([], searchFacet);
        expect(expectedResult).toEqual([searchFacet]);
      });

      it('should not add a SearchFacet to an array that contains it', () => {
        const searchFacet: ISearchFacet = sampleWithRequiredData;
        const searchFacetCollection: ISearchFacet[] = [
          {
            ...searchFacet,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSearchFacetToCollectionIfMissing(searchFacetCollection, searchFacet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SearchFacet to an array that doesn't contain it", () => {
        const searchFacet: ISearchFacet = sampleWithRequiredData;
        const searchFacetCollection: ISearchFacet[] = [sampleWithPartialData];
        expectedResult = service.addSearchFacetToCollectionIfMissing(searchFacetCollection, searchFacet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchFacet);
      });

      it('should add only unique SearchFacet to an array', () => {
        const searchFacetArray: ISearchFacet[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const searchFacetCollection: ISearchFacet[] = [sampleWithRequiredData];
        expectedResult = service.addSearchFacetToCollectionIfMissing(searchFacetCollection, ...searchFacetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const searchFacet: ISearchFacet = sampleWithRequiredData;
        const searchFacet2: ISearchFacet = sampleWithPartialData;
        expectedResult = service.addSearchFacetToCollectionIfMissing([], searchFacet, searchFacet2);
        expect(expectedResult).toEqual([searchFacet, searchFacet2]);
      });

      it('should accept null and undefined values', () => {
        const searchFacet: ISearchFacet = sampleWithRequiredData;
        expectedResult = service.addSearchFacetToCollectionIfMissing([], null, searchFacet, undefined);
        expect(expectedResult).toEqual([searchFacet]);
      });

      it('should return initial array if no SearchFacet is added', () => {
        const searchFacetCollection: ISearchFacet[] = [sampleWithRequiredData];
        expectedResult = service.addSearchFacetToCollectionIfMissing(searchFacetCollection, undefined, null);
        expect(expectedResult).toEqual(searchFacetCollection);
      });
    });

    describe('compareSearchFacet', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSearchFacet(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5218 };
        const entity2 = null;

        const compareResult1 = service.compareSearchFacet(entity1, entity2);
        const compareResult2 = service.compareSearchFacet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5218 };
        const entity2 = { id: 20050 };

        const compareResult1 = service.compareSearchFacet(entity1, entity2);
        const compareResult2 = service.compareSearchFacet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5218 };
        const entity2 = { id: 5218 };

        const compareResult1 = service.compareSearchFacet(entity1, entity2);
        const compareResult2 = service.compareSearchFacet(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
