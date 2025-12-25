import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../workflow-instance.test-samples';

import { WorkflowInstanceFormService } from './workflow-instance-form.service';

describe('WorkflowInstance Form Service', () => {
  let service: WorkflowInstanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkflowInstanceFormService);
  });

  describe('Service methods', () => {
    describe('createWorkflowInstanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkflowInstanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            status: expect.any(Object),
            currentStepNumber: expect.any(Object),
            startDate: expect.any(Object),
            dueDate: expect.any(Object),
            completedDate: expect.any(Object),
            cancelledDate: expect.any(Object),
            cancellationReason: expect.any(Object),
            priority: expect.any(Object),
            metadata: expect.any(Object),
            createdBy: expect.any(Object),
            workflow: expect.any(Object),
          }),
        );
      });

      it('passing IWorkflowInstance should create a new form with FormGroup', () => {
        const formGroup = service.createWorkflowInstanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            status: expect.any(Object),
            currentStepNumber: expect.any(Object),
            startDate: expect.any(Object),
            dueDate: expect.any(Object),
            completedDate: expect.any(Object),
            cancelledDate: expect.any(Object),
            cancellationReason: expect.any(Object),
            priority: expect.any(Object),
            metadata: expect.any(Object),
            createdBy: expect.any(Object),
            workflow: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkflowInstance', () => {
      it('should return NewWorkflowInstance for default WorkflowInstance initial value', () => {
        const formGroup = service.createWorkflowInstanceFormGroup(sampleWithNewData);

        const workflowInstance = service.getWorkflowInstance(formGroup) as any;

        expect(workflowInstance).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkflowInstance for empty WorkflowInstance initial value', () => {
        const formGroup = service.createWorkflowInstanceFormGroup();

        const workflowInstance = service.getWorkflowInstance(formGroup) as any;

        expect(workflowInstance).toMatchObject({});
      });

      it('should return IWorkflowInstance', () => {
        const formGroup = service.createWorkflowInstanceFormGroup(sampleWithRequiredData);

        const workflowInstance = service.getWorkflowInstance(formGroup) as any;

        expect(workflowInstance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkflowInstance should not enable id FormControl', () => {
        const formGroup = service.createWorkflowInstanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkflowInstance should disable id FormControl', () => {
        const formGroup = service.createWorkflowInstanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
