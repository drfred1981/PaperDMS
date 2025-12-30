import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAICache } from '../ai-cache.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ai-cache.test-samples';

import { AICacheService, RestAICache } from './ai-cache.service';

const requireRestSample: RestAICache = {
  ...sampleWithRequiredData,
  lastAccessDate: sampleWithRequiredData.lastAccessDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  expirationDate: sampleWithRequiredData.expirationDate?.toJSON(),
};

describe('AICache Service', () => {
  let service: AICacheService;
  let httpMock: HttpTestingController;
  let expectedResult: IAICache | IAICache[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AICacheService);
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

    it('should create a AICache', () => {
      const aICache = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aICache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AICache', () => {
      const aICache = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aICache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AICache', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AICache', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AICache', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AICache', () => {
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

    describe('addAICacheToCollectionIfMissing', () => {
      it('should add a AICache to an empty array', () => {
        const aICache: IAICache = sampleWithRequiredData;
        expectedResult = service.addAICacheToCollectionIfMissing([], aICache);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aICache);
      });

      it('should not add a AICache to an array that contains it', () => {
        const aICache: IAICache = sampleWithRequiredData;
        const aICacheCollection: IAICache[] = [
          {
            ...aICache,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAICacheToCollectionIfMissing(aICacheCollection, aICache);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AICache to an array that doesn't contain it", () => {
        const aICache: IAICache = sampleWithRequiredData;
        const aICacheCollection: IAICache[] = [sampleWithPartialData];
        expectedResult = service.addAICacheToCollectionIfMissing(aICacheCollection, aICache);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aICache);
      });

      it('should add only unique AICache to an array', () => {
        const aICacheArray: IAICache[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aICacheCollection: IAICache[] = [sampleWithRequiredData];
        expectedResult = service.addAICacheToCollectionIfMissing(aICacheCollection, ...aICacheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aICache: IAICache = sampleWithRequiredData;
        const aICache2: IAICache = sampleWithPartialData;
        expectedResult = service.addAICacheToCollectionIfMissing([], aICache, aICache2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aICache);
        expect(expectedResult).toContain(aICache2);
      });

      it('should accept null and undefined values', () => {
        const aICache: IAICache = sampleWithRequiredData;
        expectedResult = service.addAICacheToCollectionIfMissing([], null, aICache, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aICache);
      });

      it('should return initial array if no AICache is added', () => {
        const aICacheCollection: IAICache[] = [sampleWithRequiredData];
        expectedResult = service.addAICacheToCollectionIfMissing(aICacheCollection, undefined, null);
        expect(expectedResult).toEqual(aICacheCollection);
      });
    });

    describe('compareAICache', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAICache(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29414 };
        const entity2 = null;

        const compareResult1 = service.compareAICache(entity1, entity2);
        const compareResult2 = service.compareAICache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29414 };
        const entity2 = { id: 16798 };

        const compareResult1 = service.compareAICache(entity1, entity2);
        const compareResult2 = service.compareAICache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29414 };
        const entity2 = { id: 29414 };

        const compareResult1 = service.compareAICache(entity1, entity2);
        const compareResult2 = service.compareAICache(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
