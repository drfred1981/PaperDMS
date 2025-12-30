import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IArchiveJob, NewArchiveJob } from '../archive-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArchiveJob for edit and NewArchiveJobFormGroupInput for create.
 */
type ArchiveJobFormGroupInput = IArchiveJob | PartialWithRequiredKeyOf<NewArchiveJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArchiveJob | NewArchiveJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type ArchiveJobFormRawValue = FormValueOf<IArchiveJob>;

type NewArchiveJobFormRawValue = FormValueOf<NewArchiveJob>;

type ArchiveJobFormDefaults = Pick<NewArchiveJob, 'id' | 'encryptionEnabled' | 'startDate' | 'endDate' | 'createdDate'>;

type ArchiveJobFormGroupContent = {
  id: FormControl<ArchiveJobFormRawValue['id'] | NewArchiveJob['id']>;
  name: FormControl<ArchiveJobFormRawValue['name']>;
  description: FormControl<ArchiveJobFormRawValue['description']>;
  documentQuery: FormControl<ArchiveJobFormRawValue['documentQuery']>;
  archiveFormat: FormControl<ArchiveJobFormRawValue['archiveFormat']>;
  compressionLevel: FormControl<ArchiveJobFormRawValue['compressionLevel']>;
  encryptionEnabled: FormControl<ArchiveJobFormRawValue['encryptionEnabled']>;
  encryptionAlgorithm: FormControl<ArchiveJobFormRawValue['encryptionAlgorithm']>;
  password: FormControl<ArchiveJobFormRawValue['password']>;
  s3ArchiveKey: FormControl<ArchiveJobFormRawValue['s3ArchiveKey']>;
  archiveSha256: FormControl<ArchiveJobFormRawValue['archiveSha256']>;
  archiveSize: FormControl<ArchiveJobFormRawValue['archiveSize']>;
  documentCount: FormControl<ArchiveJobFormRawValue['documentCount']>;
  status: FormControl<ArchiveJobFormRawValue['status']>;
  startDate: FormControl<ArchiveJobFormRawValue['startDate']>;
  endDate: FormControl<ArchiveJobFormRawValue['endDate']>;
  errorMessage: FormControl<ArchiveJobFormRawValue['errorMessage']>;
  createdBy: FormControl<ArchiveJobFormRawValue['createdBy']>;
  createdDate: FormControl<ArchiveJobFormRawValue['createdDate']>;
};

export type ArchiveJobFormGroup = FormGroup<ArchiveJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArchiveJobFormService {
  createArchiveJobFormGroup(archiveJob: ArchiveJobFormGroupInput = { id: null }): ArchiveJobFormGroup {
    const archiveJobRawValue = this.convertArchiveJobToArchiveJobRawValue({
      ...this.getFormDefaults(),
      ...archiveJob,
    });
    return new FormGroup<ArchiveJobFormGroupContent>({
      id: new FormControl(
        { value: archiveJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(archiveJobRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(archiveJobRawValue.description),
      documentQuery: new FormControl(archiveJobRawValue.documentQuery, {
        validators: [Validators.required],
      }),
      archiveFormat: new FormControl(archiveJobRawValue.archiveFormat, {
        validators: [Validators.required],
      }),
      compressionLevel: new FormControl(archiveJobRawValue.compressionLevel, {
        validators: [Validators.min(0), Validators.max(9)],
      }),
      encryptionEnabled: new FormControl(archiveJobRawValue.encryptionEnabled, {
        validators: [Validators.required],
      }),
      encryptionAlgorithm: new FormControl(archiveJobRawValue.encryptionAlgorithm, {
        validators: [Validators.maxLength(50)],
      }),
      password: new FormControl(archiveJobRawValue.password, {
        validators: [Validators.maxLength(255)],
      }),
      s3ArchiveKey: new FormControl(archiveJobRawValue.s3ArchiveKey, {
        validators: [Validators.maxLength(1000)],
      }),
      archiveSha256: new FormControl(archiveJobRawValue.archiveSha256, {
        validators: [Validators.maxLength(64)],
      }),
      archiveSize: new FormControl(archiveJobRawValue.archiveSize),
      documentCount: new FormControl(archiveJobRawValue.documentCount),
      status: new FormControl(archiveJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(archiveJobRawValue.startDate),
      endDate: new FormControl(archiveJobRawValue.endDate),
      errorMessage: new FormControl(archiveJobRawValue.errorMessage),
      createdBy: new FormControl(archiveJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(archiveJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getArchiveJob(form: ArchiveJobFormGroup): IArchiveJob | NewArchiveJob {
    return this.convertArchiveJobRawValueToArchiveJob(form.getRawValue() as ArchiveJobFormRawValue | NewArchiveJobFormRawValue);
  }

  resetForm(form: ArchiveJobFormGroup, archiveJob: ArchiveJobFormGroupInput): void {
    const archiveJobRawValue = this.convertArchiveJobToArchiveJobRawValue({ ...this.getFormDefaults(), ...archiveJob });
    form.reset(
      {
        ...archiveJobRawValue,
        id: { value: archiveJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArchiveJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      encryptionEnabled: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertArchiveJobRawValueToArchiveJob(
    rawArchiveJob: ArchiveJobFormRawValue | NewArchiveJobFormRawValue,
  ): IArchiveJob | NewArchiveJob {
    return {
      ...rawArchiveJob,
      startDate: dayjs(rawArchiveJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawArchiveJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawArchiveJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertArchiveJobToArchiveJobRawValue(
    archiveJob: IArchiveJob | (Partial<NewArchiveJob> & ArchiveJobFormDefaults),
  ): ArchiveJobFormRawValue | PartialWithRequiredKeyOf<NewArchiveJobFormRawValue> {
    return {
      ...archiveJob,
      startDate: archiveJob.startDate ? archiveJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: archiveJob.endDate ? archiveJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: archiveJob.createdDate ? archiveJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
