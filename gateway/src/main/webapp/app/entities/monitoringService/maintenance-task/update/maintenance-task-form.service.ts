import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaintenanceTask, NewMaintenanceTask } from '../maintenance-task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintenanceTask for edit and NewMaintenanceTaskFormGroupInput for create.
 */
type MaintenanceTaskFormGroupInput = IMaintenanceTask | PartialWithRequiredKeyOf<NewMaintenanceTask>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMaintenanceTask | NewMaintenanceTask> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

type MaintenanceTaskFormRawValue = FormValueOf<IMaintenanceTask>;

type NewMaintenanceTaskFormRawValue = FormValueOf<NewMaintenanceTask>;

type MaintenanceTaskFormDefaults = Pick<NewMaintenanceTask, 'id' | 'isActive' | 'lastRun' | 'nextRun' | 'createdDate'>;

type MaintenanceTaskFormGroupContent = {
  id: FormControl<MaintenanceTaskFormRawValue['id'] | NewMaintenanceTask['id']>;
  name: FormControl<MaintenanceTaskFormRawValue['name']>;
  description: FormControl<MaintenanceTaskFormRawValue['description']>;
  taskType: FormControl<MaintenanceTaskFormRawValue['taskType']>;
  schedule: FormControl<MaintenanceTaskFormRawValue['schedule']>;
  status: FormControl<MaintenanceTaskFormRawValue['status']>;
  isActive: FormControl<MaintenanceTaskFormRawValue['isActive']>;
  lastRun: FormControl<MaintenanceTaskFormRawValue['lastRun']>;
  nextRun: FormControl<MaintenanceTaskFormRawValue['nextRun']>;
  duration: FormControl<MaintenanceTaskFormRawValue['duration']>;
  recordsProcessed: FormControl<MaintenanceTaskFormRawValue['recordsProcessed']>;
  createdBy: FormControl<MaintenanceTaskFormRawValue['createdBy']>;
  createdDate: FormControl<MaintenanceTaskFormRawValue['createdDate']>;
};

export type MaintenanceTaskFormGroup = FormGroup<MaintenanceTaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaintenanceTaskFormService {
  createMaintenanceTaskFormGroup(maintenanceTask?: MaintenanceTaskFormGroupInput): MaintenanceTaskFormGroup {
    const maintenanceTaskRawValue = this.convertMaintenanceTaskToMaintenanceTaskRawValue({
      ...this.getFormDefaults(),
      ...(maintenanceTask ?? { id: null }),
    });
    return new FormGroup<MaintenanceTaskFormGroupContent>({
      id: new FormControl(
        { value: maintenanceTaskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(maintenanceTaskRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(maintenanceTaskRawValue.description),
      taskType: new FormControl(maintenanceTaskRawValue.taskType, {
        validators: [Validators.required],
      }),
      schedule: new FormControl(maintenanceTaskRawValue.schedule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      status: new FormControl(maintenanceTaskRawValue.status, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(maintenanceTaskRawValue.isActive, {
        validators: [Validators.required],
      }),
      lastRun: new FormControl(maintenanceTaskRawValue.lastRun),
      nextRun: new FormControl(maintenanceTaskRawValue.nextRun),
      duration: new FormControl(maintenanceTaskRawValue.duration),
      recordsProcessed: new FormControl(maintenanceTaskRawValue.recordsProcessed),
      createdBy: new FormControl(maintenanceTaskRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(maintenanceTaskRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMaintenanceTask(form: MaintenanceTaskFormGroup): IMaintenanceTask | NewMaintenanceTask {
    return this.convertMaintenanceTaskRawValueToMaintenanceTask(
      form.getRawValue() as MaintenanceTaskFormRawValue | NewMaintenanceTaskFormRawValue,
    );
  }

  resetForm(form: MaintenanceTaskFormGroup, maintenanceTask: MaintenanceTaskFormGroupInput): void {
    const maintenanceTaskRawValue = this.convertMaintenanceTaskToMaintenanceTaskRawValue({ ...this.getFormDefaults(), ...maintenanceTask });
    form.reset({
      ...maintenanceTaskRawValue,
      id: { value: maintenanceTaskRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MaintenanceTaskFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastRun: currentTime,
      nextRun: currentTime,
      createdDate: currentTime,
    };
  }

  private convertMaintenanceTaskRawValueToMaintenanceTask(
    rawMaintenanceTask: MaintenanceTaskFormRawValue | NewMaintenanceTaskFormRawValue,
  ): IMaintenanceTask | NewMaintenanceTask {
    return {
      ...rawMaintenanceTask,
      lastRun: dayjs(rawMaintenanceTask.lastRun, DATE_TIME_FORMAT),
      nextRun: dayjs(rawMaintenanceTask.nextRun, DATE_TIME_FORMAT),
      createdDate: dayjs(rawMaintenanceTask.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMaintenanceTaskToMaintenanceTaskRawValue(
    maintenanceTask: IMaintenanceTask | (Partial<NewMaintenanceTask> & MaintenanceTaskFormDefaults),
  ): MaintenanceTaskFormRawValue | PartialWithRequiredKeyOf<NewMaintenanceTaskFormRawValue> {
    return {
      ...maintenanceTask,
      lastRun: maintenanceTask.lastRun ? maintenanceTask.lastRun.format(DATE_TIME_FORMAT) : undefined,
      nextRun: maintenanceTask.nextRun ? maintenanceTask.nextRun.format(DATE_TIME_FORMAT) : undefined,
      createdDate: maintenanceTask.createdDate ? maintenanceTask.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
