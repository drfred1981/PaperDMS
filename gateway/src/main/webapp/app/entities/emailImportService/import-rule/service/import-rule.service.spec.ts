import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IImportRule } from '../import-rule.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../import-rule.test-samples';

import { ImportRuleService, RestImportRule } from './import-rule.service';

const requireRestSample: RestImportRule = {
  ...sampleWithRequiredData,
  lastMatchDate: sampleWithRequiredData.lastMatchDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ImportRule Service', () => {
  let service: ImportRuleService;
  let httpMock: HttpTestingController;
  let expectedResult: IImportRule | IImportRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImportRuleService);
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

    it('should create a ImportRule', () => {
      const importRule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(importRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImportRule', () => {
      const importRule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(importRule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImportRule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImportRule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImportRule', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImportRuleToCollectionIfMissing', () => {
      it('should add a ImportRule to an empty array', () => {
        const importRule: IImportRule = sampleWithRequiredData;
        expectedResult = service.addImportRuleToCollectionIfMissing([], importRule);
        expect(expectedResult).toEqual([importRule]);
      });

      it('should not add a ImportRule to an array that contains it', () => {
        const importRule: IImportRule = sampleWithRequiredData;
        const importRuleCollection: IImportRule[] = [
          {
            ...importRule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImportRuleToCollectionIfMissing(importRuleCollection, importRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImportRule to an array that doesn't contain it", () => {
        const importRule: IImportRule = sampleWithRequiredData;
        const importRuleCollection: IImportRule[] = [sampleWithPartialData];
        expectedResult = service.addImportRuleToCollectionIfMissing(importRuleCollection, importRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(importRule);
      });

      it('should add only unique ImportRule to an array', () => {
        const importRuleArray: IImportRule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const importRuleCollection: IImportRule[] = [sampleWithRequiredData];
        expectedResult = service.addImportRuleToCollectionIfMissing(importRuleCollection, ...importRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const importRule: IImportRule = sampleWithRequiredData;
        const importRule2: IImportRule = sampleWithPartialData;
        expectedResult = service.addImportRuleToCollectionIfMissing([], importRule, importRule2);
        expect(expectedResult).toEqual([importRule, importRule2]);
      });

      it('should accept null and undefined values', () => {
        const importRule: IImportRule = sampleWithRequiredData;
        expectedResult = service.addImportRuleToCollectionIfMissing([], null, importRule, undefined);
        expect(expectedResult).toEqual([importRule]);
      });

      it('should return initial array if no ImportRule is added', () => {
        const importRuleCollection: IImportRule[] = [sampleWithRequiredData];
        expectedResult = service.addImportRuleToCollectionIfMissing(importRuleCollection, undefined, null);
        expect(expectedResult).toEqual(importRuleCollection);
      });
    });

    describe('compareImportRule', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImportRule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3928 };
        const entity2 = null;

        const compareResult1 = service.compareImportRule(entity1, entity2);
        const compareResult2 = service.compareImportRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3928 };
        const entity2 = { id: 22098 };

        const compareResult1 = service.compareImportRule(entity1, entity2);
        const compareResult2 = service.compareImportRule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3928 };
        const entity2 = { id: 3928 };

        const compareResult1 = service.compareImportRule(entity1, entity2);
        const compareResult2 = service.compareImportRule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
