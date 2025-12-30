import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../monitoring-system-health.test-samples';

import { MonitoringSystemHealthService, RestMonitoringSystemHealth } from './monitoring-system-health.service';

const requireRestSample: RestMonitoringSystemHealth = {
  ...sampleWithRequiredData,
  lastCheck: sampleWithRequiredData.lastCheck?.toJSON(),
};

describe('MonitoringSystemHealth Service', () => {
  let service: MonitoringSystemHealthService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitoringSystemHealth | IMonitoringSystemHealth[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitoringSystemHealthService);
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

    it('should create a MonitoringSystemHealth', () => {
      const monitoringSystemHealth = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitoringSystemHealth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonitoringSystemHealth', () => {
      const monitoringSystemHealth = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitoringSystemHealth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonitoringSystemHealth', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonitoringSystemHealth', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MonitoringSystemHealth', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MonitoringSystemHealth', () => {
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

    describe('addMonitoringSystemHealthToCollectionIfMissing', () => {
      it('should add a MonitoringSystemHealth to an empty array', () => {
        const monitoringSystemHealth: IMonitoringSystemHealth = sampleWithRequiredData;
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing([], monitoringSystemHealth);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringSystemHealth);
      });

      it('should not add a MonitoringSystemHealth to an array that contains it', () => {
        const monitoringSystemHealth: IMonitoringSystemHealth = sampleWithRequiredData;
        const monitoringSystemHealthCollection: IMonitoringSystemHealth[] = [
          {
            ...monitoringSystemHealth,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing(monitoringSystemHealthCollection, monitoringSystemHealth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonitoringSystemHealth to an array that doesn't contain it", () => {
        const monitoringSystemHealth: IMonitoringSystemHealth = sampleWithRequiredData;
        const monitoringSystemHealthCollection: IMonitoringSystemHealth[] = [sampleWithPartialData];
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing(monitoringSystemHealthCollection, monitoringSystemHealth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringSystemHealth);
      });

      it('should add only unique MonitoringSystemHealth to an array', () => {
        const monitoringSystemHealthArray: IMonitoringSystemHealth[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const monitoringSystemHealthCollection: IMonitoringSystemHealth[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing(
          monitoringSystemHealthCollection,
          ...monitoringSystemHealthArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitoringSystemHealth: IMonitoringSystemHealth = sampleWithRequiredData;
        const monitoringSystemHealth2: IMonitoringSystemHealth = sampleWithPartialData;
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing([], monitoringSystemHealth, monitoringSystemHealth2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringSystemHealth);
        expect(expectedResult).toContain(monitoringSystemHealth2);
      });

      it('should accept null and undefined values', () => {
        const monitoringSystemHealth: IMonitoringSystemHealth = sampleWithRequiredData;
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing([], null, monitoringSystemHealth, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringSystemHealth);
      });

      it('should return initial array if no MonitoringSystemHealth is added', () => {
        const monitoringSystemHealthCollection: IMonitoringSystemHealth[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringSystemHealthToCollectionIfMissing(monitoringSystemHealthCollection, undefined, null);
        expect(expectedResult).toEqual(monitoringSystemHealthCollection);
      });
    });

    describe('compareMonitoringSystemHealth', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitoringSystemHealth(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14622 };
        const entity2 = null;

        const compareResult1 = service.compareMonitoringSystemHealth(entity1, entity2);
        const compareResult2 = service.compareMonitoringSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14622 };
        const entity2 = { id: 11934 };

        const compareResult1 = service.compareMonitoringSystemHealth(entity1, entity2);
        const compareResult2 = service.compareMonitoringSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14622 };
        const entity2 = { id: 14622 };

        const compareResult1 = service.compareMonitoringSystemHealth(entity1, entity2);
        const compareResult2 = service.compareMonitoringSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
