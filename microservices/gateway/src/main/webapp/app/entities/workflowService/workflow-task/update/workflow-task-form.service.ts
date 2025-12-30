import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkflowTask, NewWorkflowTask } from '../workflow-task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkflowTask for edit and NewWorkflowTaskFormGroupInput for create.
 */
type WorkflowTaskFormGroupInput = IWorkflowTask | PartialWithRequiredKeyOf<NewWorkflowTask>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkflowTask | NewWorkflowTask> = Omit<T, 'assignedDate' | 'dueDate' | 'completedDate' | 'delegatedDate'> & {
  assignedDate?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  delegatedDate?: string | null;
};

type WorkflowTaskFormRawValue = FormValueOf<IWorkflowTask>;

type NewWorkflowTaskFormRawValue = FormValueOf<NewWorkflowTask>;

type WorkflowTaskFormDefaults = Pick<
  NewWorkflowTask,
  'id' | 'assignedDate' | 'dueDate' | 'completedDate' | 'reminderSent' | 'delegatedDate'
>;

type WorkflowTaskFormGroupContent = {
  id: FormControl<WorkflowTaskFormRawValue['id'] | NewWorkflowTask['id']>;
  assigneeId: FormControl<WorkflowTaskFormRawValue['assigneeId']>;
  status: FormControl<WorkflowTaskFormRawValue['status']>;
  action: FormControl<WorkflowTaskFormRawValue['action']>;
  comment: FormControl<WorkflowTaskFormRawValue['comment']>;
  assignedDate: FormControl<WorkflowTaskFormRawValue['assignedDate']>;
  dueDate: FormControl<WorkflowTaskFormRawValue['dueDate']>;
  completedDate: FormControl<WorkflowTaskFormRawValue['completedDate']>;
  reminderSent: FormControl<WorkflowTaskFormRawValue['reminderSent']>;
  delegatedTo: FormControl<WorkflowTaskFormRawValue['delegatedTo']>;
  delegatedDate: FormControl<WorkflowTaskFormRawValue['delegatedDate']>;
  instance: FormControl<WorkflowTaskFormRawValue['instance']>;
  step: FormControl<WorkflowTaskFormRawValue['step']>;
};

export type WorkflowTaskFormGroup = FormGroup<WorkflowTaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkflowTaskFormService {
  createWorkflowTaskFormGroup(workflowTask: WorkflowTaskFormGroupInput = { id: null }): WorkflowTaskFormGroup {
    const workflowTaskRawValue = this.convertWorkflowTaskToWorkflowTaskRawValue({
      ...this.getFormDefaults(),
      ...workflowTask,
    });
    return new FormGroup<WorkflowTaskFormGroupContent>({
      id: new FormControl(
        { value: workflowTaskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      assigneeId: new FormControl(workflowTaskRawValue.assigneeId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      status: new FormControl(workflowTaskRawValue.status),
      action: new FormControl(workflowTaskRawValue.action),
      comment: new FormControl(workflowTaskRawValue.comment),
      assignedDate: new FormControl(workflowTaskRawValue.assignedDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(workflowTaskRawValue.dueDate),
      completedDate: new FormControl(workflowTaskRawValue.completedDate),
      reminderSent: new FormControl(workflowTaskRawValue.reminderSent, {
        validators: [Validators.required],
      }),
      delegatedTo: new FormControl(workflowTaskRawValue.delegatedTo, {
        validators: [Validators.maxLength(50)],
      }),
      delegatedDate: new FormControl(workflowTaskRawValue.delegatedDate),
      instance: new FormControl(workflowTaskRawValue.instance),
      step: new FormControl(workflowTaskRawValue.step),
    });
  }

  getWorkflowTask(form: WorkflowTaskFormGroup): IWorkflowTask | NewWorkflowTask {
    return this.convertWorkflowTaskRawValueToWorkflowTask(form.getRawValue() as WorkflowTaskFormRawValue | NewWorkflowTaskFormRawValue);
  }

  resetForm(form: WorkflowTaskFormGroup, workflowTask: WorkflowTaskFormGroupInput): void {
    const workflowTaskRawValue = this.convertWorkflowTaskToWorkflowTaskRawValue({ ...this.getFormDefaults(), ...workflowTask });
    form.reset(
      {
        ...workflowTaskRawValue,
        id: { value: workflowTaskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkflowTaskFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      assignedDate: currentTime,
      dueDate: currentTime,
      completedDate: currentTime,
      reminderSent: false,
      delegatedDate: currentTime,
    };
  }

  private convertWorkflowTaskRawValueToWorkflowTask(
    rawWorkflowTask: WorkflowTaskFormRawValue | NewWorkflowTaskFormRawValue,
  ): IWorkflowTask | NewWorkflowTask {
    return {
      ...rawWorkflowTask,
      assignedDate: dayjs(rawWorkflowTask.assignedDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawWorkflowTask.dueDate, DATE_TIME_FORMAT),
      completedDate: dayjs(rawWorkflowTask.completedDate, DATE_TIME_FORMAT),
      delegatedDate: dayjs(rawWorkflowTask.delegatedDate, DATE_TIME_FORMAT),
    };
  }

  private convertWorkflowTaskToWorkflowTaskRawValue(
    workflowTask: IWorkflowTask | (Partial<NewWorkflowTask> & WorkflowTaskFormDefaults),
  ): WorkflowTaskFormRawValue | PartialWithRequiredKeyOf<NewWorkflowTaskFormRawValue> {
    return {
      ...workflowTask,
      assignedDate: workflowTask.assignedDate ? workflowTask.assignedDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: workflowTask.dueDate ? workflowTask.dueDate.format(DATE_TIME_FORMAT) : undefined,
      completedDate: workflowTask.completedDate ? workflowTask.completedDate.format(DATE_TIME_FORMAT) : undefined,
      delegatedDate: workflowTask.delegatedDate ? workflowTask.delegatedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
