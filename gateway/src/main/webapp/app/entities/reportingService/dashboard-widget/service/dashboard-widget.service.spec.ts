import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDashboardWidget } from '../dashboard-widget.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../dashboard-widget.test-samples';

import { DashboardWidgetService } from './dashboard-widget.service';

const requireRestSample: IDashboardWidget = {
  ...sampleWithRequiredData,
};

describe('DashboardWidget Service', () => {
  let service: DashboardWidgetService;
  let httpMock: HttpTestingController;
  let expectedResult: IDashboardWidget | IDashboardWidget[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DashboardWidgetService);
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

    it('should create a DashboardWidget', () => {
      const dashboardWidget = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dashboardWidget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DashboardWidget', () => {
      const dashboardWidget = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dashboardWidget).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DashboardWidget', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DashboardWidget', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DashboardWidget', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDashboardWidgetToCollectionIfMissing', () => {
      it('should add a DashboardWidget to an empty array', () => {
        const dashboardWidget: IDashboardWidget = sampleWithRequiredData;
        expectedResult = service.addDashboardWidgetToCollectionIfMissing([], dashboardWidget);
        expect(expectedResult).toEqual([dashboardWidget]);
      });

      it('should not add a DashboardWidget to an array that contains it', () => {
        const dashboardWidget: IDashboardWidget = sampleWithRequiredData;
        const dashboardWidgetCollection: IDashboardWidget[] = [
          {
            ...dashboardWidget,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDashboardWidgetToCollectionIfMissing(dashboardWidgetCollection, dashboardWidget);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DashboardWidget to an array that doesn't contain it", () => {
        const dashboardWidget: IDashboardWidget = sampleWithRequiredData;
        const dashboardWidgetCollection: IDashboardWidget[] = [sampleWithPartialData];
        expectedResult = service.addDashboardWidgetToCollectionIfMissing(dashboardWidgetCollection, dashboardWidget);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dashboardWidget);
      });

      it('should add only unique DashboardWidget to an array', () => {
        const dashboardWidgetArray: IDashboardWidget[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dashboardWidgetCollection: IDashboardWidget[] = [sampleWithRequiredData];
        expectedResult = service.addDashboardWidgetToCollectionIfMissing(dashboardWidgetCollection, ...dashboardWidgetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dashboardWidget: IDashboardWidget = sampleWithRequiredData;
        const dashboardWidget2: IDashboardWidget = sampleWithPartialData;
        expectedResult = service.addDashboardWidgetToCollectionIfMissing([], dashboardWidget, dashboardWidget2);
        expect(expectedResult).toEqual([dashboardWidget, dashboardWidget2]);
      });

      it('should accept null and undefined values', () => {
        const dashboardWidget: IDashboardWidget = sampleWithRequiredData;
        expectedResult = service.addDashboardWidgetToCollectionIfMissing([], null, dashboardWidget, undefined);
        expect(expectedResult).toEqual([dashboardWidget]);
      });

      it('should return initial array if no DashboardWidget is added', () => {
        const dashboardWidgetCollection: IDashboardWidget[] = [sampleWithRequiredData];
        expectedResult = service.addDashboardWidgetToCollectionIfMissing(dashboardWidgetCollection, undefined, null);
        expect(expectedResult).toEqual(dashboardWidgetCollection);
      });
    });

    describe('compareDashboardWidget', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDashboardWidget(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6668 };
        const entity2 = null;

        const compareResult1 = service.compareDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6668 };
        const entity2 = { id: 30011 };

        const compareResult1 = service.compareDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6668 };
        const entity2 = { id: 6668 };

        const compareResult1 = service.compareDashboardWidget(entity1, entity2);
        const compareResult2 = service.compareDashboardWidget(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
