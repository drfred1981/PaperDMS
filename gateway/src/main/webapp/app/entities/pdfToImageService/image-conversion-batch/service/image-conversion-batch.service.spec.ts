import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImageConversionBatch } from '../image-conversion-batch.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../image-conversion-batch.test-samples';

import { ImageConversionBatchService, RestImageConversionBatch } from './image-conversion-batch.service';

const requireRestSample: RestImageConversionBatch = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  startedAt: sampleWithRequiredData.startedAt?.toJSON(),
  completedAt: sampleWithRequiredData.completedAt?.toJSON(),
};

describe('ImageConversionBatch Service', () => {
  let service: ImageConversionBatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageConversionBatch | IImageConversionBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageConversionBatchService);
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

    it('should create a ImageConversionBatch', () => {
      const imageConversionBatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageConversionBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageConversionBatch', () => {
      const imageConversionBatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageConversionBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageConversionBatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageConversionBatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageConversionBatch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ImageConversionBatch', () => {
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

    describe('addImageConversionBatchToCollectionIfMissing', () => {
      it('should add a ImageConversionBatch to an empty array', () => {
        const imageConversionBatch: IImageConversionBatch = sampleWithRequiredData;
        expectedResult = service.addImageConversionBatchToCollectionIfMissing([], imageConversionBatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionBatch);
      });

      it('should not add a ImageConversionBatch to an array that contains it', () => {
        const imageConversionBatch: IImageConversionBatch = sampleWithRequiredData;
        const imageConversionBatchCollection: IImageConversionBatch[] = [
          {
            ...imageConversionBatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageConversionBatchToCollectionIfMissing(imageConversionBatchCollection, imageConversionBatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageConversionBatch to an array that doesn't contain it", () => {
        const imageConversionBatch: IImageConversionBatch = sampleWithRequiredData;
        const imageConversionBatchCollection: IImageConversionBatch[] = [sampleWithPartialData];
        expectedResult = service.addImageConversionBatchToCollectionIfMissing(imageConversionBatchCollection, imageConversionBatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionBatch);
      });

      it('should add only unique ImageConversionBatch to an array', () => {
        const imageConversionBatchArray: IImageConversionBatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageConversionBatchCollection: IImageConversionBatch[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionBatchToCollectionIfMissing(imageConversionBatchCollection, ...imageConversionBatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageConversionBatch: IImageConversionBatch = sampleWithRequiredData;
        const imageConversionBatch2: IImageConversionBatch = sampleWithPartialData;
        expectedResult = service.addImageConversionBatchToCollectionIfMissing([], imageConversionBatch, imageConversionBatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionBatch);
        expect(expectedResult).toContain(imageConversionBatch2);
      });

      it('should accept null and undefined values', () => {
        const imageConversionBatch: IImageConversionBatch = sampleWithRequiredData;
        expectedResult = service.addImageConversionBatchToCollectionIfMissing([], null, imageConversionBatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionBatch);
      });

      it('should return initial array if no ImageConversionBatch is added', () => {
        const imageConversionBatchCollection: IImageConversionBatch[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionBatchToCollectionIfMissing(imageConversionBatchCollection, undefined, null);
        expect(expectedResult).toEqual(imageConversionBatchCollection);
      });
    });

    describe('compareImageConversionBatch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageConversionBatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31426 };
        const entity2 = null;

        const compareResult1 = service.compareImageConversionBatch(entity1, entity2);
        const compareResult2 = service.compareImageConversionBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31426 };
        const entity2 = { id: 12076 };

        const compareResult1 = service.compareImageConversionBatch(entity1, entity2);
        const compareResult2 = service.compareImageConversionBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31426 };
        const entity2 = { id: 31426 };

        const compareResult1 = service.compareImageConversionBatch(entity1, entity2);
        const compareResult2 = service.compareImageConversionBatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
