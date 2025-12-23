import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IApprovalHistory } from '../approval-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../approval-history.test-samples';

import { ApprovalHistoryService, RestApprovalHistory } from './approval-history.service';

const requireRestSample: RestApprovalHistory = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('ApprovalHistory Service', () => {
  let service: ApprovalHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IApprovalHistory | IApprovalHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ApprovalHistoryService);
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

    it('should create a ApprovalHistory', () => {
      const approvalHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(approvalHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApprovalHistory', () => {
      const approvalHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(approvalHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApprovalHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApprovalHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApprovalHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApprovalHistoryToCollectionIfMissing', () => {
      it('should add a ApprovalHistory to an empty array', () => {
        const approvalHistory: IApprovalHistory = sampleWithRequiredData;
        expectedResult = service.addApprovalHistoryToCollectionIfMissing([], approvalHistory);
        expect(expectedResult).toEqual([approvalHistory]);
      });

      it('should not add a ApprovalHistory to an array that contains it', () => {
        const approvalHistory: IApprovalHistory = sampleWithRequiredData;
        const approvalHistoryCollection: IApprovalHistory[] = [
          {
            ...approvalHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApprovalHistoryToCollectionIfMissing(approvalHistoryCollection, approvalHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApprovalHistory to an array that doesn't contain it", () => {
        const approvalHistory: IApprovalHistory = sampleWithRequiredData;
        const approvalHistoryCollection: IApprovalHistory[] = [sampleWithPartialData];
        expectedResult = service.addApprovalHistoryToCollectionIfMissing(approvalHistoryCollection, approvalHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(approvalHistory);
      });

      it('should add only unique ApprovalHistory to an array', () => {
        const approvalHistoryArray: IApprovalHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const approvalHistoryCollection: IApprovalHistory[] = [sampleWithRequiredData];
        expectedResult = service.addApprovalHistoryToCollectionIfMissing(approvalHistoryCollection, ...approvalHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const approvalHistory: IApprovalHistory = sampleWithRequiredData;
        const approvalHistory2: IApprovalHistory = sampleWithPartialData;
        expectedResult = service.addApprovalHistoryToCollectionIfMissing([], approvalHistory, approvalHistory2);
        expect(expectedResult).toEqual([approvalHistory, approvalHistory2]);
      });

      it('should accept null and undefined values', () => {
        const approvalHistory: IApprovalHistory = sampleWithRequiredData;
        expectedResult = service.addApprovalHistoryToCollectionIfMissing([], null, approvalHistory, undefined);
        expect(expectedResult).toEqual([approvalHistory]);
      });

      it('should return initial array if no ApprovalHistory is added', () => {
        const approvalHistoryCollection: IApprovalHistory[] = [sampleWithRequiredData];
        expectedResult = service.addApprovalHistoryToCollectionIfMissing(approvalHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(approvalHistoryCollection);
      });
    });

    describe('compareApprovalHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApprovalHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15176 };
        const entity2 = null;

        const compareResult1 = service.compareApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15176 };
        const entity2 = { id: 10065 };

        const compareResult1 = service.compareApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15176 };
        const entity2 = { id: 15176 };

        const compareResult1 = service.compareApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
