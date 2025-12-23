import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBankStatement } from '../bank-statement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bank-statement.test-samples';

import { BankStatementService, RestBankStatement } from './bank-statement.service';

const requireRestSample: RestBankStatement = {
  ...sampleWithRequiredData,
  statementDate: sampleWithRequiredData.statementDate?.format(DATE_FORMAT),
  statementPeriodStart: sampleWithRequiredData.statementPeriodStart?.format(DATE_FORMAT),
  statementPeriodEnd: sampleWithRequiredData.statementPeriodEnd?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('BankStatement Service', () => {
  let service: BankStatementService;
  let httpMock: HttpTestingController;
  let expectedResult: IBankStatement | IBankStatement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BankStatementService);
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

    it('should create a BankStatement', () => {
      const bankStatement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bankStatement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankStatement', () => {
      const bankStatement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bankStatement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankStatement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankStatement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BankStatement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBankStatementToCollectionIfMissing', () => {
      it('should add a BankStatement to an empty array', () => {
        const bankStatement: IBankStatement = sampleWithRequiredData;
        expectedResult = service.addBankStatementToCollectionIfMissing([], bankStatement);
        expect(expectedResult).toEqual([bankStatement]);
      });

      it('should not add a BankStatement to an array that contains it', () => {
        const bankStatement: IBankStatement = sampleWithRequiredData;
        const bankStatementCollection: IBankStatement[] = [
          {
            ...bankStatement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBankStatementToCollectionIfMissing(bankStatementCollection, bankStatement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankStatement to an array that doesn't contain it", () => {
        const bankStatement: IBankStatement = sampleWithRequiredData;
        const bankStatementCollection: IBankStatement[] = [sampleWithPartialData];
        expectedResult = service.addBankStatementToCollectionIfMissing(bankStatementCollection, bankStatement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankStatement);
      });

      it('should add only unique BankStatement to an array', () => {
        const bankStatementArray: IBankStatement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bankStatementCollection: IBankStatement[] = [sampleWithRequiredData];
        expectedResult = service.addBankStatementToCollectionIfMissing(bankStatementCollection, ...bankStatementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankStatement: IBankStatement = sampleWithRequiredData;
        const bankStatement2: IBankStatement = sampleWithPartialData;
        expectedResult = service.addBankStatementToCollectionIfMissing([], bankStatement, bankStatement2);
        expect(expectedResult).toEqual([bankStatement, bankStatement2]);
      });

      it('should accept null and undefined values', () => {
        const bankStatement: IBankStatement = sampleWithRequiredData;
        expectedResult = service.addBankStatementToCollectionIfMissing([], null, bankStatement, undefined);
        expect(expectedResult).toEqual([bankStatement]);
      });

      it('should return initial array if no BankStatement is added', () => {
        const bankStatementCollection: IBankStatement[] = [sampleWithRequiredData];
        expectedResult = service.addBankStatementToCollectionIfMissing(bankStatementCollection, undefined, null);
        expect(expectedResult).toEqual(bankStatementCollection);
      });
    });

    describe('compareBankStatement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBankStatement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32402 };
        const entity2 = null;

        const compareResult1 = service.compareBankStatement(entity1, entity2);
        const compareResult2 = service.compareBankStatement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32402 };
        const entity2 = { id: 28585 };

        const compareResult1 = service.compareBankStatement(entity1, entity2);
        const compareResult2 = service.compareBankStatement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32402 };
        const entity2 = { id: 32402 };

        const compareResult1 = service.compareBankStatement(entity1, entity2);
        const compareResult2 = service.compareBankStatement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
