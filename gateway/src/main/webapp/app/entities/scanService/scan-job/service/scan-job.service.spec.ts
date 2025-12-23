import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScanJob } from '../scan-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../scan-job.test-samples';

import { RestScanJob, ScanJobService } from './scan-job.service';

const requireRestSample: RestScanJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ScanJob Service', () => {
  let service: ScanJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IScanJob | IScanJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScanJobService);
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

    it('should create a ScanJob', () => {
      const scanJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scanJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScanJob', () => {
      const scanJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scanJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScanJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScanJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScanJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScanJobToCollectionIfMissing', () => {
      it('should add a ScanJob to an empty array', () => {
        const scanJob: IScanJob = sampleWithRequiredData;
        expectedResult = service.addScanJobToCollectionIfMissing([], scanJob);
        expect(expectedResult).toEqual([scanJob]);
      });

      it('should not add a ScanJob to an array that contains it', () => {
        const scanJob: IScanJob = sampleWithRequiredData;
        const scanJobCollection: IScanJob[] = [
          {
            ...scanJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScanJobToCollectionIfMissing(scanJobCollection, scanJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScanJob to an array that doesn't contain it", () => {
        const scanJob: IScanJob = sampleWithRequiredData;
        const scanJobCollection: IScanJob[] = [sampleWithPartialData];
        expectedResult = service.addScanJobToCollectionIfMissing(scanJobCollection, scanJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scanJob);
      });

      it('should add only unique ScanJob to an array', () => {
        const scanJobArray: IScanJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scanJobCollection: IScanJob[] = [sampleWithRequiredData];
        expectedResult = service.addScanJobToCollectionIfMissing(scanJobCollection, ...scanJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scanJob: IScanJob = sampleWithRequiredData;
        const scanJob2: IScanJob = sampleWithPartialData;
        expectedResult = service.addScanJobToCollectionIfMissing([], scanJob, scanJob2);
        expect(expectedResult).toEqual([scanJob, scanJob2]);
      });

      it('should accept null and undefined values', () => {
        const scanJob: IScanJob = sampleWithRequiredData;
        expectedResult = service.addScanJobToCollectionIfMissing([], null, scanJob, undefined);
        expect(expectedResult).toEqual([scanJob]);
      });

      it('should return initial array if no ScanJob is added', () => {
        const scanJobCollection: IScanJob[] = [sampleWithRequiredData];
        expectedResult = service.addScanJobToCollectionIfMissing(scanJobCollection, undefined, null);
        expect(expectedResult).toEqual(scanJobCollection);
      });
    });

    describe('compareScanJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScanJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3321 };
        const entity2 = null;

        const compareResult1 = service.compareScanJob(entity1, entity2);
        const compareResult2 = service.compareScanJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3321 };
        const entity2 = { id: 5501 };

        const compareResult1 = service.compareScanJob(entity1, entity2);
        const compareResult2 = service.compareScanJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3321 };
        const entity2 = { id: 3321 };

        const compareResult1 = service.compareScanJob(entity1, entity2);
        const compareResult2 = service.compareScanJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
