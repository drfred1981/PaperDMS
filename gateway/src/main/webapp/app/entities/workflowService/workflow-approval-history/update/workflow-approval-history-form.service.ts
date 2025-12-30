import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkflowApprovalHistory, NewWorkflowApprovalHistory } from '../workflow-approval-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkflowApprovalHistory for edit and NewWorkflowApprovalHistoryFormGroupInput for create.
 */
type WorkflowApprovalHistoryFormGroupInput = IWorkflowApprovalHistory | PartialWithRequiredKeyOf<NewWorkflowApprovalHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkflowApprovalHistory | NewWorkflowApprovalHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type WorkflowApprovalHistoryFormRawValue = FormValueOf<IWorkflowApprovalHistory>;

type NewWorkflowApprovalHistoryFormRawValue = FormValueOf<NewWorkflowApprovalHistory>;

type WorkflowApprovalHistoryFormDefaults = Pick<NewWorkflowApprovalHistory, 'id' | 'actionDate'>;

type WorkflowApprovalHistoryFormGroupContent = {
  id: FormControl<WorkflowApprovalHistoryFormRawValue['id'] | NewWorkflowApprovalHistory['id']>;
  documentSha256: FormControl<WorkflowApprovalHistoryFormRawValue['documentSha256']>;
  stepNumber: FormControl<WorkflowApprovalHistoryFormRawValue['stepNumber']>;
  action: FormControl<WorkflowApprovalHistoryFormRawValue['action']>;
  comment: FormControl<WorkflowApprovalHistoryFormRawValue['comment']>;
  actionDate: FormControl<WorkflowApprovalHistoryFormRawValue['actionDate']>;
  actionBy: FormControl<WorkflowApprovalHistoryFormRawValue['actionBy']>;
  previousAssignee: FormControl<WorkflowApprovalHistoryFormRawValue['previousAssignee']>;
  timeTaken: FormControl<WorkflowApprovalHistoryFormRawValue['timeTaken']>;
  workflowInstance: FormControl<WorkflowApprovalHistoryFormRawValue['workflowInstance']>;
};

export type WorkflowApprovalHistoryFormGroup = FormGroup<WorkflowApprovalHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkflowApprovalHistoryFormService {
  createWorkflowApprovalHistoryFormGroup(
    workflowApprovalHistory: WorkflowApprovalHistoryFormGroupInput = { id: null },
  ): WorkflowApprovalHistoryFormGroup {
    const workflowApprovalHistoryRawValue = this.convertWorkflowApprovalHistoryToWorkflowApprovalHistoryRawValue({
      ...this.getFormDefaults(),
      ...workflowApprovalHistory,
    });
    return new FormGroup<WorkflowApprovalHistoryFormGroupContent>({
      id: new FormControl(
        { value: workflowApprovalHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(workflowApprovalHistoryRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      stepNumber: new FormControl(workflowApprovalHistoryRawValue.stepNumber, {
        validators: [Validators.required],
      }),
      action: new FormControl(workflowApprovalHistoryRawValue.action),
      comment: new FormControl(workflowApprovalHistoryRawValue.comment),
      actionDate: new FormControl(workflowApprovalHistoryRawValue.actionDate, {
        validators: [Validators.required],
      }),
      actionBy: new FormControl(workflowApprovalHistoryRawValue.actionBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      previousAssignee: new FormControl(workflowApprovalHistoryRawValue.previousAssignee, {
        validators: [Validators.maxLength(50)],
      }),
      timeTaken: new FormControl(workflowApprovalHistoryRawValue.timeTaken),
      workflowInstance: new FormControl(workflowApprovalHistoryRawValue.workflowInstance),
    });
  }

  getWorkflowApprovalHistory(form: WorkflowApprovalHistoryFormGroup): IWorkflowApprovalHistory | NewWorkflowApprovalHistory {
    return this.convertWorkflowApprovalHistoryRawValueToWorkflowApprovalHistory(
      form.getRawValue() as WorkflowApprovalHistoryFormRawValue | NewWorkflowApprovalHistoryFormRawValue,
    );
  }

  resetForm(form: WorkflowApprovalHistoryFormGroup, workflowApprovalHistory: WorkflowApprovalHistoryFormGroupInput): void {
    const workflowApprovalHistoryRawValue = this.convertWorkflowApprovalHistoryToWorkflowApprovalHistoryRawValue({
      ...this.getFormDefaults(),
      ...workflowApprovalHistory,
    });
    form.reset(
      {
        ...workflowApprovalHistoryRawValue,
        id: { value: workflowApprovalHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkflowApprovalHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertWorkflowApprovalHistoryRawValueToWorkflowApprovalHistory(
    rawWorkflowApprovalHistory: WorkflowApprovalHistoryFormRawValue | NewWorkflowApprovalHistoryFormRawValue,
  ): IWorkflowApprovalHistory | NewWorkflowApprovalHistory {
    return {
      ...rawWorkflowApprovalHistory,
      actionDate: dayjs(rawWorkflowApprovalHistory.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertWorkflowApprovalHistoryToWorkflowApprovalHistoryRawValue(
    workflowApprovalHistory: IWorkflowApprovalHistory | (Partial<NewWorkflowApprovalHistory> & WorkflowApprovalHistoryFormDefaults),
  ): WorkflowApprovalHistoryFormRawValue | PartialWithRequiredKeyOf<NewWorkflowApprovalHistoryFormRawValue> {
    return {
      ...workflowApprovalHistory,
      actionDate: workflowApprovalHistory.actionDate ? workflowApprovalHistory.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
