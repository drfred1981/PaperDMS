import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IWorkflowStep } from '../workflow-step.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../workflow-step.test-samples';

import { WorkflowStepService } from './workflow-step.service';

const requireRestSample: IWorkflowStep = {
  ...sampleWithRequiredData,
};

describe('WorkflowStep Service', () => {
  let service: WorkflowStepService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkflowStep | IWorkflowStep[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorkflowStepService);
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

    it('should create a WorkflowStep', () => {
      const workflowStep = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workflowStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkflowStep', () => {
      const workflowStep = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workflowStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkflowStep', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkflowStep', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkflowStep', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkflowStepToCollectionIfMissing', () => {
      it('should add a WorkflowStep to an empty array', () => {
        const workflowStep: IWorkflowStep = sampleWithRequiredData;
        expectedResult = service.addWorkflowStepToCollectionIfMissing([], workflowStep);
        expect(expectedResult).toEqual([workflowStep]);
      });

      it('should not add a WorkflowStep to an array that contains it', () => {
        const workflowStep: IWorkflowStep = sampleWithRequiredData;
        const workflowStepCollection: IWorkflowStep[] = [
          {
            ...workflowStep,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkflowStepToCollectionIfMissing(workflowStepCollection, workflowStep);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkflowStep to an array that doesn't contain it", () => {
        const workflowStep: IWorkflowStep = sampleWithRequiredData;
        const workflowStepCollection: IWorkflowStep[] = [sampleWithPartialData];
        expectedResult = service.addWorkflowStepToCollectionIfMissing(workflowStepCollection, workflowStep);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workflowStep);
      });

      it('should add only unique WorkflowStep to an array', () => {
        const workflowStepArray: IWorkflowStep[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workflowStepCollection: IWorkflowStep[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowStepToCollectionIfMissing(workflowStepCollection, ...workflowStepArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workflowStep: IWorkflowStep = sampleWithRequiredData;
        const workflowStep2: IWorkflowStep = sampleWithPartialData;
        expectedResult = service.addWorkflowStepToCollectionIfMissing([], workflowStep, workflowStep2);
        expect(expectedResult).toEqual([workflowStep, workflowStep2]);
      });

      it('should accept null and undefined values', () => {
        const workflowStep: IWorkflowStep = sampleWithRequiredData;
        expectedResult = service.addWorkflowStepToCollectionIfMissing([], null, workflowStep, undefined);
        expect(expectedResult).toEqual([workflowStep]);
      });

      it('should return initial array if no WorkflowStep is added', () => {
        const workflowStepCollection: IWorkflowStep[] = [sampleWithRequiredData];
        expectedResult = service.addWorkflowStepToCollectionIfMissing(workflowStepCollection, undefined, null);
        expect(expectedResult).toEqual(workflowStepCollection);
      });
    });

    describe('compareWorkflowStep', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkflowStep(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22856 };
        const entity2 = null;

        const compareResult1 = service.compareWorkflowStep(entity1, entity2);
        const compareResult2 = service.compareWorkflowStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22856 };
        const entity2 = { id: 27664 };

        const compareResult1 = service.compareWorkflowStep(entity1, entity2);
        const compareResult2 = service.compareWorkflowStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22856 };
        const entity2 = { id: 22856 };

        const compareResult1 = service.compareWorkflowStep(entity1, entity2);
        const compareResult2 = service.compareWorkflowStep(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
