import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringMaintenanceTask, NewMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringMaintenanceTask for edit and NewMonitoringMaintenanceTaskFormGroupInput for create.
 */
type MonitoringMaintenanceTaskFormGroupInput = IMonitoringMaintenanceTask | PartialWithRequiredKeyOf<NewMonitoringMaintenanceTask>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringMaintenanceTask | NewMonitoringMaintenanceTask> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

type MonitoringMaintenanceTaskFormRawValue = FormValueOf<IMonitoringMaintenanceTask>;

type NewMonitoringMaintenanceTaskFormRawValue = FormValueOf<NewMonitoringMaintenanceTask>;

type MonitoringMaintenanceTaskFormDefaults = Pick<NewMonitoringMaintenanceTask, 'id' | 'isActive' | 'lastRun' | 'nextRun' | 'createdDate'>;

type MonitoringMaintenanceTaskFormGroupContent = {
  id: FormControl<MonitoringMaintenanceTaskFormRawValue['id'] | NewMonitoringMaintenanceTask['id']>;
  name: FormControl<MonitoringMaintenanceTaskFormRawValue['name']>;
  description: FormControl<MonitoringMaintenanceTaskFormRawValue['description']>;
  taskType: FormControl<MonitoringMaintenanceTaskFormRawValue['taskType']>;
  schedule: FormControl<MonitoringMaintenanceTaskFormRawValue['schedule']>;
  status: FormControl<MonitoringMaintenanceTaskFormRawValue['status']>;
  isActive: FormControl<MonitoringMaintenanceTaskFormRawValue['isActive']>;
  lastRun: FormControl<MonitoringMaintenanceTaskFormRawValue['lastRun']>;
  nextRun: FormControl<MonitoringMaintenanceTaskFormRawValue['nextRun']>;
  duration: FormControl<MonitoringMaintenanceTaskFormRawValue['duration']>;
  recordsProcessed: FormControl<MonitoringMaintenanceTaskFormRawValue['recordsProcessed']>;
  createdBy: FormControl<MonitoringMaintenanceTaskFormRawValue['createdBy']>;
  createdDate: FormControl<MonitoringMaintenanceTaskFormRawValue['createdDate']>;
};

export type MonitoringMaintenanceTaskFormGroup = FormGroup<MonitoringMaintenanceTaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringMaintenanceTaskFormService {
  createMonitoringMaintenanceTaskFormGroup(
    monitoringMaintenanceTask: MonitoringMaintenanceTaskFormGroupInput = { id: null },
  ): MonitoringMaintenanceTaskFormGroup {
    const monitoringMaintenanceTaskRawValue = this.convertMonitoringMaintenanceTaskToMonitoringMaintenanceTaskRawValue({
      ...this.getFormDefaults(),
      ...monitoringMaintenanceTask,
    });
    return new FormGroup<MonitoringMaintenanceTaskFormGroupContent>({
      id: new FormControl(
        { value: monitoringMaintenanceTaskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(monitoringMaintenanceTaskRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(monitoringMaintenanceTaskRawValue.description),
      taskType: new FormControl(monitoringMaintenanceTaskRawValue.taskType, {
        validators: [Validators.required],
      }),
      schedule: new FormControl(monitoringMaintenanceTaskRawValue.schedule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      status: new FormControl(monitoringMaintenanceTaskRawValue.status, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(monitoringMaintenanceTaskRawValue.isActive, {
        validators: [Validators.required],
      }),
      lastRun: new FormControl(monitoringMaintenanceTaskRawValue.lastRun),
      nextRun: new FormControl(monitoringMaintenanceTaskRawValue.nextRun),
      duration: new FormControl(monitoringMaintenanceTaskRawValue.duration),
      recordsProcessed: new FormControl(monitoringMaintenanceTaskRawValue.recordsProcessed),
      createdBy: new FormControl(monitoringMaintenanceTaskRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(monitoringMaintenanceTaskRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMonitoringMaintenanceTask(form: MonitoringMaintenanceTaskFormGroup): IMonitoringMaintenanceTask | NewMonitoringMaintenanceTask {
    return this.convertMonitoringMaintenanceTaskRawValueToMonitoringMaintenanceTask(
      form.getRawValue() as MonitoringMaintenanceTaskFormRawValue | NewMonitoringMaintenanceTaskFormRawValue,
    );
  }

  resetForm(form: MonitoringMaintenanceTaskFormGroup, monitoringMaintenanceTask: MonitoringMaintenanceTaskFormGroupInput): void {
    const monitoringMaintenanceTaskRawValue = this.convertMonitoringMaintenanceTaskToMonitoringMaintenanceTaskRawValue({
      ...this.getFormDefaults(),
      ...monitoringMaintenanceTask,
    });
    form.reset(
      {
        ...monitoringMaintenanceTaskRawValue,
        id: { value: monitoringMaintenanceTaskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringMaintenanceTaskFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastRun: currentTime,
      nextRun: currentTime,
      createdDate: currentTime,
    };
  }

  private convertMonitoringMaintenanceTaskRawValueToMonitoringMaintenanceTask(
    rawMonitoringMaintenanceTask: MonitoringMaintenanceTaskFormRawValue | NewMonitoringMaintenanceTaskFormRawValue,
  ): IMonitoringMaintenanceTask | NewMonitoringMaintenanceTask {
    return {
      ...rawMonitoringMaintenanceTask,
      lastRun: dayjs(rawMonitoringMaintenanceTask.lastRun, DATE_TIME_FORMAT),
      nextRun: dayjs(rawMonitoringMaintenanceTask.nextRun, DATE_TIME_FORMAT),
      createdDate: dayjs(rawMonitoringMaintenanceTask.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringMaintenanceTaskToMonitoringMaintenanceTaskRawValue(
    monitoringMaintenanceTask: IMonitoringMaintenanceTask | (Partial<NewMonitoringMaintenanceTask> & MonitoringMaintenanceTaskFormDefaults),
  ): MonitoringMaintenanceTaskFormRawValue | PartialWithRequiredKeyOf<NewMonitoringMaintenanceTaskFormRawValue> {
    return {
      ...monitoringMaintenanceTask,
      lastRun: monitoringMaintenanceTask.lastRun ? monitoringMaintenanceTask.lastRun.format(DATE_TIME_FORMAT) : undefined,
      nextRun: monitoringMaintenanceTask.nextRun ? monitoringMaintenanceTask.nextRun.format(DATE_TIME_FORMAT) : undefined,
      createdDate: monitoringMaintenanceTask.createdDate ? monitoringMaintenanceTask.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
