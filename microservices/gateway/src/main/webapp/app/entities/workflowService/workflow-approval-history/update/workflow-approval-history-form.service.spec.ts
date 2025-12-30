import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../workflow-approval-history.test-samples';

import { WorkflowApprovalHistoryFormService } from './workflow-approval-history-form.service';

describe('WorkflowApprovalHistory Form Service', () => {
  let service: WorkflowApprovalHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkflowApprovalHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createWorkflowApprovalHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            stepNumber: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            actionDate: expect.any(Object),
            actionBy: expect.any(Object),
            previousAssignee: expect.any(Object),
            timeTaken: expect.any(Object),
            workflowInstance: expect.any(Object),
          }),
        );
      });

      it('passing IWorkflowApprovalHistory should create a new form with FormGroup', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            stepNumber: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            actionDate: expect.any(Object),
            actionBy: expect.any(Object),
            previousAssignee: expect.any(Object),
            timeTaken: expect.any(Object),
            workflowInstance: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkflowApprovalHistory', () => {
      it('should return NewWorkflowApprovalHistory for default WorkflowApprovalHistory initial value', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup(sampleWithNewData);

        const workflowApprovalHistory = service.getWorkflowApprovalHistory(formGroup) as any;

        expect(workflowApprovalHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkflowApprovalHistory for empty WorkflowApprovalHistory initial value', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup();

        const workflowApprovalHistory = service.getWorkflowApprovalHistory(formGroup) as any;

        expect(workflowApprovalHistory).toMatchObject({});
      });

      it('should return IWorkflowApprovalHistory', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup(sampleWithRequiredData);

        const workflowApprovalHistory = service.getWorkflowApprovalHistory(formGroup) as any;

        expect(workflowApprovalHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkflowApprovalHistory should not enable id FormControl', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkflowApprovalHistory should disable id FormControl', () => {
        const formGroup = service.createWorkflowApprovalHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
