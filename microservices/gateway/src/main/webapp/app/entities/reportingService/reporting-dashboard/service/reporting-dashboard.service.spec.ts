import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingDashboard } from '../reporting-dashboard.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reporting-dashboard.test-samples';

import { ReportingDashboardService, RestReportingDashboard } from './reporting-dashboard.service';

const requireRestSample: RestReportingDashboard = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ReportingDashboard Service', () => {
  let service: ReportingDashboardService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingDashboard | IReportingDashboard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingDashboardService);
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

    it('should create a ReportingDashboard', () => {
      const reportingDashboard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingDashboard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingDashboard', () => {
      const reportingDashboard = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingDashboard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingDashboard', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingDashboard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingDashboard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingDashboard', () => {
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

    describe('addReportingDashboardToCollectionIfMissing', () => {
      it('should add a ReportingDashboard to an empty array', () => {
        const reportingDashboard: IReportingDashboard = sampleWithRequiredData;
        expectedResult = service.addReportingDashboardToCollectionIfMissing([], reportingDashboard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingDashboard);
      });

      it('should not add a ReportingDashboard to an array that contains it', () => {
        const reportingDashboard: IReportingDashboard = sampleWithRequiredData;
        const reportingDashboardCollection: IReportingDashboard[] = [
          {
            ...reportingDashboard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingDashboardToCollectionIfMissing(reportingDashboardCollection, reportingDashboard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingDashboard to an array that doesn't contain it", () => {
        const reportingDashboard: IReportingDashboard = sampleWithRequiredData;
        const reportingDashboardCollection: IReportingDashboard[] = [sampleWithPartialData];
        expectedResult = service.addReportingDashboardToCollectionIfMissing(reportingDashboardCollection, reportingDashboard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingDashboard);
      });

      it('should add only unique ReportingDashboard to an array', () => {
        const reportingDashboardArray: IReportingDashboard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportingDashboardCollection: IReportingDashboard[] = [sampleWithRequiredData];
        expectedResult = service.addReportingDashboardToCollectionIfMissing(reportingDashboardCollection, ...reportingDashboardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingDashboard: IReportingDashboard = sampleWithRequiredData;
        const reportingDashboard2: IReportingDashboard = sampleWithPartialData;
        expectedResult = service.addReportingDashboardToCollectionIfMissing([], reportingDashboard, reportingDashboard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingDashboard);
        expect(expectedResult).toContain(reportingDashboard2);
      });

      it('should accept null and undefined values', () => {
        const reportingDashboard: IReportingDashboard = sampleWithRequiredData;
        expectedResult = service.addReportingDashboardToCollectionIfMissing([], null, reportingDashboard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingDashboard);
      });

      it('should return initial array if no ReportingDashboard is added', () => {
        const reportingDashboardCollection: IReportingDashboard[] = [sampleWithRequiredData];
        expectedResult = service.addReportingDashboardToCollectionIfMissing(reportingDashboardCollection, undefined, null);
        expect(expectedResult).toEqual(reportingDashboardCollection);
      });
    });

    describe('compareReportingDashboard', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingDashboard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5841 };
        const entity2 = null;

        const compareResult1 = service.compareReportingDashboard(entity1, entity2);
        const compareResult2 = service.compareReportingDashboard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5841 };
        const entity2 = { id: 30933 };

        const compareResult1 = service.compareReportingDashboard(entity1, entity2);
        const compareResult2 = service.compareReportingDashboard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5841 };
        const entity2 = { id: 5841 };

        const compareResult1 = service.compareReportingDashboard(entity1, entity2);
        const compareResult2 = service.compareReportingDashboard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
