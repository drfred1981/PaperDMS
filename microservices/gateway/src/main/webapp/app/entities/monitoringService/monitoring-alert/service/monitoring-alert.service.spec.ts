import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitoringAlert } from '../monitoring-alert.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../monitoring-alert.test-samples';

import { MonitoringAlertService, RestMonitoringAlert } from './monitoring-alert.service';

const requireRestSample: RestMonitoringAlert = {
  ...sampleWithRequiredData,
  triggeredDate: sampleWithRequiredData.triggeredDate?.toJSON(),
  acknowledgedDate: sampleWithRequiredData.acknowledgedDate?.toJSON(),
  resolvedDate: sampleWithRequiredData.resolvedDate?.toJSON(),
};

describe('MonitoringAlert Service', () => {
  let service: MonitoringAlertService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitoringAlert | IMonitoringAlert[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitoringAlertService);
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

    it('should create a MonitoringAlert', () => {
      const monitoringAlert = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitoringAlert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonitoringAlert', () => {
      const monitoringAlert = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitoringAlert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonitoringAlert', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonitoringAlert', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MonitoringAlert', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MonitoringAlert', () => {
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

    describe('addMonitoringAlertToCollectionIfMissing', () => {
      it('should add a MonitoringAlert to an empty array', () => {
        const monitoringAlert: IMonitoringAlert = sampleWithRequiredData;
        expectedResult = service.addMonitoringAlertToCollectionIfMissing([], monitoringAlert);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringAlert);
      });

      it('should not add a MonitoringAlert to an array that contains it', () => {
        const monitoringAlert: IMonitoringAlert = sampleWithRequiredData;
        const monitoringAlertCollection: IMonitoringAlert[] = [
          {
            ...monitoringAlert,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitoringAlertToCollectionIfMissing(monitoringAlertCollection, monitoringAlert);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonitoringAlert to an array that doesn't contain it", () => {
        const monitoringAlert: IMonitoringAlert = sampleWithRequiredData;
        const monitoringAlertCollection: IMonitoringAlert[] = [sampleWithPartialData];
        expectedResult = service.addMonitoringAlertToCollectionIfMissing(monitoringAlertCollection, monitoringAlert);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringAlert);
      });

      it('should add only unique MonitoringAlert to an array', () => {
        const monitoringAlertArray: IMonitoringAlert[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const monitoringAlertCollection: IMonitoringAlert[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringAlertToCollectionIfMissing(monitoringAlertCollection, ...monitoringAlertArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitoringAlert: IMonitoringAlert = sampleWithRequiredData;
        const monitoringAlert2: IMonitoringAlert = sampleWithPartialData;
        expectedResult = service.addMonitoringAlertToCollectionIfMissing([], monitoringAlert, monitoringAlert2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringAlert);
        expect(expectedResult).toContain(monitoringAlert2);
      });

      it('should accept null and undefined values', () => {
        const monitoringAlert: IMonitoringAlert = sampleWithRequiredData;
        expectedResult = service.addMonitoringAlertToCollectionIfMissing([], null, monitoringAlert, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringAlert);
      });

      it('should return initial array if no MonitoringAlert is added', () => {
        const monitoringAlertCollection: IMonitoringAlert[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringAlertToCollectionIfMissing(monitoringAlertCollection, undefined, null);
        expect(expectedResult).toEqual(monitoringAlertCollection);
      });
    });

    describe('compareMonitoringAlert', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitoringAlert(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15172 };
        const entity2 = null;

        const compareResult1 = service.compareMonitoringAlert(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15172 };
        const entity2 = { id: 11544 };

        const compareResult1 = service.compareMonitoringAlert(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15172 };
        const entity2 = { id: 15172 };

        const compareResult1 = service.compareMonitoringAlert(entity1, entity2);
        const compareResult2 = service.compareMonitoringAlert(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
