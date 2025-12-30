import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../reporting-performance-metric.test-samples';

import { ReportingPerformanceMetricService, RestReportingPerformanceMetric } from './reporting-performance-metric.service';

const requireRestSample: RestReportingPerformanceMetric = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('ReportingPerformanceMetric Service', () => {
  let service: ReportingPerformanceMetricService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingPerformanceMetric | IReportingPerformanceMetric[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingPerformanceMetricService);
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

    it('should create a ReportingPerformanceMetric', () => {
      const reportingPerformanceMetric = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingPerformanceMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingPerformanceMetric', () => {
      const reportingPerformanceMetric = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingPerformanceMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingPerformanceMetric', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingPerformanceMetric', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingPerformanceMetric', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingPerformanceMetric', () => {
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

    describe('addReportingPerformanceMetricToCollectionIfMissing', () => {
      it('should add a ReportingPerformanceMetric to an empty array', () => {
        const reportingPerformanceMetric: IReportingPerformanceMetric = sampleWithRequiredData;
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing([], reportingPerformanceMetric);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingPerformanceMetric);
      });

      it('should not add a ReportingPerformanceMetric to an array that contains it', () => {
        const reportingPerformanceMetric: IReportingPerformanceMetric = sampleWithRequiredData;
        const reportingPerformanceMetricCollection: IReportingPerformanceMetric[] = [
          {
            ...reportingPerformanceMetric,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing(
          reportingPerformanceMetricCollection,
          reportingPerformanceMetric,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingPerformanceMetric to an array that doesn't contain it", () => {
        const reportingPerformanceMetric: IReportingPerformanceMetric = sampleWithRequiredData;
        const reportingPerformanceMetricCollection: IReportingPerformanceMetric[] = [sampleWithPartialData];
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing(
          reportingPerformanceMetricCollection,
          reportingPerformanceMetric,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingPerformanceMetric);
      });

      it('should add only unique ReportingPerformanceMetric to an array', () => {
        const reportingPerformanceMetricArray: IReportingPerformanceMetric[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const reportingPerformanceMetricCollection: IReportingPerformanceMetric[] = [sampleWithRequiredData];
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing(
          reportingPerformanceMetricCollection,
          ...reportingPerformanceMetricArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingPerformanceMetric: IReportingPerformanceMetric = sampleWithRequiredData;
        const reportingPerformanceMetric2: IReportingPerformanceMetric = sampleWithPartialData;
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing(
          [],
          reportingPerformanceMetric,
          reportingPerformanceMetric2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingPerformanceMetric);
        expect(expectedResult).toContain(reportingPerformanceMetric2);
      });

      it('should accept null and undefined values', () => {
        const reportingPerformanceMetric: IReportingPerformanceMetric = sampleWithRequiredData;
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing([], null, reportingPerformanceMetric, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingPerformanceMetric);
      });

      it('should return initial array if no ReportingPerformanceMetric is added', () => {
        const reportingPerformanceMetricCollection: IReportingPerformanceMetric[] = [sampleWithRequiredData];
        expectedResult = service.addReportingPerformanceMetricToCollectionIfMissing(reportingPerformanceMetricCollection, undefined, null);
        expect(expectedResult).toEqual(reportingPerformanceMetricCollection);
      });
    });

    describe('compareReportingPerformanceMetric', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingPerformanceMetric(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32088 };
        const entity2 = null;

        const compareResult1 = service.compareReportingPerformanceMetric(entity1, entity2);
        const compareResult2 = service.compareReportingPerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32088 };
        const entity2 = { id: 15464 };

        const compareResult1 = service.compareReportingPerformanceMetric(entity1, entity2);
        const compareResult2 = service.compareReportingPerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32088 };
        const entity2 = { id: 32088 };

        const compareResult1 = service.compareReportingPerformanceMetric(entity1, entity2);
        const compareResult2 = service.compareReportingPerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
