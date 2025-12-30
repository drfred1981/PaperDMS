import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISearchQuery } from '../search-query.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../search-query.test-samples';

import { RestSearchQuery, SearchQueryService } from './search-query.service';

const requireRestSample: RestSearchQuery = {
  ...sampleWithRequiredData,
  searchDate: sampleWithRequiredData.searchDate?.toJSON(),
};

describe('SearchQuery Service', () => {
  let service: SearchQueryService;
  let httpMock: HttpTestingController;
  let expectedResult: ISearchQuery | ISearchQuery[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SearchQueryService);
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

    it('should create a SearchQuery', () => {
      const searchQuery = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(searchQuery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SearchQuery', () => {
      const searchQuery = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(searchQuery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SearchQuery', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SearchQuery', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SearchQuery', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SearchQuery', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addSearchQueryToCollectionIfMissing', () => {
      it('should add a SearchQuery to an empty array', () => {
        const searchQuery: ISearchQuery = sampleWithRequiredData;
        expectedResult = service.addSearchQueryToCollectionIfMissing([], searchQuery);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchQuery);
      });

      it('should not add a SearchQuery to an array that contains it', () => {
        const searchQuery: ISearchQuery = sampleWithRequiredData;
        const searchQueryCollection: ISearchQuery[] = [
          {
            ...searchQuery,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSearchQueryToCollectionIfMissing(searchQueryCollection, searchQuery);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SearchQuery to an array that doesn't contain it", () => {
        const searchQuery: ISearchQuery = sampleWithRequiredData;
        const searchQueryCollection: ISearchQuery[] = [sampleWithPartialData];
        expectedResult = service.addSearchQueryToCollectionIfMissing(searchQueryCollection, searchQuery);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchQuery);
      });

      it('should add only unique SearchQuery to an array', () => {
        const searchQueryArray: ISearchQuery[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const searchQueryCollection: ISearchQuery[] = [sampleWithRequiredData];
        expectedResult = service.addSearchQueryToCollectionIfMissing(searchQueryCollection, ...searchQueryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const searchQuery: ISearchQuery = sampleWithRequiredData;
        const searchQuery2: ISearchQuery = sampleWithPartialData;
        expectedResult = service.addSearchQueryToCollectionIfMissing([], searchQuery, searchQuery2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchQuery);
        expect(expectedResult).toContain(searchQuery2);
      });

      it('should accept null and undefined values', () => {
        const searchQuery: ISearchQuery = sampleWithRequiredData;
        expectedResult = service.addSearchQueryToCollectionIfMissing([], null, searchQuery, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchQuery);
      });

      it('should return initial array if no SearchQuery is added', () => {
        const searchQueryCollection: ISearchQuery[] = [sampleWithRequiredData];
        expectedResult = service.addSearchQueryToCollectionIfMissing(searchQueryCollection, undefined, null);
        expect(expectedResult).toEqual(searchQueryCollection);
      });
    });

    describe('compareSearchQuery', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSearchQuery(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14684 };
        const entity2 = null;

        const compareResult1 = service.compareSearchQuery(entity1, entity2);
        const compareResult2 = service.compareSearchQuery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14684 };
        const entity2 = { id: 23738 };

        const compareResult1 = service.compareSearchQuery(entity1, entity2);
        const compareResult2 = service.compareSearchQuery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14684 };
        const entity2 = { id: 14684 };

        const compareResult1 = service.compareSearchQuery(entity1, entity2);
        const compareResult2 = service.compareSearchQuery(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
