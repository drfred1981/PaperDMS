import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingScheduledReport } from '../reporting-scheduled-report.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../reporting-scheduled-report.test-samples';

import { ReportingScheduledReportService, RestReportingScheduledReport } from './reporting-scheduled-report.service';

const requireRestSample: RestReportingScheduledReport = {
  ...sampleWithRequiredData,
  lastRun: sampleWithRequiredData.lastRun?.toJSON(),
  nextRun: sampleWithRequiredData.nextRun?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ReportingScheduledReport Service', () => {
  let service: ReportingScheduledReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingScheduledReport | IReportingScheduledReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingScheduledReportService);
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

    it('should create a ReportingScheduledReport', () => {
      const reportingScheduledReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingScheduledReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingScheduledReport', () => {
      const reportingScheduledReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingScheduledReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingScheduledReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingScheduledReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingScheduledReport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingScheduledReport', () => {
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

    describe('addReportingScheduledReportToCollectionIfMissing', () => {
      it('should add a ReportingScheduledReport to an empty array', () => {
        const reportingScheduledReport: IReportingScheduledReport = sampleWithRequiredData;
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing([], reportingScheduledReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingScheduledReport);
      });

      it('should not add a ReportingScheduledReport to an array that contains it', () => {
        const reportingScheduledReport: IReportingScheduledReport = sampleWithRequiredData;
        const reportingScheduledReportCollection: IReportingScheduledReport[] = [
          {
            ...reportingScheduledReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing(
          reportingScheduledReportCollection,
          reportingScheduledReport,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingScheduledReport to an array that doesn't contain it", () => {
        const reportingScheduledReport: IReportingScheduledReport = sampleWithRequiredData;
        const reportingScheduledReportCollection: IReportingScheduledReport[] = [sampleWithPartialData];
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing(
          reportingScheduledReportCollection,
          reportingScheduledReport,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingScheduledReport);
      });

      it('should add only unique ReportingScheduledReport to an array', () => {
        const reportingScheduledReportArray: IReportingScheduledReport[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const reportingScheduledReportCollection: IReportingScheduledReport[] = [sampleWithRequiredData];
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing(
          reportingScheduledReportCollection,
          ...reportingScheduledReportArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingScheduledReport: IReportingScheduledReport = sampleWithRequiredData;
        const reportingScheduledReport2: IReportingScheduledReport = sampleWithPartialData;
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing([], reportingScheduledReport, reportingScheduledReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingScheduledReport);
        expect(expectedResult).toContain(reportingScheduledReport2);
      });

      it('should accept null and undefined values', () => {
        const reportingScheduledReport: IReportingScheduledReport = sampleWithRequiredData;
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing([], null, reportingScheduledReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingScheduledReport);
      });

      it('should return initial array if no ReportingScheduledReport is added', () => {
        const reportingScheduledReportCollection: IReportingScheduledReport[] = [sampleWithRequiredData];
        expectedResult = service.addReportingScheduledReportToCollectionIfMissing(reportingScheduledReportCollection, undefined, null);
        expect(expectedResult).toEqual(reportingScheduledReportCollection);
      });
    });

    describe('compareReportingScheduledReport', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingScheduledReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25144 };
        const entity2 = null;

        const compareResult1 = service.compareReportingScheduledReport(entity1, entity2);
        const compareResult2 = service.compareReportingScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25144 };
        const entity2 = { id: 16309 };

        const compareResult1 = service.compareReportingScheduledReport(entity1, entity2);
        const compareResult2 = service.compareReportingScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25144 };
        const entity2 = { id: 25144 };

        const compareResult1 = service.compareReportingScheduledReport(entity1, entity2);
        const compareResult2 = service.compareReportingScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
