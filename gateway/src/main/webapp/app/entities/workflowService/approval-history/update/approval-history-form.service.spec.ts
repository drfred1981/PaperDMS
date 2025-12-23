import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../approval-history.test-samples';

import { ApprovalHistoryFormService } from './approval-history-form.service';

describe('ApprovalHistory Form Service', () => {
  let service: ApprovalHistoryFormService;

  beforeEach(() => {
    service = TestBed.inject(ApprovalHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createApprovalHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApprovalHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            workflowInstanceId: expect.any(Object),
            stepNumber: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            actionDate: expect.any(Object),
            actionBy: expect.any(Object),
            previousAssignee: expect.any(Object),
            timeTaken: expect.any(Object),
          }),
        );
      });

      it('passing IApprovalHistory should create a new form with FormGroup', () => {
        const formGroup = service.createApprovalHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            workflowInstanceId: expect.any(Object),
            stepNumber: expect.any(Object),
            action: expect.any(Object),
            comment: expect.any(Object),
            actionDate: expect.any(Object),
            actionBy: expect.any(Object),
            previousAssignee: expect.any(Object),
            timeTaken: expect.any(Object),
          }),
        );
      });
    });

    describe('getApprovalHistory', () => {
      it('should return NewApprovalHistory for default ApprovalHistory initial value', () => {
        const formGroup = service.createApprovalHistoryFormGroup(sampleWithNewData);

        const approvalHistory = service.getApprovalHistory(formGroup);

        expect(approvalHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewApprovalHistory for empty ApprovalHistory initial value', () => {
        const formGroup = service.createApprovalHistoryFormGroup();

        const approvalHistory = service.getApprovalHistory(formGroup);

        expect(approvalHistory).toMatchObject({});
      });

      it('should return IApprovalHistory', () => {
        const formGroup = service.createApprovalHistoryFormGroup(sampleWithRequiredData);

        const approvalHistory = service.getApprovalHistory(formGroup);

        expect(approvalHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApprovalHistory should not enable id FormControl', () => {
        const formGroup = service.createApprovalHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApprovalHistory should disable id FormControl', () => {
        const formGroup = service.createApprovalHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
