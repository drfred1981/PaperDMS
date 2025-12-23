import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IWorkflow } from '../workflow.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../workflow.test-samples';

import { RestWorkflow, WorkflowService } from './workflow.service';

const requireRestSample: RestWorkflow = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Workflow Service', () => {
  let service: WorkflowService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkflow | IWorkflow[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorkflowService);
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

    it('should create a Workflow', () => {
      const workflow = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workflow).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Workflow', () => {
      const workflow = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workflow).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Workflow', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Workflow', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Workflow', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Workflow', () => {
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

    describe('addWorkflowToCollectionIfMissing', () => {
      it('should add a Workflow to an empty array', () => {
        const workflow: IWorkflow = sampleWithRequiredData;
        expectedResult = service.addWorkflowToCollectionIfMissing([], workflow);
        expect(expectedResult).toEqual([workflow]);
      });

      it('should not add a Workflow to an array that contains it', () => {
        const workflow: IWorkflow = sampleWithRequiredData;
        const workflowCollection: IWorkflow[] = [
          {
            ...workflow,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkflowToCollectionIfMissing(workflowCollection, workflow);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Workflow to an array that doesn't contain it", () => {
        const workflow: IWorkflow = sampleWithRequiredData;
        const workflowCollection: IWorkflow[] = [sampleWithPartialData];
        expectedResult = service.addWorkflowToCollectionIfMissing(workflowCollection, workflow);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflow);
      });

      it('should add only unique Workflow to an array', () => {
        const workflowArray: IWorkflow[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workflowCollection: IWorkflow[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowToCollectionIfMissing(workflowCollection, ...workflowArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workflow: IWorkflow = sampleWithRequiredData;
        const workflow2: IWorkflow = sampleWithPartialData;
        expectedResult = service.addWorkflowToCollectionIfMissing([], workflow, workflow2);
        expect(expectedResult).toEqual([workflow, workflow2]);
      });

      it('should accept null and undefined values', () => {
        const workflow: IWorkflow = sampleWithRequiredData;
        expectedResult = service.addWorkflowToCollectionIfMissing([], null, workflow, undefined);
        expect(expectedResult).toEqual([workflow]);
      });

      it('should return initial array if no Workflow is added', () => {
        const workflowCollection: IWorkflow[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowToCollectionIfMissing(workflowCollection, undefined, null);
        expect(expectedResult).toEqual(workflowCollection);
      });
    });

    describe('compareWorkflow', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkflow(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11987 };
        const entity2 = null;

        const compareResult1 = service.compareWorkflow(entity1, entity2);
        const compareResult2 = service.compareWorkflow(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11987 };
        const entity2 = { id: 32445 };

        const compareResult1 = service.compareWorkflow(entity1, entity2);
        const compareResult2 = service.compareWorkflow(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11987 };
        const entity2 = { id: 11987 };

        const compareResult1 = service.compareWorkflow(entity1, entity2);
        const compareResult2 = service.compareWorkflow(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
