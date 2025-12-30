import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { INotificationWebhookLog } from '../notification-webhook-log.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../notification-webhook-log.test-samples';

import { NotificationWebhookLogService, RestNotificationWebhookLog } from './notification-webhook-log.service';

const requireRestSample: RestNotificationWebhookLog = {
  ...sampleWithRequiredData,
  sentDate: sampleWithRequiredData.sentDate?.toJSON(),
};

describe('NotificationWebhookLog Service', () => {
  let service: NotificationWebhookLogService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationWebhookLog | INotificationWebhookLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationWebhookLogService);
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

    it('should create a NotificationWebhookLog', () => {
      const notificationWebhookLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationWebhookLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationWebhookLog', () => {
      const notificationWebhookLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationWebhookLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationWebhookLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationWebhookLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationWebhookLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a NotificationWebhookLog', () => {
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

    describe('addNotificationWebhookLogToCollectionIfMissing', () => {
      it('should add a NotificationWebhookLog to an empty array', () => {
        const notificationWebhookLog: INotificationWebhookLog = sampleWithRequiredData;
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing([], notificationWebhookLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationWebhookLog);
      });

      it('should not add a NotificationWebhookLog to an array that contains it', () => {
        const notificationWebhookLog: INotificationWebhookLog = sampleWithRequiredData;
        const notificationWebhookLogCollection: INotificationWebhookLog[] = [
          {
            ...notificationWebhookLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing(notificationWebhookLogCollection, notificationWebhookLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationWebhookLog to an array that doesn't contain it", () => {
        const notificationWebhookLog: INotificationWebhookLog = sampleWithRequiredData;
        const notificationWebhookLogCollection: INotificationWebhookLog[] = [sampleWithPartialData];
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing(notificationWebhookLogCollection, notificationWebhookLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationWebhookLog);
      });

      it('should add only unique NotificationWebhookLog to an array', () => {
        const notificationWebhookLogArray: INotificationWebhookLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationWebhookLogCollection: INotificationWebhookLog[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing(
          notificationWebhookLogCollection,
          ...notificationWebhookLogArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationWebhookLog: INotificationWebhookLog = sampleWithRequiredData;
        const notificationWebhookLog2: INotificationWebhookLog = sampleWithPartialData;
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing([], notificationWebhookLog, notificationWebhookLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationWebhookLog);
        expect(expectedResult).toContain(notificationWebhookLog2);
      });

      it('should accept null and undefined values', () => {
        const notificationWebhookLog: INotificationWebhookLog = sampleWithRequiredData;
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing([], null, notificationWebhookLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationWebhookLog);
      });

      it('should return initial array if no NotificationWebhookLog is added', () => {
        const notificationWebhookLogCollection: INotificationWebhookLog[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationWebhookLogToCollectionIfMissing(notificationWebhookLogCollection, undefined, null);
        expect(expectedResult).toEqual(notificationWebhookLogCollection);
      });
    });

    describe('compareNotificationWebhookLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationWebhookLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9697 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationWebhookLog(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9697 };
        const entity2 = { id: 7958 };

        const compareResult1 = service.compareNotificationWebhookLog(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9697 };
        const entity2 = { id: 9697 };

        const compareResult1 = service.compareNotificationWebhookLog(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
