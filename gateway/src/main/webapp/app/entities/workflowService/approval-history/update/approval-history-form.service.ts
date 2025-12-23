import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApprovalHistory, NewApprovalHistory } from '../approval-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApprovalHistory for edit and NewApprovalHistoryFormGroupInput for create.
 */
type ApprovalHistoryFormGroupInput = IApprovalHistory | PartialWithRequiredKeyOf<NewApprovalHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApprovalHistory | NewApprovalHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type ApprovalHistoryFormRawValue = FormValueOf<IApprovalHistory>;

type NewApprovalHistoryFormRawValue = FormValueOf<NewApprovalHistory>;

type ApprovalHistoryFormDefaults = Pick<NewApprovalHistory, 'id' | 'actionDate'>;

type ApprovalHistoryFormGroupContent = {
  id: FormControl<ApprovalHistoryFormRawValue['id'] | NewApprovalHistory['id']>;
  documentId: FormControl<ApprovalHistoryFormRawValue['documentId']>;
  workflowInstanceId: FormControl<ApprovalHistoryFormRawValue['workflowInstanceId']>;
  stepNumber: FormControl<ApprovalHistoryFormRawValue['stepNumber']>;
  action: FormControl<ApprovalHistoryFormRawValue['action']>;
  comment: FormControl<ApprovalHistoryFormRawValue['comment']>;
  actionDate: FormControl<ApprovalHistoryFormRawValue['actionDate']>;
  actionBy: FormControl<ApprovalHistoryFormRawValue['actionBy']>;
  previousAssignee: FormControl<ApprovalHistoryFormRawValue['previousAssignee']>;
  timeTaken: FormControl<ApprovalHistoryFormRawValue['timeTaken']>;
};

export type ApprovalHistoryFormGroup = FormGroup<ApprovalHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApprovalHistoryFormService {
  createApprovalHistoryFormGroup(approvalHistory?: ApprovalHistoryFormGroupInput): ApprovalHistoryFormGroup {
    const approvalHistoryRawValue = this.convertApprovalHistoryToApprovalHistoryRawValue({
      ...this.getFormDefaults(),
      ...(approvalHistory ?? { id: null }),
    });
    return new FormGroup<ApprovalHistoryFormGroupContent>({
      id: new FormControl(
        { value: approvalHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(approvalHistoryRawValue.documentId, {
        validators: [Validators.required],
      }),
      workflowInstanceId: new FormControl(approvalHistoryRawValue.workflowInstanceId, {
        validators: [Validators.required],
      }),
      stepNumber: new FormControl(approvalHistoryRawValue.stepNumber, {
        validators: [Validators.required],
      }),
      action: new FormControl(approvalHistoryRawValue.action),
      comment: new FormControl(approvalHistoryRawValue.comment),
      actionDate: new FormControl(approvalHistoryRawValue.actionDate, {
        validators: [Validators.required],
      }),
      actionBy: new FormControl(approvalHistoryRawValue.actionBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      previousAssignee: new FormControl(approvalHistoryRawValue.previousAssignee, {
        validators: [Validators.maxLength(50)],
      }),
      timeTaken: new FormControl(approvalHistoryRawValue.timeTaken),
    });
  }

  getApprovalHistory(form: ApprovalHistoryFormGroup): IApprovalHistory | NewApprovalHistory {
    return this.convertApprovalHistoryRawValueToApprovalHistory(
      form.getRawValue() as ApprovalHistoryFormRawValue | NewApprovalHistoryFormRawValue,
    );
  }

  resetForm(form: ApprovalHistoryFormGroup, approvalHistory: ApprovalHistoryFormGroupInput): void {
    const approvalHistoryRawValue = this.convertApprovalHistoryToApprovalHistoryRawValue({ ...this.getFormDefaults(), ...approvalHistory });
    form.reset({
      ...approvalHistoryRawValue,
      id: { value: approvalHistoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ApprovalHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertApprovalHistoryRawValueToApprovalHistory(
    rawApprovalHistory: ApprovalHistoryFormRawValue | NewApprovalHistoryFormRawValue,
  ): IApprovalHistory | NewApprovalHistory {
    return {
      ...rawApprovalHistory,
      actionDate: dayjs(rawApprovalHistory.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertApprovalHistoryToApprovalHistoryRawValue(
    approvalHistory: IApprovalHistory | (Partial<NewApprovalHistory> & ApprovalHistoryFormDefaults),
  ): ApprovalHistoryFormRawValue | PartialWithRequiredKeyOf<NewApprovalHistoryFormRawValue> {
    return {
      ...approvalHistory,
      actionDate: approvalHistory.actionDate ? approvalHistory.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
