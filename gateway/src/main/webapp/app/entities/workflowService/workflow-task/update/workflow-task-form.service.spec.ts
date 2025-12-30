import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../workflow-task.test-samples';

import { WorkflowTaskFormService } from './workflow-task-form.service';

describe('WorkflowTask Form Service', () => {
  let service: WorkflowTaskFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkflowTaskFormService);
  });

  describe('Service methods', () => {
    describe('createWorkflowTaskFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkflowTaskFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assigneeId: expect.any(Object),
            status: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            assignedDate: expect.any(Object),
            dueDate: expect.any(Object),
            completedDate: expect.any(Object),
            reminderSent: expect.any(Object),
            delegatedTo: expect.any(Object),
            delegatedDate: expect.any(Object),
            instance: expect.any(Object),
            step: expect.any(Object),
          }),
        );
      });

      it('passing IWorkflowTask should create a new form with FormGroup', () => {
        const formGroup = service.createWorkflowTaskFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assigneeId: expect.any(Object),
            status: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            assignedDate: expect.any(Object),
            dueDate: expect.any(Object),
            completedDate: expect.any(Object),
            reminderSent: expect.any(Object),
            delegatedTo: expect.any(Object),
            delegatedDate: expect.any(Object),
            instance: expect.any(Object),
            step: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkflowTask', () => {
      it('should return NewWorkflowTask for default WorkflowTask initial value', () => {
        const formGroup = service.createWorkflowTaskFormGroup(sampleWithNewData);

        const workflowTask = service.getWorkflowTask(formGroup) as any;

        expect(workflowTask).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkflowTask for empty WorkflowTask initial value', () => {
        const formGroup = service.createWorkflowTaskFormGroup();

        const workflowTask = service.getWorkflowTask(formGroup) as any;

        expect(workflowTask).toMatchObject({});
      });

      it('should return IWorkflowTask', () => {
        const formGroup = service.createWorkflowTaskFormGroup(sampleWithRequiredData);

        const workflowTask = service.getWorkflowTask(formGroup) as any;

        expect(workflowTask).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkflowTask should not enable id FormControl', () => {
        const formGroup = service.createWorkflowTaskFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkflowTask should disable id FormControl', () => {
        const formGroup = service.createWorkflowTaskFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
