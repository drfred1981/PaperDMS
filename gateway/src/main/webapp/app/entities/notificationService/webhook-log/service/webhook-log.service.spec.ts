import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWebhookLog } from '../webhook-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../webhook-log.test-samples';

import { RestWebhookLog, WebhookLogService } from './webhook-log.service';

const requireRestSample: RestWebhookLog = {
  ...sampleWithRequiredData,
  sentDate: sampleWithRequiredData.sentDate?.toJSON(),
};

describe('WebhookLog Service', () => {
  let service: WebhookLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IWebhookLog | IWebhookLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WebhookLogService);
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

    it('should create a WebhookLog', () => {
      const webhookLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(webhookLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WebhookLog', () => {
      const webhookLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(webhookLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WebhookLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WebhookLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WebhookLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWebhookLogToCollectionIfMissing', () => {
      it('should add a WebhookLog to an empty array', () => {
        const webhookLog: IWebhookLog = sampleWithRequiredData;
        expectedResult = service.addWebhookLogToCollectionIfMissing([], webhookLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(webhookLog);
      });

      it('should not add a WebhookLog to an array that contains it', () => {
        const webhookLog: IWebhookLog = sampleWithRequiredData;
        const webhookLogCollection: IWebhookLog[] = [
          {
            ...webhookLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWebhookLogToCollectionIfMissing(webhookLogCollection, webhookLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WebhookLog to an array that doesn't contain it", () => {
        const webhookLog: IWebhookLog = sampleWithRequiredData;
        const webhookLogCollection: IWebhookLog[] = [sampleWithPartialData];
        expectedResult = service.addWebhookLogToCollectionIfMissing(webhookLogCollection, webhookLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(webhookLog);
      });

      it('should add only unique WebhookLog to an array', () => {
        const webhookLogArray: IWebhookLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const webhookLogCollection: IWebhookLog[] = [sampleWithRequiredData];
        expectedResult = service.addWebhookLogToCollectionIfMissing(webhookLogCollection, ...webhookLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const webhookLog: IWebhookLog = sampleWithRequiredData;
        const webhookLog2: IWebhookLog = sampleWithPartialData;
        expectedResult = service.addWebhookLogToCollectionIfMissing([], webhookLog, webhookLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(webhookLog);
        expect(expectedResult).toContain(webhookLog2);
      });

      it('should accept null and undefined values', () => {
        const webhookLog: IWebhookLog = sampleWithRequiredData;
        expectedResult = service.addWebhookLogToCollectionIfMissing([], null, webhookLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(webhookLog);
      });

      it('should return initial array if no WebhookLog is added', () => {
        const webhookLogCollection: IWebhookLog[] = [sampleWithRequiredData];
        expectedResult = service.addWebhookLogToCollectionIfMissing(webhookLogCollection, undefined, null);
        expect(expectedResult).toEqual(webhookLogCollection);
      });
    });

    describe('compareWebhookLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWebhookLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27220 };
        const entity2 = null;

        const compareResult1 = service.compareWebhookLog(entity1, entity2);
        const compareResult2 = service.compareWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27220 };
        const entity2 = { id: 26019 };

        const compareResult1 = service.compareWebhookLog(entity1, entity2);
        const compareResult2 = service.compareWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27220 };
        const entity2 = { id: 27220 };

        const compareResult1 = service.compareWebhookLog(entity1, entity2);
        const compareResult2 = service.compareWebhookLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
