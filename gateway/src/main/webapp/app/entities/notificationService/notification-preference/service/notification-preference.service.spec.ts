import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { INotificationPreference } from '../notification-preference.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../notification-preference.test-samples';

import { NotificationPreferenceService, RestNotificationPreference } from './notification-preference.service';

const requireRestSample: RestNotificationPreference = {
  ...sampleWithRequiredData,
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('NotificationPreference Service', () => {
  let service: NotificationPreferenceService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationPreference | INotificationPreference[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationPreferenceService);
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

    it('should create a NotificationPreference', () => {
      const notificationPreference = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationPreference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationPreference', () => {
      const notificationPreference = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationPreference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationPreference', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationPreference', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationPreference', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNotificationPreferenceToCollectionIfMissing', () => {
      it('should add a NotificationPreference to an empty array', () => {
        const notificationPreference: INotificationPreference = sampleWithRequiredData;
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing([], notificationPreference);
        expect(expectedResult).toEqual([notificationPreference]);
      });

      it('should not add a NotificationPreference to an array that contains it', () => {
        const notificationPreference: INotificationPreference = sampleWithRequiredData;
        const notificationPreferenceCollection: INotificationPreference[] = [
          {
            ...notificationPreference,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing(notificationPreferenceCollection, notificationPreference);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationPreference to an array that doesn't contain it", () => {
        const notificationPreference: INotificationPreference = sampleWithRequiredData;
        const notificationPreferenceCollection: INotificationPreference[] = [sampleWithPartialData];
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing(notificationPreferenceCollection, notificationPreference);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationPreference);
      });

      it('should add only unique NotificationPreference to an array', () => {
        const notificationPreferenceArray: INotificationPreference[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationPreferenceCollection: INotificationPreference[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing(
          notificationPreferenceCollection,
          ...notificationPreferenceArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationPreference: INotificationPreference = sampleWithRequiredData;
        const notificationPreference2: INotificationPreference = sampleWithPartialData;
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing([], notificationPreference, notificationPreference2);
        expect(expectedResult).toEqual([notificationPreference, notificationPreference2]);
      });

      it('should accept null and undefined values', () => {
        const notificationPreference: INotificationPreference = sampleWithRequiredData;
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing([], null, notificationPreference, undefined);
        expect(expectedResult).toEqual([notificationPreference]);
      });

      it('should return initial array if no NotificationPreference is added', () => {
        const notificationPreferenceCollection: INotificationPreference[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationPreferenceToCollectionIfMissing(notificationPreferenceCollection, undefined, null);
        expect(expectedResult).toEqual(notificationPreferenceCollection);
      });
    });

    describe('compareNotificationPreference', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationPreference(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17384 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationPreference(entity1, entity2);
        const compareResult2 = service.compareNotificationPreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17384 };
        const entity2 = { id: 19345 };

        const compareResult1 = service.compareNotificationPreference(entity1, entity2);
        const compareResult2 = service.compareNotificationPreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17384 };
        const entity2 = { id: 17384 };

        const compareResult1 = service.compareNotificationPreference(entity1, entity2);
        const compareResult2 = service.compareNotificationPreference(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
