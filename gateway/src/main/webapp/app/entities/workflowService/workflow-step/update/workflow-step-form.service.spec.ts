import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../workflow-step.test-samples';

import { WorkflowStepFormService } from './workflow-step-form.service';

describe('WorkflowStep Form Service', () => {
  let service: WorkflowStepFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkflowStepFormService);
  });

  describe('Service methods', () => {
    describe('createWorkflowStepFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkflowStepFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stepNumber: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            stepType: expect.any(Object),
            assigneeType: expect.any(Object),
            assigneeId: expect.any(Object),
            assigneeGroup: expect.any(Object),
            dueInDays: expect.any(Object),
            isRequired: expect.any(Object),
            canDelegate: expect.any(Object),
            canReject: expect.any(Object),
            configuration: expect.any(Object),
            workflow: expect.any(Object),
          }),
        );
      });

      it('passing IWorkflowStep should create a new form with FormGroup', () => {
        const formGroup = service.createWorkflowStepFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stepNumber: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            stepType: expect.any(Object),
            assigneeType: expect.any(Object),
            assigneeId: expect.any(Object),
            assigneeGroup: expect.any(Object),
            dueInDays: expect.any(Object),
            isRequired: expect.any(Object),
            canDelegate: expect.any(Object),
            canReject: expect.any(Object),
            configuration: expect.any(Object),
            workflow: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkflowStep', () => {
      it('should return NewWorkflowStep for default WorkflowStep initial value', () => {
        const formGroup = service.createWorkflowStepFormGroup(sampleWithNewData);

        const workflowStep = service.getWorkflowStep(formGroup) as any;

        expect(workflowStep).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkflowStep for empty WorkflowStep initial value', () => {
        const formGroup = service.createWorkflowStepFormGroup();

        const workflowStep = service.getWorkflowStep(formGroup) as any;

        expect(workflowStep).toMatchObject({});
      });

      it('should return IWorkflowStep', () => {
        const formGroup = service.createWorkflowStepFormGroup(sampleWithRequiredData);

        const workflowStep = service.getWorkflowStep(formGroup) as any;

        expect(workflowStep).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkflowStep should not enable id FormControl', () => {
        const formGroup = service.createWorkflowStepFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkflowStep should disable id FormControl', () => {
        const formGroup = service.createWorkflowStepFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
