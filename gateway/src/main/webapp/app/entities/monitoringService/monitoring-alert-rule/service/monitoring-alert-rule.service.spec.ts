import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitoringAlertRule } from '../monitoring-alert-rule.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../monitoring-alert-rule.test-samples';

import { MonitoringAlertRuleService, RestMonitoringAlertRule } from './monitoring-alert-rule.service';

const requireRestSample: RestMonitoringAlertRule = {
  ...sampleWithRequiredData,
  lastTriggered: sampleWithRequiredData.lastTriggered?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MonitoringAlertRule Service', () => {
  let service: MonitoringAlertRuleService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitoringAlertRule | IMonitoringAlertRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitoringAlertRuleService);
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

    it('should create a MonitoringAlertRule', () => {
      const monitoringAlertRule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitoringAlertRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonitoringAlertRule', () => {
      const monitoringAlertRule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitoringAlertRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonitoringAlertRule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonitoringAlertRule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MonitoringAlertRule', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MonitoringAlertRule', () => {
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

    describe('addMonitoringAlertRuleToCollectionIfMissing', () => {
      it('should add a MonitoringAlertRule to an empty array', () => {
        const monitoringAlertRule: IMonitoringAlertRule = sampleWithRequiredData;
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing([], monitoringAlertRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringAlertRule);
      });

      it('should not add a MonitoringAlertRule to an array that contains it', () => {
        const monitoringAlertRule: IMonitoringAlertRule = sampleWithRequiredData;
        const monitoringAlertRuleCollection: IMonitoringAlertRule[] = [
          {
            ...monitoringAlertRule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing(monitoringAlertRuleCollection, monitoringAlertRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonitoringAlertRule to an array that doesn't contain it", () => {
        const monitoringAlertRule: IMonitoringAlertRule = sampleWithRequiredData;
        const monitoringAlertRuleCollection: IMonitoringAlertRule[] = [sampleWithPartialData];
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing(monitoringAlertRuleCollection, monitoringAlertRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringAlertRule);
      });

      it('should add only unique MonitoringAlertRule to an array', () => {
        const monitoringAlertRuleArray: IMonitoringAlertRule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const monitoringAlertRuleCollection: IMonitoringAlertRule[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing(monitoringAlertRuleCollection, ...monitoringAlertRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitoringAlertRule: IMonitoringAlertRule = sampleWithRequiredData;
        const monitoringAlertRule2: IMonitoringAlertRule = sampleWithPartialData;
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing([], monitoringAlertRule, monitoringAlertRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringAlertRule);
        expect(expectedResult).toContain(monitoringAlertRule2);
      });

      it('should accept null and undefined values', () => {
        const monitoringAlertRule: IMonitoringAlertRule = sampleWithRequiredData;
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing([], null, monitoringAlertRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringAlertRule);
      });

      it('should return initial array if no MonitoringAlertRule is added', () => {
        const monitoringAlertRuleCollection: IMonitoringAlertRule[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringAlertRuleToCollectionIfMissing(monitoringAlertRuleCollection, undefined, null);
        expect(expectedResult).toEqual(monitoringAlertRuleCollection);
      });
    });

    describe('compareMonitoringAlertRule', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitoringAlertRule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16699 };
        const entity2 = null;

        const compareResult1 = service.compareMonitoringAlertRule(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlertRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16699 };
        const entity2 = { id: 14601 };

        const compareResult1 = service.compareMonitoringAlertRule(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlertRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16699 };
        const entity2 = { id: 16699 };

        const compareResult1 = service.compareMonitoringAlertRule(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlertRule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
