import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingSystemMetric } from '../reporting-system-metric.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../reporting-system-metric.test-samples';

import { ReportingSystemMetricService, RestReportingSystemMetric } from './reporting-system-metric.service';

const requireRestSample: RestReportingSystemMetric = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('ReportingSystemMetric Service', () => {
  let service: ReportingSystemMetricService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingSystemMetric | IReportingSystemMetric[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingSystemMetricService);
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

    it('should create a ReportingSystemMetric', () => {
      const reportingSystemMetric = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingSystemMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingSystemMetric', () => {
      const reportingSystemMetric = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingSystemMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingSystemMetric', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingSystemMetric', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingSystemMetric', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingSystemMetric', () => {
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

    describe('addReportingSystemMetricToCollectionIfMissing', () => {
      it('should add a ReportingSystemMetric to an empty array', () => {
        const reportingSystemMetric: IReportingSystemMetric = sampleWithRequiredData;
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing([], reportingSystemMetric);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingSystemMetric);
      });

      it('should not add a ReportingSystemMetric to an array that contains it', () => {
        const reportingSystemMetric: IReportingSystemMetric = sampleWithRequiredData;
        const reportingSystemMetricCollection: IReportingSystemMetric[] = [
          {
            ...reportingSystemMetric,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing(reportingSystemMetricCollection, reportingSystemMetric);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingSystemMetric to an array that doesn't contain it", () => {
        const reportingSystemMetric: IReportingSystemMetric = sampleWithRequiredData;
        const reportingSystemMetricCollection: IReportingSystemMetric[] = [sampleWithPartialData];
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing(reportingSystemMetricCollection, reportingSystemMetric);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingSystemMetric);
      });

      it('should add only unique ReportingSystemMetric to an array', () => {
        const reportingSystemMetricArray: IReportingSystemMetric[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportingSystemMetricCollection: IReportingSystemMetric[] = [sampleWithRequiredData];
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing(
          reportingSystemMetricCollection,
          ...reportingSystemMetricArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingSystemMetric: IReportingSystemMetric = sampleWithRequiredData;
        const reportingSystemMetric2: IReportingSystemMetric = sampleWithPartialData;
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing([], reportingSystemMetric, reportingSystemMetric2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingSystemMetric);
        expect(expectedResult).toContain(reportingSystemMetric2);
      });

      it('should accept null and undefined values', () => {
        const reportingSystemMetric: IReportingSystemMetric = sampleWithRequiredData;
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing([], null, reportingSystemMetric, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingSystemMetric);
      });

      it('should return initial array if no ReportingSystemMetric is added', () => {
        const reportingSystemMetricCollection: IReportingSystemMetric[] = [sampleWithRequiredData];
        expectedResult = service.addReportingSystemMetricToCollectionIfMissing(reportingSystemMetricCollection, undefined, null);
        expect(expectedResult).toEqual(reportingSystemMetricCollection);
      });
    });

    describe('compareReportingSystemMetric', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingSystemMetric(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23948 };
        const entity2 = null;

        const compareResult1 = service.compareReportingSystemMetric(entity1, entity2);
        const compareResult2 = service.compareReportingSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23948 };
        const entity2 = { id: 3362 };

        const compareResult1 = service.compareReportingSystemMetric(entity1, entity2);
        const compareResult2 = service.compareReportingSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23948 };
        const entity2 = { id: 23948 };

        const compareResult1 = service.compareReportingSystemMetric(entity1, entity2);
        const compareResult2 = service.compareReportingSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
