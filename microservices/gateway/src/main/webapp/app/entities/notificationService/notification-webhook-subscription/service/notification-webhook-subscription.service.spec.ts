import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../notification-webhook-subscription.test-samples';

import { NotificationWebhookSubscriptionService, RestNotificationWebhookSubscription } from './notification-webhook-subscription.service';

const requireRestSample: RestNotificationWebhookSubscription = {
  ...sampleWithRequiredData,
  lastTriggerDate: sampleWithRequiredData.lastTriggerDate?.toJSON(),
  lastSuccessDate: sampleWithRequiredData.lastSuccessDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('NotificationWebhookSubscription Service', () => {
  let service: NotificationWebhookSubscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationWebhookSubscription | INotificationWebhookSubscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationWebhookSubscriptionService);
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

    it('should create a NotificationWebhookSubscription', () => {
      const notificationWebhookSubscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationWebhookSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationWebhookSubscription', () => {
      const notificationWebhookSubscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationWebhookSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationWebhookSubscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationWebhookSubscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationWebhookSubscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a NotificationWebhookSubscription', () => {
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

    describe('addNotificationWebhookSubscriptionToCollectionIfMissing', () => {
      it('should add a NotificationWebhookSubscription to an empty array', () => {
        const notificationWebhookSubscription: INotificationWebhookSubscription = sampleWithRequiredData;
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing([], notificationWebhookSubscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationWebhookSubscription);
      });

      it('should not add a NotificationWebhookSubscription to an array that contains it', () => {
        const notificationWebhookSubscription: INotificationWebhookSubscription = sampleWithRequiredData;
        const notificationWebhookSubscriptionCollection: INotificationWebhookSubscription[] = [
          {
            ...notificationWebhookSubscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          notificationWebhookSubscriptionCollection,
          notificationWebhookSubscription,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationWebhookSubscription to an array that doesn't contain it", () => {
        const notificationWebhookSubscription: INotificationWebhookSubscription = sampleWithRequiredData;
        const notificationWebhookSubscriptionCollection: INotificationWebhookSubscription[] = [sampleWithPartialData];
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          notificationWebhookSubscriptionCollection,
          notificationWebhookSubscription,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationWebhookSubscription);
      });

      it('should add only unique NotificationWebhookSubscription to an array', () => {
        const notificationWebhookSubscriptionArray: INotificationWebhookSubscription[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const notificationWebhookSubscriptionCollection: INotificationWebhookSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          notificationWebhookSubscriptionCollection,
          ...notificationWebhookSubscriptionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationWebhookSubscription: INotificationWebhookSubscription = sampleWithRequiredData;
        const notificationWebhookSubscription2: INotificationWebhookSubscription = sampleWithPartialData;
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          [],
          notificationWebhookSubscription,
          notificationWebhookSubscription2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationWebhookSubscription);
        expect(expectedResult).toContain(notificationWebhookSubscription2);
      });

      it('should accept null and undefined values', () => {
        const notificationWebhookSubscription: INotificationWebhookSubscription = sampleWithRequiredData;
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          [],
          null,
          notificationWebhookSubscription,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationWebhookSubscription);
      });

      it('should return initial array if no NotificationWebhookSubscription is added', () => {
        const notificationWebhookSubscriptionCollection: INotificationWebhookSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationWebhookSubscriptionToCollectionIfMissing(
          notificationWebhookSubscriptionCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(notificationWebhookSubscriptionCollection);
      });
    });

    describe('compareNotificationWebhookSubscription', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationWebhookSubscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19116 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationWebhookSubscription(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19116 };
        const entity2 = { id: 13817 };

        const compareResult1 = service.compareNotificationWebhookSubscription(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19116 };
        const entity2 = { id: 19116 };

        const compareResult1 = service.compareNotificationWebhookSubscription(entity1, entity2);
        const compareResult2 = service.compareNotificationWebhookSubscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
