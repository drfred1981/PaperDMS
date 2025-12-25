import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkflow, NewWorkflow } from '../workflow.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkflow for edit and NewWorkflowFormGroupInput for create.
 */
type WorkflowFormGroupInput = IWorkflow | PartialWithRequiredKeyOf<NewWorkflow>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkflow | NewWorkflow> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type WorkflowFormRawValue = FormValueOf<IWorkflow>;

type NewWorkflowFormRawValue = FormValueOf<NewWorkflow>;

type WorkflowFormDefaults = Pick<NewWorkflow, 'id' | 'isActive' | 'isParallel' | 'autoStart' | 'createdDate' | 'lastModifiedDate'>;

type WorkflowFormGroupContent = {
  id: FormControl<WorkflowFormRawValue['id'] | NewWorkflow['id']>;
  name: FormControl<WorkflowFormRawValue['name']>;
  description: FormControl<WorkflowFormRawValue['description']>;
  version: FormControl<WorkflowFormRawValue['version']>;
  isActive: FormControl<WorkflowFormRawValue['isActive']>;
  isParallel: FormControl<WorkflowFormRawValue['isParallel']>;
  autoStart: FormControl<WorkflowFormRawValue['autoStart']>;
  triggerEvent: FormControl<WorkflowFormRawValue['triggerEvent']>;
  configuration: FormControl<WorkflowFormRawValue['configuration']>;
  createdDate: FormControl<WorkflowFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<WorkflowFormRawValue['lastModifiedDate']>;
  createdBy: FormControl<WorkflowFormRawValue['createdBy']>;
};

export type WorkflowFormGroup = FormGroup<WorkflowFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkflowFormService {
  createWorkflowFormGroup(workflow: WorkflowFormGroupInput = { id: null }): WorkflowFormGroup {
    const workflowRawValue = this.convertWorkflowToWorkflowRawValue({
      ...this.getFormDefaults(),
      ...workflow,
    });
    return new FormGroup<WorkflowFormGroupContent>({
      id: new FormControl(
        { value: workflowRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(workflowRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(workflowRawValue.description),
      version: new FormControl(workflowRawValue.version, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(workflowRawValue.isActive, {
        validators: [Validators.required],
      }),
      isParallel: new FormControl(workflowRawValue.isParallel, {
        validators: [Validators.required],
      }),
      autoStart: new FormControl(workflowRawValue.autoStart, {
        validators: [Validators.required],
      }),
      triggerEvent: new FormControl(workflowRawValue.triggerEvent, {
        validators: [Validators.maxLength(100)],
      }),
      configuration: new FormControl(workflowRawValue.configuration),
      createdDate: new FormControl(workflowRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(workflowRawValue.lastModifiedDate),
      createdBy: new FormControl(workflowRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getWorkflow(form: WorkflowFormGroup): IWorkflow | NewWorkflow {
    return this.convertWorkflowRawValueToWorkflow(form.getRawValue() as WorkflowFormRawValue | NewWorkflowFormRawValue);
  }

  resetForm(form: WorkflowFormGroup, workflow: WorkflowFormGroupInput): void {
    const workflowRawValue = this.convertWorkflowToWorkflowRawValue({ ...this.getFormDefaults(), ...workflow });
    form.reset(
      {
        ...workflowRawValue,
        id: { value: workflowRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkflowFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      isParallel: false,
      autoStart: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertWorkflowRawValueToWorkflow(rawWorkflow: WorkflowFormRawValue | NewWorkflowFormRawValue): IWorkflow | NewWorkflow {
    return {
      ...rawWorkflow,
      createdDate: dayjs(rawWorkflow.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawWorkflow.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertWorkflowToWorkflowRawValue(
    workflow: IWorkflow | (Partial<NewWorkflow> & WorkflowFormDefaults),
  ): WorkflowFormRawValue | PartialWithRequiredKeyOf<NewWorkflowFormRawValue> {
    return {
      ...workflow,
      createdDate: workflow.createdDate ? workflow.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: workflow.lastModifiedDate ? workflow.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
