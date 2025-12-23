import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { INotificationTemplate } from '../notification-template.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../notification-template.test-samples';

import { NotificationTemplateService, RestNotificationTemplate } from './notification-template.service';

const requireRestSample: RestNotificationTemplate = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('NotificationTemplate Service', () => {
  let service: NotificationTemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationTemplate | INotificationTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationTemplateService);
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

    it('should create a NotificationTemplate', () => {
      const notificationTemplate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationTemplate', () => {
      const notificationTemplate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationTemplate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationTemplate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationTemplate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNotificationTemplateToCollectionIfMissing', () => {
      it('should add a NotificationTemplate to an empty array', () => {
        const notificationTemplate: INotificationTemplate = sampleWithRequiredData;
        expectedResult = service.addNotificationTemplateToCollectionIfMissing([], notificationTemplate);
        expect(expectedResult).toEqual([notificationTemplate]);
      });

      it('should not add a NotificationTemplate to an array that contains it', () => {
        const notificationTemplate: INotificationTemplate = sampleWithRequiredData;
        const notificationTemplateCollection: INotificationTemplate[] = [
          {
            ...notificationTemplate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationTemplateToCollectionIfMissing(notificationTemplateCollection, notificationTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationTemplate to an array that doesn't contain it", () => {
        const notificationTemplate: INotificationTemplate = sampleWithRequiredData;
        const notificationTemplateCollection: INotificationTemplate[] = [sampleWithPartialData];
        expectedResult = service.addNotificationTemplateToCollectionIfMissing(notificationTemplateCollection, notificationTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationTemplate);
      });

      it('should add only unique NotificationTemplate to an array', () => {
        const notificationTemplateArray: INotificationTemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationTemplateCollection: INotificationTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationTemplateToCollectionIfMissing(notificationTemplateCollection, ...notificationTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationTemplate: INotificationTemplate = sampleWithRequiredData;
        const notificationTemplate2: INotificationTemplate = sampleWithPartialData;
        expectedResult = service.addNotificationTemplateToCollectionIfMissing([], notificationTemplate, notificationTemplate2);
        expect(expectedResult).toEqual([notificationTemplate, notificationTemplate2]);
      });

      it('should accept null and undefined values', () => {
        const notificationTemplate: INotificationTemplate = sampleWithRequiredData;
        expectedResult = service.addNotificationTemplateToCollectionIfMissing([], null, notificationTemplate, undefined);
        expect(expectedResult).toEqual([notificationTemplate]);
      });

      it('should return initial array if no NotificationTemplate is added', () => {
        const notificationTemplateCollection: INotificationTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationTemplateToCollectionIfMissing(notificationTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(notificationTemplateCollection);
      });
    });

    describe('compareNotificationTemplate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25253 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationTemplate(entity1, entity2);
        const compareResult2 = service.compareNotificationTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25253 };
        const entity2 = { id: 29982 };

        const compareResult1 = service.compareNotificationTemplate(entity1, entity2);
        const compareResult2 = service.compareNotificationTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25253 };
        const entity2 = { id: 25253 };

        const compareResult1 = service.compareNotificationTemplate(entity1, entity2);
        const compareResult2 = service.compareNotificationTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
