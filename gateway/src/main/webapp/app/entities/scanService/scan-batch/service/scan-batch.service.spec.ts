import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScanBatch } from '../scan-batch.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../scan-batch.test-samples';

import { RestScanBatch, ScanBatchService } from './scan-batch.service';

const requireRestSample: RestScanBatch = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ScanBatch Service', () => {
  let service: ScanBatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IScanBatch | IScanBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScanBatchService);
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

    it('should create a ScanBatch', () => {
      const scanBatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scanBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScanBatch', () => {
      const scanBatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scanBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScanBatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScanBatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScanBatch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScanBatchToCollectionIfMissing', () => {
      it('should add a ScanBatch to an empty array', () => {
        const scanBatch: IScanBatch = sampleWithRequiredData;
        expectedResult = service.addScanBatchToCollectionIfMissing([], scanBatch);
        expect(expectedResult).toEqual([scanBatch]);
      });

      it('should not add a ScanBatch to an array that contains it', () => {
        const scanBatch: IScanBatch = sampleWithRequiredData;
        const scanBatchCollection: IScanBatch[] = [
          {
            ...scanBatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScanBatchToCollectionIfMissing(scanBatchCollection, scanBatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScanBatch to an array that doesn't contain it", () => {
        const scanBatch: IScanBatch = sampleWithRequiredData;
        const scanBatchCollection: IScanBatch[] = [sampleWithPartialData];
        expectedResult = service.addScanBatchToCollectionIfMissing(scanBatchCollection, scanBatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scanBatch);
      });

      it('should add only unique ScanBatch to an array', () => {
        const scanBatchArray: IScanBatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scanBatchCollection: IScanBatch[] = [sampleWithRequiredData];
        expectedResult = service.addScanBatchToCollectionIfMissing(scanBatchCollection, ...scanBatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scanBatch: IScanBatch = sampleWithRequiredData;
        const scanBatch2: IScanBatch = sampleWithPartialData;
        expectedResult = service.addScanBatchToCollectionIfMissing([], scanBatch, scanBatch2);
        expect(expectedResult).toEqual([scanBatch, scanBatch2]);
      });

      it('should accept null and undefined values', () => {
        const scanBatch: IScanBatch = sampleWithRequiredData;
        expectedResult = service.addScanBatchToCollectionIfMissing([], null, scanBatch, undefined);
        expect(expectedResult).toEqual([scanBatch]);
      });

      it('should return initial array if no ScanBatch is added', () => {
        const scanBatchCollection: IScanBatch[] = [sampleWithRequiredData];
        expectedResult = service.addScanBatchToCollectionIfMissing(scanBatchCollection, undefined, null);
        expect(expectedResult).toEqual(scanBatchCollection);
      });
    });

    describe('compareScanBatch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScanBatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9534 };
        const entity2 = null;

        const compareResult1 = service.compareScanBatch(entity1, entity2);
        const compareResult2 = service.compareScanBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9534 };
        const entity2 = { id: 11487 };

        const compareResult1 = service.compareScanBatch(entity1, entity2);
        const compareResult2 = service.compareScanBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9534 };
        const entity2 = { id: 9534 };

        const compareResult1 = service.compareScanBatch(entity1, entity2);
        const compareResult2 = service.compareScanBatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
