import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWorkflowApprovalHistory } from '../workflow-approval-history.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../workflow-approval-history.test-samples';

import { RestWorkflowApprovalHistory, WorkflowApprovalHistoryService } from './workflow-approval-history.service';

const requireRestSample: RestWorkflowApprovalHistory = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('WorkflowApprovalHistory Service', () => {
  let service: WorkflowApprovalHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkflowApprovalHistory | IWorkflowApprovalHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorkflowApprovalHistoryService);
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

    it('should create a WorkflowApprovalHistory', () => {
      const workflowApprovalHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workflowApprovalHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkflowApprovalHistory', () => {
      const workflowApprovalHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workflowApprovalHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkflowApprovalHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkflowApprovalHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkflowApprovalHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a WorkflowApprovalHistory', () => {
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

    describe('addWorkflowApprovalHistoryToCollectionIfMissing', () => {
      it('should add a WorkflowApprovalHistory to an empty array', () => {
        const workflowApprovalHistory: IWorkflowApprovalHistory = sampleWithRequiredData;
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing([], workflowApprovalHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workflowApprovalHistory);
      });

      it('should not add a WorkflowApprovalHistory to an array that contains it', () => {
        const workflowApprovalHistory: IWorkflowApprovalHistory = sampleWithRequiredData;
        const workflowApprovalHistoryCollection: IWorkflowApprovalHistory[] = [
          {
            ...workflowApprovalHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing(
          workflowApprovalHistoryCollection,
          workflowApprovalHistory,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkflowApprovalHistory to an array that doesn't contain it", () => {
        const workflowApprovalHistory: IWorkflowApprovalHistory = sampleWithRequiredData;
        const workflowApprovalHistoryCollection: IWorkflowApprovalHistory[] = [sampleWithPartialData];
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing(
          workflowApprovalHistoryCollection,
          workflowApprovalHistory,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflowApprovalHistory);
      });

      it('should add only unique WorkflowApprovalHistory to an array', () => {
        const workflowApprovalHistoryArray: IWorkflowApprovalHistory[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const workflowApprovalHistoryCollection: IWorkflowApprovalHistory[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing(
          workflowApprovalHistoryCollection,
          ...workflowApprovalHistoryArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workflowApprovalHistory: IWorkflowApprovalHistory = sampleWithRequiredData;
        const workflowApprovalHistory2: IWorkflowApprovalHistory = sampleWithPartialData;
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing([], workflowApprovalHistory, workflowApprovalHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflowApprovalHistory);
        expect(expectedResult).toContain(workflowApprovalHistory2);
      });

      it('should accept null and undefined values', () => {
        const workflowApprovalHistory: IWorkflowApprovalHistory = sampleWithRequiredData;
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing([], null, workflowApprovalHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workflowApprovalHistory);
      });

      it('should return initial array if no WorkflowApprovalHistory is added', () => {
        const workflowApprovalHistoryCollection: IWorkflowApprovalHistory[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowApprovalHistoryToCollectionIfMissing(workflowApprovalHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(workflowApprovalHistoryCollection);
      });
    });

    describe('compareWorkflowApprovalHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkflowApprovalHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17441 };
        const entity2 = null;

        const compareResult1 = service.compareWorkflowApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareWorkflowApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17441 };
        const entity2 = { id: 22100 };

        const compareResult1 = service.compareWorkflowApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareWorkflowApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17441 };
        const entity2 = { id: 17441 };

        const compareResult1 = service.compareWorkflowApprovalHistory(entity1, entity2);
        const compareResult2 = service.compareWorkflowApprovalHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
