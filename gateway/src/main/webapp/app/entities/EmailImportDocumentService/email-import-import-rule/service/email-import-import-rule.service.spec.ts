import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmailImportImportRule } from '../email-import-import-rule.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../email-import-import-rule.test-samples';

import { EmailImportImportRuleService, RestEmailImportImportRule } from './email-import-import-rule.service';

const requireRestSample: RestEmailImportImportRule = {
  ...sampleWithRequiredData,
  lastMatchDate: sampleWithRequiredData.lastMatchDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('EmailImportImportRule Service', () => {
  let service: EmailImportImportRuleService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmailImportImportRule | IEmailImportImportRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmailImportImportRuleService);
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

    it('should create a EmailImportImportRule', () => {
      const emailImportImportRule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emailImportImportRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmailImportImportRule', () => {
      const emailImportImportRule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emailImportImportRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmailImportImportRule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmailImportImportRule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmailImportImportRule', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a EmailImportImportRule', () => {
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

    describe('addEmailImportImportRuleToCollectionIfMissing', () => {
      it('should add a EmailImportImportRule to an empty array', () => {
        const emailImportImportRule: IEmailImportImportRule = sampleWithRequiredData;
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing([], emailImportImportRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportImportRule);
      });

      it('should not add a EmailImportImportRule to an array that contains it', () => {
        const emailImportImportRule: IEmailImportImportRule = sampleWithRequiredData;
        const emailImportImportRuleCollection: IEmailImportImportRule[] = [
          {
            ...emailImportImportRule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing(emailImportImportRuleCollection, emailImportImportRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmailImportImportRule to an array that doesn't contain it", () => {
        const emailImportImportRule: IEmailImportImportRule = sampleWithRequiredData;
        const emailImportImportRuleCollection: IEmailImportImportRule[] = [sampleWithPartialData];
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing(emailImportImportRuleCollection, emailImportImportRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportImportRule);
      });

      it('should add only unique EmailImportImportRule to an array', () => {
        const emailImportImportRuleArray: IEmailImportImportRule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const emailImportImportRuleCollection: IEmailImportImportRule[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing(
          emailImportImportRuleCollection,
          ...emailImportImportRuleArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emailImportImportRule: IEmailImportImportRule = sampleWithRequiredData;
        const emailImportImportRule2: IEmailImportImportRule = sampleWithPartialData;
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing([], emailImportImportRule, emailImportImportRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportImportRule);
        expect(expectedResult).toContain(emailImportImportRule2);
      });

      it('should accept null and undefined values', () => {
        const emailImportImportRule: IEmailImportImportRule = sampleWithRequiredData;
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing([], null, emailImportImportRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportImportRule);
      });

      it('should return initial array if no EmailImportImportRule is added', () => {
        const emailImportImportRuleCollection: IEmailImportImportRule[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportImportRuleToCollectionIfMissing(emailImportImportRuleCollection, undefined, null);
        expect(expectedResult).toEqual(emailImportImportRuleCollection);
      });
    });

    describe('compareEmailImportImportRule', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmailImportImportRule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12705 };
        const entity2 = null;

        const compareResult1 = service.compareEmailImportImportRule(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12705 };
        const entity2 = { id: 16106 };

        const compareResult1 = service.compareEmailImportImportRule(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12705 };
        const entity2 = { id: 12705 };

        const compareResult1 = service.compareEmailImportImportRule(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportRule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
