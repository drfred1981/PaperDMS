import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitoringServiceStatus } from '../monitoring-service-status.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../monitoring-service-status.test-samples';

import { MonitoringServiceStatusService, RestMonitoringServiceStatus } from './monitoring-service-status.service';

const requireRestSample: RestMonitoringServiceStatus = {
  ...sampleWithRequiredData,
  lastPing: sampleWithRequiredData.lastPing?.toJSON(),
};

describe('MonitoringServiceStatus Service', () => {
  let service: MonitoringServiceStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitoringServiceStatus | IMonitoringServiceStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitoringServiceStatusService);
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

    it('should create a MonitoringServiceStatus', () => {
      const monitoringServiceStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitoringServiceStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonitoringServiceStatus', () => {
      const monitoringServiceStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitoringServiceStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonitoringServiceStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonitoringServiceStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MonitoringServiceStatus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MonitoringServiceStatus', () => {
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

    describe('addMonitoringServiceStatusToCollectionIfMissing', () => {
      it('should add a MonitoringServiceStatus to an empty array', () => {
        const monitoringServiceStatus: IMonitoringServiceStatus = sampleWithRequiredData;
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing([], monitoringServiceStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringServiceStatus);
      });

      it('should not add a MonitoringServiceStatus to an array that contains it', () => {
        const monitoringServiceStatus: IMonitoringServiceStatus = sampleWithRequiredData;
        const monitoringServiceStatusCollection: IMonitoringServiceStatus[] = [
          {
            ...monitoringServiceStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing(
          monitoringServiceStatusCollection,
          monitoringServiceStatus,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonitoringServiceStatus to an array that doesn't contain it", () => {
        const monitoringServiceStatus: IMonitoringServiceStatus = sampleWithRequiredData;
        const monitoringServiceStatusCollection: IMonitoringServiceStatus[] = [sampleWithPartialData];
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing(
          monitoringServiceStatusCollection,
          monitoringServiceStatus,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringServiceStatus);
      });

      it('should add only unique MonitoringServiceStatus to an array', () => {
        const monitoringServiceStatusArray: IMonitoringServiceStatus[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const monitoringServiceStatusCollection: IMonitoringServiceStatus[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing(
          monitoringServiceStatusCollection,
          ...monitoringServiceStatusArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitoringServiceStatus: IMonitoringServiceStatus = sampleWithRequiredData;
        const monitoringServiceStatus2: IMonitoringServiceStatus = sampleWithPartialData;
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing([], monitoringServiceStatus, monitoringServiceStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringServiceStatus);
        expect(expectedResult).toContain(monitoringServiceStatus2);
      });

      it('should accept null and undefined values', () => {
        const monitoringServiceStatus: IMonitoringServiceStatus = sampleWithRequiredData;
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing([], null, monitoringServiceStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringServiceStatus);
      });

      it('should return initial array if no MonitoringServiceStatus is added', () => {
        const monitoringServiceStatusCollection: IMonitoringServiceStatus[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringServiceStatusToCollectionIfMissing(monitoringServiceStatusCollection, undefined, null);
        expect(expectedResult).toEqual(monitoringServiceStatusCollection);
      });
    });

    describe('compareMonitoringServiceStatus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitoringServiceStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22154 };
        const entity2 = null;

        const compareResult1 = service.compareMonitoringServiceStatus(entity1, entity2);
        const compareResult2 = service.compareMonitoringServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22154 };
        const entity2 = { id: 17949 };

        const compareResult1 = service.compareMonitoringServiceStatus(entity1, entity2);
        const compareResult2 = service.compareMonitoringServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22154 };
        const entity2 = { id: 22154 };

        const compareResult1 = service.compareMonitoringServiceStatus(entity1, entity2);
        const compareResult2 = service.compareMonitoringServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
