import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkflowInstance, NewWorkflowInstance } from '../workflow-instance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkflowInstance for edit and NewWorkflowInstanceFormGroupInput for create.
 */
type WorkflowInstanceFormGroupInput = IWorkflowInstance | PartialWithRequiredKeyOf<NewWorkflowInstance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkflowInstance | NewWorkflowInstance> = Omit<
  T,
  'startDate' | 'dueDate' | 'completedDate' | 'cancelledDate'
> & {
  startDate?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  cancelledDate?: string | null;
};

type WorkflowInstanceFormRawValue = FormValueOf<IWorkflowInstance>;

type NewWorkflowInstanceFormRawValue = FormValueOf<NewWorkflowInstance>;

type WorkflowInstanceFormDefaults = Pick<NewWorkflowInstance, 'id' | 'startDate' | 'dueDate' | 'completedDate' | 'cancelledDate'>;

type WorkflowInstanceFormGroupContent = {
  id: FormControl<WorkflowInstanceFormRawValue['id'] | NewWorkflowInstance['id']>;
  documentId: FormControl<WorkflowInstanceFormRawValue['documentId']>;
  status: FormControl<WorkflowInstanceFormRawValue['status']>;
  currentStepNumber: FormControl<WorkflowInstanceFormRawValue['currentStepNumber']>;
  startDate: FormControl<WorkflowInstanceFormRawValue['startDate']>;
  dueDate: FormControl<WorkflowInstanceFormRawValue['dueDate']>;
  completedDate: FormControl<WorkflowInstanceFormRawValue['completedDate']>;
  cancelledDate: FormControl<WorkflowInstanceFormRawValue['cancelledDate']>;
  cancellationReason: FormControl<WorkflowInstanceFormRawValue['cancellationReason']>;
  priority: FormControl<WorkflowInstanceFormRawValue['priority']>;
  metadata: FormControl<WorkflowInstanceFormRawValue['metadata']>;
  createdBy: FormControl<WorkflowInstanceFormRawValue['createdBy']>;
  workflow: FormControl<WorkflowInstanceFormRawValue['workflow']>;
};

export type WorkflowInstanceFormGroup = FormGroup<WorkflowInstanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkflowInstanceFormService {
  createWorkflowInstanceFormGroup(workflowInstance: WorkflowInstanceFormGroupInput = { id: null }): WorkflowInstanceFormGroup {
    const workflowInstanceRawValue = this.convertWorkflowInstanceToWorkflowInstanceRawValue({
      ...this.getFormDefaults(),
      ...workflowInstance,
    });
    return new FormGroup<WorkflowInstanceFormGroupContent>({
      id: new FormControl(
        { value: workflowInstanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(workflowInstanceRawValue.documentId, {
        validators: [Validators.required],
      }),
      status: new FormControl(workflowInstanceRawValue.status),
      currentStepNumber: new FormControl(workflowInstanceRawValue.currentStepNumber),
      startDate: new FormControl(workflowInstanceRawValue.startDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(workflowInstanceRawValue.dueDate),
      completedDate: new FormControl(workflowInstanceRawValue.completedDate),
      cancelledDate: new FormControl(workflowInstanceRawValue.cancelledDate),
      cancellationReason: new FormControl(workflowInstanceRawValue.cancellationReason),
      priority: new FormControl(workflowInstanceRawValue.priority),
      metadata: new FormControl(workflowInstanceRawValue.metadata),
      createdBy: new FormControl(workflowInstanceRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      workflow: new FormControl(workflowInstanceRawValue.workflow, {
        validators: [Validators.required],
      }),
    });
  }

  getWorkflowInstance(form: WorkflowInstanceFormGroup): IWorkflowInstance | NewWorkflowInstance {
    return this.convertWorkflowInstanceRawValueToWorkflowInstance(
      form.getRawValue() as WorkflowInstanceFormRawValue | NewWorkflowInstanceFormRawValue,
    );
  }

  resetForm(form: WorkflowInstanceFormGroup, workflowInstance: WorkflowInstanceFormGroupInput): void {
    const workflowInstanceRawValue = this.convertWorkflowInstanceToWorkflowInstanceRawValue({
      ...this.getFormDefaults(),
      ...workflowInstance,
    });
    form.reset(
      {
        ...workflowInstanceRawValue,
        id: { value: workflowInstanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkflowInstanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      dueDate: currentTime,
      completedDate: currentTime,
      cancelledDate: currentTime,
    };
  }

  private convertWorkflowInstanceRawValueToWorkflowInstance(
    rawWorkflowInstance: WorkflowInstanceFormRawValue | NewWorkflowInstanceFormRawValue,
  ): IWorkflowInstance | NewWorkflowInstance {
    return {
      ...rawWorkflowInstance,
      startDate: dayjs(rawWorkflowInstance.startDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawWorkflowInstance.dueDate, DATE_TIME_FORMAT),
      completedDate: dayjs(rawWorkflowInstance.completedDate, DATE_TIME_FORMAT),
      cancelledDate: dayjs(rawWorkflowInstance.cancelledDate, DATE_TIME_FORMAT),
    };
  }

  private convertWorkflowInstanceToWorkflowInstanceRawValue(
    workflowInstance: IWorkflowInstance | (Partial<NewWorkflowInstance> & WorkflowInstanceFormDefaults),
  ): WorkflowInstanceFormRawValue | PartialWithRequiredKeyOf<NewWorkflowInstanceFormRawValue> {
    return {
      ...workflowInstance,
      startDate: workflowInstance.startDate ? workflowInstance.startDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: workflowInstance.dueDate ? workflowInstance.dueDate.format(DATE_TIME_FORMAT) : undefined,
      completedDate: workflowInstance.completedDate ? workflowInstance.completedDate.format(DATE_TIME_FORMAT) : undefined,
      cancelledDate: workflowInstance.cancelledDate ? workflowInstance.cancelledDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
