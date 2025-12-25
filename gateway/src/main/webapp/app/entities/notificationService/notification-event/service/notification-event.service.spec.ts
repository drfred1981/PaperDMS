import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { INotificationEvent } from '../notification-event.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../notification-event.test-samples';

import { NotificationEventService, RestNotificationEvent } from './notification-event.service';

const requireRestSample: RestNotificationEvent = {
  ...sampleWithRequiredData,
  eventDate: sampleWithRequiredData.eventDate?.toJSON(),
  processedDate: sampleWithRequiredData.processedDate?.toJSON(),
};

describe('NotificationEvent Service', () => {
  let service: NotificationEventService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationEvent | INotificationEvent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationEventService);
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

    it('should create a NotificationEvent', () => {
      const notificationEvent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationEvent', () => {
      const notificationEvent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationEvent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationEvent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationEvent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNotificationEventToCollectionIfMissing', () => {
      it('should add a NotificationEvent to an empty array', () => {
        const notificationEvent: INotificationEvent = sampleWithRequiredData;
        expectedResult = service.addNotificationEventToCollectionIfMissing([], notificationEvent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationEvent);
      });

      it('should not add a NotificationEvent to an array that contains it', () => {
        const notificationEvent: INotificationEvent = sampleWithRequiredData;
        const notificationEventCollection: INotificationEvent[] = [
          {
            ...notificationEvent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationEventToCollectionIfMissing(notificationEventCollection, notificationEvent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationEvent to an array that doesn't contain it", () => {
        const notificationEvent: INotificationEvent = sampleWithRequiredData;
        const notificationEventCollection: INotificationEvent[] = [sampleWithPartialData];
        expectedResult = service.addNotificationEventToCollectionIfMissing(notificationEventCollection, notificationEvent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationEvent);
      });

      it('should add only unique NotificationEvent to an array', () => {
        const notificationEventArray: INotificationEvent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationEventCollection: INotificationEvent[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationEventToCollectionIfMissing(notificationEventCollection, ...notificationEventArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationEvent: INotificationEvent = sampleWithRequiredData;
        const notificationEvent2: INotificationEvent = sampleWithPartialData;
        expectedResult = service.addNotificationEventToCollectionIfMissing([], notificationEvent, notificationEvent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationEvent);
        expect(expectedResult).toContain(notificationEvent2);
      });

      it('should accept null and undefined values', () => {
        const notificationEvent: INotificationEvent = sampleWithRequiredData;
        expectedResult = service.addNotificationEventToCollectionIfMissing([], null, notificationEvent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationEvent);
      });

      it('should return initial array if no NotificationEvent is added', () => {
        const notificationEventCollection: INotificationEvent[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationEventToCollectionIfMissing(notificationEventCollection, undefined, null);
        expect(expectedResult).toEqual(notificationEventCollection);
      });
    });

    describe('compareNotificationEvent', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationEvent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9232 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationEvent(entity1, entity2);
        const compareResult2 = service.compareNotificationEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9232 };
        const entity2 = { id: 30681 };

        const compareResult1 = service.compareNotificationEvent(entity1, entity2);
        const compareResult2 = service.compareNotificationEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9232 };
        const entity2 = { id: 9232 };

        const compareResult1 = service.compareNotificationEvent(entity1, entity2);
        const compareResult2 = service.compareNotificationEvent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
