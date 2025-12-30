import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOcrComparison } from '../ocr-comparison.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ocr-comparison.test-samples';

import { OcrComparisonService, RestOcrComparison } from './ocr-comparison.service';

const requireRestSample: RestOcrComparison = {
  ...sampleWithRequiredData,
  selectedDate: sampleWithRequiredData.selectedDate?.toJSON(),
  comparisonDate: sampleWithRequiredData.comparisonDate?.toJSON(),
};

describe('OcrComparison Service', () => {
  let service: OcrComparisonService;
  let httpMock: HttpTestingController;
  let expectedResult: IOcrComparison | IOcrComparison[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OcrComparisonService);
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

    it('should create a OcrComparison', () => {
      const ocrComparison = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ocrComparison).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OcrComparison', () => {
      const ocrComparison = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ocrComparison).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OcrComparison', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OcrComparison', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OcrComparison', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a OcrComparison', () => {
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

    describe('addOcrComparisonToCollectionIfMissing', () => {
      it('should add a OcrComparison to an empty array', () => {
        const ocrComparison: IOcrComparison = sampleWithRequiredData;
        expectedResult = service.addOcrComparisonToCollectionIfMissing([], ocrComparison);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrComparison);
      });

      it('should not add a OcrComparison to an array that contains it', () => {
        const ocrComparison: IOcrComparison = sampleWithRequiredData;
        const ocrComparisonCollection: IOcrComparison[] = [
          {
            ...ocrComparison,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOcrComparisonToCollectionIfMissing(ocrComparisonCollection, ocrComparison);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OcrComparison to an array that doesn't contain it", () => {
        const ocrComparison: IOcrComparison = sampleWithRequiredData;
        const ocrComparisonCollection: IOcrComparison[] = [sampleWithPartialData];
        expectedResult = service.addOcrComparisonToCollectionIfMissing(ocrComparisonCollection, ocrComparison);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrComparison);
      });

      it('should add only unique OcrComparison to an array', () => {
        const ocrComparisonArray: IOcrComparison[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ocrComparisonCollection: IOcrComparison[] = [sampleWithRequiredData];
        expectedResult = service.addOcrComparisonToCollectionIfMissing(ocrComparisonCollection, ...ocrComparisonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ocrComparison: IOcrComparison = sampleWithRequiredData;
        const ocrComparison2: IOcrComparison = sampleWithPartialData;
        expectedResult = service.addOcrComparisonToCollectionIfMissing([], ocrComparison, ocrComparison2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrComparison);
        expect(expectedResult).toContain(ocrComparison2);
      });

      it('should accept null and undefined values', () => {
        const ocrComparison: IOcrComparison = sampleWithRequiredData;
        expectedResult = service.addOcrComparisonToCollectionIfMissing([], null, ocrComparison, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrComparison);
      });

      it('should return initial array if no OcrComparison is added', () => {
        const ocrComparisonCollection: IOcrComparison[] = [sampleWithRequiredData];
        expectedResult = service.addOcrComparisonToCollectionIfMissing(ocrComparisonCollection, undefined, null);
        expect(expectedResult).toEqual(ocrComparisonCollection);
      });
    });

    describe('compareOcrComparison', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOcrComparison(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31164 };
        const entity2 = null;

        const compareResult1 = service.compareOcrComparison(entity1, entity2);
        const compareResult2 = service.compareOcrComparison(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31164 };
        const entity2 = { id: 21003 };

        const compareResult1 = service.compareOcrComparison(entity1, entity2);
        const compareResult2 = service.compareOcrComparison(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31164 };
        const entity2 = { id: 31164 };

        const compareResult1 = service.compareOcrComparison(entity1, entity2);
        const compareResult2 = service.compareOcrComparison(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
