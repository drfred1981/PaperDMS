import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImageConversionHistory } from '../image-conversion-history.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../image-conversion-history.test-samples';

import { ImageConversionHistoryService, RestImageConversionHistory } from './image-conversion-history.service';

const requireRestSample: RestImageConversionHistory = {
  ...sampleWithRequiredData,
  archivedAt: sampleWithRequiredData.archivedAt?.toJSON(),
};

describe('ImageConversionHistory Service', () => {
  let service: ImageConversionHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageConversionHistory | IImageConversionHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageConversionHistoryService);
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

    it('should create a ImageConversionHistory', () => {
      const imageConversionHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageConversionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageConversionHistory', () => {
      const imageConversionHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageConversionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageConversionHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageConversionHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageConversionHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ImageConversionHistory', () => {
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

    describe('addImageConversionHistoryToCollectionIfMissing', () => {
      it('should add a ImageConversionHistory to an empty array', () => {
        const imageConversionHistory: IImageConversionHistory = sampleWithRequiredData;
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing([], imageConversionHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionHistory);
      });

      it('should not add a ImageConversionHistory to an array that contains it', () => {
        const imageConversionHistory: IImageConversionHistory = sampleWithRequiredData;
        const imageConversionHistoryCollection: IImageConversionHistory[] = [
          {
            ...imageConversionHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing(imageConversionHistoryCollection, imageConversionHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageConversionHistory to an array that doesn't contain it", () => {
        const imageConversionHistory: IImageConversionHistory = sampleWithRequiredData;
        const imageConversionHistoryCollection: IImageConversionHistory[] = [sampleWithPartialData];
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing(imageConversionHistoryCollection, imageConversionHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionHistory);
      });

      it('should add only unique ImageConversionHistory to an array', () => {
        const imageConversionHistoryArray: IImageConversionHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageConversionHistoryCollection: IImageConversionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing(
          imageConversionHistoryCollection,
          ...imageConversionHistoryArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageConversionHistory: IImageConversionHistory = sampleWithRequiredData;
        const imageConversionHistory2: IImageConversionHistory = sampleWithPartialData;
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing([], imageConversionHistory, imageConversionHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionHistory);
        expect(expectedResult).toContain(imageConversionHistory2);
      });

      it('should accept null and undefined values', () => {
        const imageConversionHistory: IImageConversionHistory = sampleWithRequiredData;
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing([], null, imageConversionHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionHistory);
      });

      it('should return initial array if no ImageConversionHistory is added', () => {
        const imageConversionHistoryCollection: IImageConversionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionHistoryToCollectionIfMissing(imageConversionHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(imageConversionHistoryCollection);
      });
    });

    describe('compareImageConversionHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageConversionHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7139 };
        const entity2 = null;

        const compareResult1 = service.compareImageConversionHistory(entity1, entity2);
        const compareResult2 = service.compareImageConversionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7139 };
        const entity2 = { id: 8118 };

        const compareResult1 = service.compareImageConversionHistory(entity1, entity2);
        const compareResult2 = service.compareImageConversionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7139 };
        const entity2 = { id: 7139 };

        const compareResult1 = service.compareImageConversionHistory(entity1, entity2);
        const compareResult2 = service.compareImageConversionHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
