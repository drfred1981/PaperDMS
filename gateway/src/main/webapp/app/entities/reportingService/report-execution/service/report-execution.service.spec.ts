import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IReportExecution } from '../report-execution.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../report-execution.test-samples';

import { ReportExecutionService, RestReportExecution } from './report-execution.service';

const requireRestSample: RestReportExecution = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('ReportExecution Service', () => {
  let service: ReportExecutionService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportExecution | IReportExecution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportExecutionService);
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

    it('should create a ReportExecution', () => {
      const reportExecution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportExecution', () => {
      const reportExecution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportExecution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportExecution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportExecution', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportExecutionToCollectionIfMissing', () => {
      it('should add a ReportExecution to an empty array', () => {
        const reportExecution: IReportExecution = sampleWithRequiredData;
        expectedResult = service.addReportExecutionToCollectionIfMissing([], reportExecution);
        expect(expectedResult).toEqual([reportExecution]);
      });

      it('should not add a ReportExecution to an array that contains it', () => {
        const reportExecution: IReportExecution = sampleWithRequiredData;
        const reportExecutionCollection: IReportExecution[] = [
          {
            ...reportExecution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportExecutionToCollectionIfMissing(reportExecutionCollection, reportExecution);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportExecution to an array that doesn't contain it", () => {
        const reportExecution: IReportExecution = sampleWithRequiredData;
        const reportExecutionCollection: IReportExecution[] = [sampleWithPartialData];
        expectedResult = service.addReportExecutionToCollectionIfMissing(reportExecutionCollection, reportExecution);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportExecution);
      });

      it('should add only unique ReportExecution to an array', () => {
        const reportExecutionArray: IReportExecution[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportExecutionCollection: IReportExecution[] = [sampleWithRequiredData];
        expectedResult = service.addReportExecutionToCollectionIfMissing(reportExecutionCollection, ...reportExecutionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportExecution: IReportExecution = sampleWithRequiredData;
        const reportExecution2: IReportExecution = sampleWithPartialData;
        expectedResult = service.addReportExecutionToCollectionIfMissing([], reportExecution, reportExecution2);
        expect(expectedResult).toEqual([reportExecution, reportExecution2]);
      });

      it('should accept null and undefined values', () => {
        const reportExecution: IReportExecution = sampleWithRequiredData;
        expectedResult = service.addReportExecutionToCollectionIfMissing([], null, reportExecution, undefined);
        expect(expectedResult).toEqual([reportExecution]);
      });

      it('should return initial array if no ReportExecution is added', () => {
        const reportExecutionCollection: IReportExecution[] = [sampleWithRequiredData];
        expectedResult = service.addReportExecutionToCollectionIfMissing(reportExecutionCollection, undefined, null);
        expect(expectedResult).toEqual(reportExecutionCollection);
      });
    });

    describe('compareReportExecution', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportExecution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23917 };
        const entity2 = null;

        const compareResult1 = service.compareReportExecution(entity1, entity2);
        const compareResult2 = service.compareReportExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23917 };
        const entity2 = { id: 29743 };

        const compareResult1 = service.compareReportExecution(entity1, entity2);
        const compareResult2 = service.compareReportExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23917 };
        const entity2 = { id: 23917 };

        const compareResult1 = service.compareReportExecution(entity1, entity2);
        const compareResult2 = service.compareReportExecution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
