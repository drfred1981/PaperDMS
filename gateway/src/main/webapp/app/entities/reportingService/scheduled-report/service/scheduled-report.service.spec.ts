import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScheduledReport } from '../scheduled-report.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../scheduled-report.test-samples';

import { RestScheduledReport, ScheduledReportService } from './scheduled-report.service';

const requireRestSample: RestScheduledReport = {
  ...sampleWithRequiredData,
  lastRun: sampleWithRequiredData.lastRun?.toJSON(),
  nextRun: sampleWithRequiredData.nextRun?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ScheduledReport Service', () => {
  let service: ScheduledReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IScheduledReport | IScheduledReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScheduledReportService);
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

    it('should create a ScheduledReport', () => {
      const scheduledReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scheduledReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScheduledReport', () => {
      const scheduledReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scheduledReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScheduledReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScheduledReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScheduledReport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScheduledReportToCollectionIfMissing', () => {
      it('should add a ScheduledReport to an empty array', () => {
        const scheduledReport: IScheduledReport = sampleWithRequiredData;
        expectedResult = service.addScheduledReportToCollectionIfMissing([], scheduledReport);
        expect(expectedResult).toEqual([scheduledReport]);
      });

      it('should not add a ScheduledReport to an array that contains it', () => {
        const scheduledReport: IScheduledReport = sampleWithRequiredData;
        const scheduledReportCollection: IScheduledReport[] = [
          {
            ...scheduledReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScheduledReportToCollectionIfMissing(scheduledReportCollection, scheduledReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScheduledReport to an array that doesn't contain it", () => {
        const scheduledReport: IScheduledReport = sampleWithRequiredData;
        const scheduledReportCollection: IScheduledReport[] = [sampleWithPartialData];
        expectedResult = service.addScheduledReportToCollectionIfMissing(scheduledReportCollection, scheduledReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scheduledReport);
      });

      it('should add only unique ScheduledReport to an array', () => {
        const scheduledReportArray: IScheduledReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scheduledReportCollection: IScheduledReport[] = [sampleWithRequiredData];
        expectedResult = service.addScheduledReportToCollectionIfMissing(scheduledReportCollection, ...scheduledReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scheduledReport: IScheduledReport = sampleWithRequiredData;
        const scheduledReport2: IScheduledReport = sampleWithPartialData;
        expectedResult = service.addScheduledReportToCollectionIfMissing([], scheduledReport, scheduledReport2);
        expect(expectedResult).toEqual([scheduledReport, scheduledReport2]);
      });

      it('should accept null and undefined values', () => {
        const scheduledReport: IScheduledReport = sampleWithRequiredData;
        expectedResult = service.addScheduledReportToCollectionIfMissing([], null, scheduledReport, undefined);
        expect(expectedResult).toEqual([scheduledReport]);
      });

      it('should return initial array if no ScheduledReport is added', () => {
        const scheduledReportCollection: IScheduledReport[] = [sampleWithRequiredData];
        expectedResult = service.addScheduledReportToCollectionIfMissing(scheduledReportCollection, undefined, null);
        expect(expectedResult).toEqual(scheduledReportCollection);
      });
    });

    describe('compareScheduledReport', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScheduledReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28944 };
        const entity2 = null;

        const compareResult1 = service.compareScheduledReport(entity1, entity2);
        const compareResult2 = service.compareScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28944 };
        const entity2 = { id: 3093 };

        const compareResult1 = service.compareScheduledReport(entity1, entity2);
        const compareResult2 = service.compareScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28944 };
        const entity2 = { id: 28944 };

        const compareResult1 = service.compareScheduledReport(entity1, entity2);
        const compareResult2 = service.compareScheduledReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
