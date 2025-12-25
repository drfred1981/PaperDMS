import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWorkflowInstance } from '../workflow-instance.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../workflow-instance.test-samples';

import { RestWorkflowInstance, WorkflowInstanceService } from './workflow-instance.service';

const requireRestSample: RestWorkflowInstance = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  dueDate: sampleWithRequiredData.dueDate?.toJSON(),
  completedDate: sampleWithRequiredData.completedDate?.toJSON(),
  cancelledDate: sampleWithRequiredData.cancelledDate?.toJSON(),
};

describe('WorkflowInstance Service', () => {
  let service: WorkflowInstanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkflowInstance | IWorkflowInstance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorkflowInstanceService);
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

    it('should create a WorkflowInstance', () => {
      const workflowInstance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workflowInstance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkflowInstance', () => {
      const workflowInstance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workflowInstance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkflowInstance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkflowInstance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkflowInstance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkflowInstanceToCollectionIfMissing', () => {
      it('should add a WorkflowInstance to an empty array', () => {
        const workflowInstance: IWorkflowInstance = sampleWithRequiredData;
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing([], workflowInstance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workflowInstance);
      });

      it('should not add a WorkflowInstance to an array that contains it', () => {
        const workflowInstance: IWorkflowInstance = sampleWithRequiredData;
        const workflowInstanceCollection: IWorkflowInstance[] = [
          {
            ...workflowInstance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing(workflowInstanceCollection, workflowInstance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkflowInstance to an array that doesn't contain it", () => {
        const workflowInstance: IWorkflowInstance = sampleWithRequiredData;
        const workflowInstanceCollection: IWorkflowInstance[] = [sampleWithPartialData];
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing(workflowInstanceCollection, workflowInstance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflowInstance);
      });

      it('should add only unique WorkflowInstance to an array', () => {
        const workflowInstanceArray: IWorkflowInstance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workflowInstanceCollection: IWorkflowInstance[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing(workflowInstanceCollection, ...workflowInstanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workflowInstance: IWorkflowInstance = sampleWithRequiredData;
        const workflowInstance2: IWorkflowInstance = sampleWithPartialData;
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing([], workflowInstance, workflowInstance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflowInstance);
        expect(expectedResult).toContain(workflowInstance2);
      });

      it('should accept null and undefined values', () => {
        const workflowInstance: IWorkflowInstance = sampleWithRequiredData;
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing([], null, workflowInstance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workflowInstance);
      });

      it('should return initial array if no WorkflowInstance is added', () => {
        const workflowInstanceCollection: IWorkflowInstance[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowInstanceToCollectionIfMissing(workflowInstanceCollection, undefined, null);
        expect(expectedResult).toEqual(workflowInstanceCollection);
      });
    });

    describe('compareWorkflowInstance', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkflowInstance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15006 };
        const entity2 = null;

        const compareResult1 = service.compareWorkflowInstance(entity1, entity2);
        const compareResult2 = service.compareWorkflowInstance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15006 };
        const entity2 = { id: 21506 };

        const compareResult1 = service.compareWorkflowInstance(entity1, entity2);
        const compareResult2 = service.compareWorkflowInstance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15006 };
        const entity2 = { id: 15006 };

        const compareResult1 = service.compareWorkflowInstance(entity1, entity2);
        const compareResult2 = service.compareWorkflowInstance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
