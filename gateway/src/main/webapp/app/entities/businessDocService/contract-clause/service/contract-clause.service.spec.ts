import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContractClause } from '../contract-clause.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contract-clause.test-samples';

import { ContractClauseService } from './contract-clause.service';

const requireRestSample: IContractClause = {
  ...sampleWithRequiredData,
};

describe('ContractClause Service', () => {
  let service: ContractClauseService;
  let httpMock: HttpTestingController;
  let expectedResult: IContractClause | IContractClause[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContractClauseService);
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

    it('should create a ContractClause', () => {
      const contractClause = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contractClause).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractClause', () => {
      const contractClause = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contractClause).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractClause', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractClause', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContractClause', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContractClauseToCollectionIfMissing', () => {
      it('should add a ContractClause to an empty array', () => {
        const contractClause: IContractClause = sampleWithRequiredData;
        expectedResult = service.addContractClauseToCollectionIfMissing([], contractClause);
        expect(expectedResult).toEqual([contractClause]);
      });

      it('should not add a ContractClause to an array that contains it', () => {
        const contractClause: IContractClause = sampleWithRequiredData;
        const contractClauseCollection: IContractClause[] = [
          {
            ...contractClause,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractClauseToCollectionIfMissing(contractClauseCollection, contractClause);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractClause to an array that doesn't contain it", () => {
        const contractClause: IContractClause = sampleWithRequiredData;
        const contractClauseCollection: IContractClause[] = [sampleWithPartialData];
        expectedResult = service.addContractClauseToCollectionIfMissing(contractClauseCollection, contractClause);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractClause);
      });

      it('should add only unique ContractClause to an array', () => {
        const contractClauseArray: IContractClause[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractClauseCollection: IContractClause[] = [sampleWithRequiredData];
        expectedResult = service.addContractClauseToCollectionIfMissing(contractClauseCollection, ...contractClauseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractClause: IContractClause = sampleWithRequiredData;
        const contractClause2: IContractClause = sampleWithPartialData;
        expectedResult = service.addContractClauseToCollectionIfMissing([], contractClause, contractClause2);
        expect(expectedResult).toEqual([contractClause, contractClause2]);
      });

      it('should accept null and undefined values', () => {
        const contractClause: IContractClause = sampleWithRequiredData;
        expectedResult = service.addContractClauseToCollectionIfMissing([], null, contractClause, undefined);
        expect(expectedResult).toEqual([contractClause]);
      });

      it('should return initial array if no ContractClause is added', () => {
        const contractClauseCollection: IContractClause[] = [sampleWithRequiredData];
        expectedResult = service.addContractClauseToCollectionIfMissing(contractClauseCollection, undefined, null);
        expect(expectedResult).toEqual(contractClauseCollection);
      });
    });

    describe('compareContractClause', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContractClause(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6360 };
        const entity2 = null;

        const compareResult1 = service.compareContractClause(entity1, entity2);
        const compareResult2 = service.compareContractClause(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6360 };
        const entity2 = { id: 5317 };

        const compareResult1 = service.compareContractClause(entity1, entity2);
        const compareResult2 = service.compareContractClause(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6360 };
        const entity2 = { id: 6360 };

        const compareResult1 = service.compareContractClause(entity1, entity2);
        const compareResult2 = service.compareContractClause(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
