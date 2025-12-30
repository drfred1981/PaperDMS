import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingExecution } from '../reporting-execution.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reporting-execution.test-samples';

import { ReportingExecutionService, RestReportingExecution } from './reporting-execution.service';

const requireRestSample: RestReportingExecution = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('ReportingExecution Service', () => {
  let service: ReportingExecutionService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingExecution | IReportingExecution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingExecutionService);
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

    it('should create a ReportingExecution', () => {
      const reportingExecution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingExecution', () => {
      const reportingExecution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingExecution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingExecution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingExecution', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingExecution', () => {
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

    describe('addReportingExecutionToCollectionIfMissing', () => {
      it('should add a ReportingExecution to an empty array', () => {
        const reportingExecution: IReportingExecution = sampleWithRequiredData;
        expectedResult = service.addReportingExecutionToCollectionIfMissing([], reportingExecution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingExecution);
      });

      it('should not add a ReportingExecution to an array that contains it', () => {
        const reportingExecution: IReportingExecution = sampleWithRequiredData;
        const reportingExecutionCollection: IReportingExecution[] = [
          {
            ...reportingExecution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingExecutionToCollectionIfMissing(reportingExecutionCollection, reportingExecution);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingExecution to an array that doesn't contain it", () => {
        const reportingExecution: IReportingExecution = sampleWithRequiredData;
        const reportingExecutionCollection: IReportingExecution[] = [sampleWithPartialData];
        expectedResult = service.addReportingExecutionToCollectionIfMissing(reportingExecutionCollection, reportingExecution);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingExecution);
      });

      it('should add only unique ReportingExecution to an array', () => {
        const reportingExecutionArray: IReportingExecution[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportingExecutionCollection: IReportingExecution[] = [sampleWithRequiredData];
        expectedResult = service.addReportingExecutionToCollectionIfMissing(reportingExecutionCollection, ...reportingExecutionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingExecution: IReportingExecution = sampleWithRequiredData;
        const reportingExecution2: IReportingExecution = sampleWithPartialData;
        expectedResult = service.addReportingExecutionToCollectionIfMissing([], reportingExecution, reportingExecution2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingExecution);
        expect(expectedResult).toContain(reportingExecution2);
      });

      it('should accept null and undefined values', () => {
        const reportingExecution: IReportingExecution = sampleWithRequiredData;
        expectedResult = service.addReportingExecutionToCollectionIfMissing([], null, reportingExecution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingExecution);
      });

      it('should return initial array if no ReportingExecution is added', () => {
        const reportingExecutionCollection: IReportingExecution[] = [sampleWithRequiredData];
        expectedResult = service.addReportingExecutionToCollectionIfMissing(reportingExecutionCollection, undefined, null);
        expect(expectedResult).toEqual(reportingExecutionCollection);
      });
    });

    describe('compareReportingExecution', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingExecution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27701 };
        const entity2 = null;

        const compareResult1 = service.compareReportingExecution(entity1, entity2);
        const compareResult2 = service.compareReportingExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27701 };
        const entity2 = { id: 8451 };

        const compareResult1 = service.compareReportingExecution(entity1, entity2);
        const compareResult2 = service.compareReportingExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27701 };
        const entity2 = { id: 27701 };

        const compareResult1 = service.compareReportingExecution(entity1, entity2);
        const compareResult2 = service.compareReportingExecution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
