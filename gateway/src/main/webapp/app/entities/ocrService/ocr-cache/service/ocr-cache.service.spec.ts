import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOcrCache } from '../ocr-cache.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ocr-cache.test-samples';

import { OcrCacheService, RestOcrCache } from './ocr-cache.service';

const requireRestSample: RestOcrCache = {
  ...sampleWithRequiredData,
  lastAccessDate: sampleWithRequiredData.lastAccessDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  expirationDate: sampleWithRequiredData.expirationDate?.toJSON(),
};

describe('OcrCache Service', () => {
  let service: OcrCacheService;
  let httpMock: HttpTestingController;
  let expectedResult: IOcrCache | IOcrCache[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OcrCacheService);
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

    it('should create a OcrCache', () => {
      const ocrCache = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ocrCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OcrCache', () => {
      const ocrCache = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ocrCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OcrCache', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OcrCache', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OcrCache', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOcrCacheToCollectionIfMissing', () => {
      it('should add a OcrCache to an empty array', () => {
        const ocrCache: IOcrCache = sampleWithRequiredData;
        expectedResult = service.addOcrCacheToCollectionIfMissing([], ocrCache);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrCache);
      });

      it('should not add a OcrCache to an array that contains it', () => {
        const ocrCache: IOcrCache = sampleWithRequiredData;
        const ocrCacheCollection: IOcrCache[] = [
          {
            ...ocrCache,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOcrCacheToCollectionIfMissing(ocrCacheCollection, ocrCache);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OcrCache to an array that doesn't contain it", () => {
        const ocrCache: IOcrCache = sampleWithRequiredData;
        const ocrCacheCollection: IOcrCache[] = [sampleWithPartialData];
        expectedResult = service.addOcrCacheToCollectionIfMissing(ocrCacheCollection, ocrCache);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrCache);
      });

      it('should add only unique OcrCache to an array', () => {
        const ocrCacheArray: IOcrCache[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ocrCacheCollection: IOcrCache[] = [sampleWithRequiredData];
        expectedResult = service.addOcrCacheToCollectionIfMissing(ocrCacheCollection, ...ocrCacheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ocrCache: IOcrCache = sampleWithRequiredData;
        const ocrCache2: IOcrCache = sampleWithPartialData;
        expectedResult = service.addOcrCacheToCollectionIfMissing([], ocrCache, ocrCache2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrCache);
        expect(expectedResult).toContain(ocrCache2);
      });

      it('should accept null and undefined values', () => {
        const ocrCache: IOcrCache = sampleWithRequiredData;
        expectedResult = service.addOcrCacheToCollectionIfMissing([], null, ocrCache, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrCache);
      });

      it('should return initial array if no OcrCache is added', () => {
        const ocrCacheCollection: IOcrCache[] = [sampleWithRequiredData];
        expectedResult = service.addOcrCacheToCollectionIfMissing(ocrCacheCollection, undefined, null);
        expect(expectedResult).toEqual(ocrCacheCollection);
      });
    });

    describe('compareOcrCache', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOcrCache(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3253 };
        const entity2 = null;

        const compareResult1 = service.compareOcrCache(entity1, entity2);
        const compareResult2 = service.compareOcrCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3253 };
        const entity2 = { id: 11186 };

        const compareResult1 = service.compareOcrCache(entity1, entity2);
        const compareResult2 = service.compareOcrCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3253 };
        const entity2 = { id: 3253 };

        const compareResult1 = service.compareOcrCache(entity1, entity2);
        const compareResult2 = service.compareOcrCache(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
