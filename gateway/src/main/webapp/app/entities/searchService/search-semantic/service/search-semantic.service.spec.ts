import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISearchSemantic } from '../search-semantic.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../search-semantic.test-samples';

import { RestSearchSemantic, SearchSemanticService } from './search-semantic.service';

const requireRestSample: RestSearchSemantic = {
  ...sampleWithRequiredData,
  searchDate: sampleWithRequiredData.searchDate?.toJSON(),
};

describe('SearchSemantic Service', () => {
  let service: SearchSemanticService;
  let httpMock: HttpTestingController;
  let expectedResult: ISearchSemantic | ISearchSemantic[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SearchSemanticService);
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

    it('should create a SearchSemantic', () => {
      const searchSemantic = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(searchSemantic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SearchSemantic', () => {
      const searchSemantic = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(searchSemantic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SearchSemantic', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SearchSemantic', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SearchSemantic', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SearchSemantic', () => {
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

    describe('addSearchSemanticToCollectionIfMissing', () => {
      it('should add a SearchSemantic to an empty array', () => {
        const searchSemantic: ISearchSemantic = sampleWithRequiredData;
        expectedResult = service.addSearchSemanticToCollectionIfMissing([], searchSemantic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchSemantic);
      });

      it('should not add a SearchSemantic to an array that contains it', () => {
        const searchSemantic: ISearchSemantic = sampleWithRequiredData;
        const searchSemanticCollection: ISearchSemantic[] = [
          {
            ...searchSemantic,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSearchSemanticToCollectionIfMissing(searchSemanticCollection, searchSemantic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SearchSemantic to an array that doesn't contain it", () => {
        const searchSemantic: ISearchSemantic = sampleWithRequiredData;
        const searchSemanticCollection: ISearchSemantic[] = [sampleWithPartialData];
        expectedResult = service.addSearchSemanticToCollectionIfMissing(searchSemanticCollection, searchSemantic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchSemantic);
      });

      it('should add only unique SearchSemantic to an array', () => {
        const searchSemanticArray: ISearchSemantic[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const searchSemanticCollection: ISearchSemantic[] = [sampleWithRequiredData];
        expectedResult = service.addSearchSemanticToCollectionIfMissing(searchSemanticCollection, ...searchSemanticArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const searchSemantic: ISearchSemantic = sampleWithRequiredData;
        const searchSemantic2: ISearchSemantic = sampleWithPartialData;
        expectedResult = service.addSearchSemanticToCollectionIfMissing([], searchSemantic, searchSemantic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchSemantic);
        expect(expectedResult).toContain(searchSemantic2);
      });

      it('should accept null and undefined values', () => {
        const searchSemantic: ISearchSemantic = sampleWithRequiredData;
        expectedResult = service.addSearchSemanticToCollectionIfMissing([], null, searchSemantic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchSemantic);
      });

      it('should return initial array if no SearchSemantic is added', () => {
        const searchSemanticCollection: ISearchSemantic[] = [sampleWithRequiredData];
        expectedResult = service.addSearchSemanticToCollectionIfMissing(searchSemanticCollection, undefined, null);
        expect(expectedResult).toEqual(searchSemanticCollection);
      });
    });

    describe('compareSearchSemantic', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSearchSemantic(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4712 };
        const entity2 = null;

        const compareResult1 = service.compareSearchSemantic(entity1, entity2);
        const compareResult2 = service.compareSearchSemantic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4712 };
        const entity2 = { id: 9131 };

        const compareResult1 = service.compareSearchSemantic(entity1, entity2);
        const compareResult2 = service.compareSearchSemantic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4712 };
        const entity2 = { id: 4712 };

        const compareResult1 = service.compareSearchSemantic(entity1, entity2);
        const compareResult2 = service.compareSearchSemantic(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
