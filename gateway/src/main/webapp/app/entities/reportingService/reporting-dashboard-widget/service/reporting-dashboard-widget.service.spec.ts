import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReportingDashboardWidget } from '../reporting-dashboard-widget.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../reporting-dashboard-widget.test-samples';

import { ReportingDashboardWidgetService } from './reporting-dashboard-widget.service';

const requireRestSample: IReportingDashboardWidget = {
  ...sampleWithRequiredData,
};

describe('ReportingDashboardWidget Service', () => {
  let service: ReportingDashboardWidgetService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportingDashboardWidget | IReportingDashboardWidget[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingDashboardWidgetService);
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

    it('should create a ReportingDashboardWidget', () => {
      const reportingDashboardWidget = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportingDashboardWidget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingDashboardWidget', () => {
      const reportingDashboardWidget = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportingDashboardWidget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingDashboardWidget', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingDashboardWidget', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportingDashboardWidget', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportingDashboardWidget', () => {
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

    describe('addReportingDashboardWidgetToCollectionIfMissing', () => {
      it('should add a ReportingDashboardWidget to an empty array', () => {
        const reportingDashboardWidget: IReportingDashboardWidget = sampleWithRequiredData;
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing([], reportingDashboardWidget);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingDashboardWidget);
      });

      it('should not add a ReportingDashboardWidget to an array that contains it', () => {
        const reportingDashboardWidget: IReportingDashboardWidget = sampleWithRequiredData;
        const reportingDashboardWidgetCollection: IReportingDashboardWidget[] = [
          {
            ...reportingDashboardWidget,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing(
          reportingDashboardWidgetCollection,
          reportingDashboardWidget,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingDashboardWidget to an array that doesn't contain it", () => {
        const reportingDashboardWidget: IReportingDashboardWidget = sampleWithRequiredData;
        const reportingDashboardWidgetCollection: IReportingDashboardWidget[] = [sampleWithPartialData];
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing(
          reportingDashboardWidgetCollection,
          reportingDashboardWidget,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingDashboardWidget);
      });

      it('should add only unique ReportingDashboardWidget to an array', () => {
        const reportingDashboardWidgetArray: IReportingDashboardWidget[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const reportingDashboardWidgetCollection: IReportingDashboardWidget[] = [sampleWithRequiredData];
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing(
          reportingDashboardWidgetCollection,
          ...reportingDashboardWidgetArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingDashboardWidget: IReportingDashboardWidget = sampleWithRequiredData;
        const reportingDashboardWidget2: IReportingDashboardWidget = sampleWithPartialData;
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing([], reportingDashboardWidget, reportingDashboardWidget2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingDashboardWidget);
        expect(expectedResult).toContain(reportingDashboardWidget2);
      });

      it('should accept null and undefined values', () => {
        const reportingDashboardWidget: IReportingDashboardWidget = sampleWithRequiredData;
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing([], null, reportingDashboardWidget, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingDashboardWidget);
      });

      it('should return initial array if no ReportingDashboardWidget is added', () => {
        const reportingDashboardWidgetCollection: IReportingDashboardWidget[] = [sampleWithRequiredData];
        expectedResult = service.addReportingDashboardWidgetToCollectionIfMissing(reportingDashboardWidgetCollection, undefined, null);
        expect(expectedResult).toEqual(reportingDashboardWidgetCollection);
      });
    });

    describe('compareReportingDashboardWidget', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportingDashboardWidget(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12221 };
        const entity2 = null;

        const compareResult1 = service.compareReportingDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareReportingDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12221 };
        const entity2 = { id: 25774 };

        const compareResult1 = service.compareReportingDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareReportingDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12221 };
        const entity2 = { id: 12221 };

        const compareResult1 = service.compareReportingDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareReportingDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
