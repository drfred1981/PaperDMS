import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWorkflowStep, NewWorkflowStep } from '../workflow-step.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkflowStep for edit and NewWorkflowStepFormGroupInput for create.
 */
type WorkflowStepFormGroupInput = IWorkflowStep | PartialWithRequiredKeyOf<NewWorkflowStep>;

type WorkflowStepFormDefaults = Pick<NewWorkflowStep, 'id' | 'isRequired' | 'canDelegate' | 'canReject'>;

type WorkflowStepFormGroupContent = {
  id: FormControl<IWorkflowStep['id'] | NewWorkflowStep['id']>;
  stepNumber: FormControl<IWorkflowStep['stepNumber']>;
  name: FormControl<IWorkflowStep['name']>;
  description: FormControl<IWorkflowStep['description']>;
  stepType: FormControl<IWorkflowStep['stepType']>;
  assigneeType: FormControl<IWorkflowStep['assigneeType']>;
  assigneeId: FormControl<IWorkflowStep['assigneeId']>;
  assigneeGroup: FormControl<IWorkflowStep['assigneeGroup']>;
  dueInDays: FormControl<IWorkflowStep['dueInDays']>;
  isRequired: FormControl<IWorkflowStep['isRequired']>;
  canDelegate: FormControl<IWorkflowStep['canDelegate']>;
  canReject: FormControl<IWorkflowStep['canReject']>;
  configuration: FormControl<IWorkflowStep['configuration']>;
  workflow: FormControl<IWorkflowStep['workflow']>;
};

export type WorkflowStepFormGroup = FormGroup<WorkflowStepFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkflowStepFormService {
  createWorkflowStepFormGroup(workflowStep: WorkflowStepFormGroupInput = { id: null }): WorkflowStepFormGroup {
    const workflowStepRawValue = {
      ...this.getFormDefaults(),
      ...workflowStep,
    };
    return new FormGroup<WorkflowStepFormGroupContent>({
      id: new FormControl(
        { value: workflowStepRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      stepNumber: new FormControl(workflowStepRawValue.stepNumber, {
        validators: [Validators.required],
      }),
      name: new FormControl(workflowStepRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(workflowStepRawValue.description),
      stepType: new FormControl(workflowStepRawValue.stepType),
      assigneeType: new FormControl(workflowStepRawValue.assigneeType),
      assigneeId: new FormControl(workflowStepRawValue.assigneeId, {
        validators: [Validators.maxLength(50)],
      }),
      assigneeGroup: new FormControl(workflowStepRawValue.assigneeGroup, {
        validators: [Validators.maxLength(100)],
      }),
      dueInDays: new FormControl(workflowStepRawValue.dueInDays),
      isRequired: new FormControl(workflowStepRawValue.isRequired, {
        validators: [Validators.required],
      }),
      canDelegate: new FormControl(workflowStepRawValue.canDelegate, {
        validators: [Validators.required],
      }),
      canReject: new FormControl(workflowStepRawValue.canReject, {
        validators: [Validators.required],
      }),
      configuration: new FormControl(workflowStepRawValue.configuration),
      workflow: new FormControl(workflowStepRawValue.workflow),
    });
  }

  getWorkflowStep(form: WorkflowStepFormGroup): IWorkflowStep | NewWorkflowStep {
    return form.getRawValue() as IWorkflowStep | NewWorkflowStep;
  }

  resetForm(form: WorkflowStepFormGroup, workflowStep: WorkflowStepFormGroupInput): void {
    const workflowStepRawValue = { ...this.getFormDefaults(), ...workflowStep };
    form.reset(
      {
        ...workflowStepRawValue,
        id: { value: workflowStepRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkflowStepFormDefaults {
    return {
      id: null,
      isRequired: false,
      canDelegate: false,
      canReject: false,
    };
  }
}
