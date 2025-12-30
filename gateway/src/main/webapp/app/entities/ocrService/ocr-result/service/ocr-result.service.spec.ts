import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOcrResult } from '../ocr-result.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ocr-result.test-samples';

import { OcrResultService, RestOcrResult } from './ocr-result.service';

const requireRestSample: RestOcrResult = {
  ...sampleWithRequiredData,
  processedDate: sampleWithRequiredData.processedDate?.toJSON(),
};

describe('OcrResult Service', () => {
  let service: OcrResultService;
  let httpMock: HttpTestingController;
  let expectedResult: IOcrResult | IOcrResult[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OcrResultService);
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

    it('should create a OcrResult', () => {
      const ocrResult = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ocrResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OcrResult', () => {
      const ocrResult = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ocrResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OcrResult', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OcrResult', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OcrResult', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a OcrResult', () => {
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

    describe('addOcrResultToCollectionIfMissing', () => {
      it('should add a OcrResult to an empty array', () => {
        const ocrResult: IOcrResult = sampleWithRequiredData;
        expectedResult = service.addOcrResultToCollectionIfMissing([], ocrResult);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrResult);
      });

      it('should not add a OcrResult to an array that contains it', () => {
        const ocrResult: IOcrResult = sampleWithRequiredData;
        const ocrResultCollection: IOcrResult[] = [
          {
            ...ocrResult,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOcrResultToCollectionIfMissing(ocrResultCollection, ocrResult);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OcrResult to an array that doesn't contain it", () => {
        const ocrResult: IOcrResult = sampleWithRequiredData;
        const ocrResultCollection: IOcrResult[] = [sampleWithPartialData];
        expectedResult = service.addOcrResultToCollectionIfMissing(ocrResultCollection, ocrResult);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrResult);
      });

      it('should add only unique OcrResult to an array', () => {
        const ocrResultArray: IOcrResult[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ocrResultCollection: IOcrResult[] = [sampleWithRequiredData];
        expectedResult = service.addOcrResultToCollectionIfMissing(ocrResultCollection, ...ocrResultArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ocrResult: IOcrResult = sampleWithRequiredData;
        const ocrResult2: IOcrResult = sampleWithPartialData;
        expectedResult = service.addOcrResultToCollectionIfMissing([], ocrResult, ocrResult2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrResult);
        expect(expectedResult).toContain(ocrResult2);
      });

      it('should accept null and undefined values', () => {
        const ocrResult: IOcrResult = sampleWithRequiredData;
        expectedResult = service.addOcrResultToCollectionIfMissing([], null, ocrResult, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrResult);
      });

      it('should return initial array if no OcrResult is added', () => {
        const ocrResultCollection: IOcrResult[] = [sampleWithRequiredData];
        expectedResult = service.addOcrResultToCollectionIfMissing(ocrResultCollection, undefined, null);
        expect(expectedResult).toEqual(ocrResultCollection);
      });
    });

    describe('compareOcrResult', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOcrResult(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27853 };
        const entity2 = null;

        const compareResult1 = service.compareOcrResult(entity1, entity2);
        const compareResult2 = service.compareOcrResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27853 };
        const entity2 = { id: 13914 };

        const compareResult1 = service.compareOcrResult(entity1, entity2);
        const compareResult2 = service.compareOcrResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27853 };
        const entity2 = { id: 27853 };

        const compareResult1 = service.compareOcrResult(entity1, entity2);
        const compareResult2 = service.compareOcrResult(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
