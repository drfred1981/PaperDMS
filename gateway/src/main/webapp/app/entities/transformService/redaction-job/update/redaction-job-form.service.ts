import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRedactionJob, NewRedactionJob } from '../redaction-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRedactionJob for edit and NewRedactionJobFormGroupInput for create.
 */
type RedactionJobFormGroupInput = IRedactionJob | PartialWithRequiredKeyOf<NewRedactionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRedactionJob | NewRedactionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type RedactionJobFormRawValue = FormValueOf<IRedactionJob>;

type NewRedactionJobFormRawValue = FormValueOf<NewRedactionJob>;

type RedactionJobFormDefaults = Pick<NewRedactionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type RedactionJobFormGroupContent = {
  id: FormControl<RedactionJobFormRawValue['id'] | NewRedactionJob['id']>;
  documentId: FormControl<RedactionJobFormRawValue['documentId']>;
  redactionAreas: FormControl<RedactionJobFormRawValue['redactionAreas']>;
  redactionType: FormControl<RedactionJobFormRawValue['redactionType']>;
  redactionColor: FormControl<RedactionJobFormRawValue['redactionColor']>;
  replaceWith: FormControl<RedactionJobFormRawValue['replaceWith']>;
  outputS3Key: FormControl<RedactionJobFormRawValue['outputS3Key']>;
  outputDocumentId: FormControl<RedactionJobFormRawValue['outputDocumentId']>;
  status: FormControl<RedactionJobFormRawValue['status']>;
  startDate: FormControl<RedactionJobFormRawValue['startDate']>;
  endDate: FormControl<RedactionJobFormRawValue['endDate']>;
  errorMessage: FormControl<RedactionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<RedactionJobFormRawValue['createdBy']>;
  createdDate: FormControl<RedactionJobFormRawValue['createdDate']>;
};

export type RedactionJobFormGroup = FormGroup<RedactionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RedactionJobFormService {
  createRedactionJobFormGroup(redactionJob: RedactionJobFormGroupInput = { id: null }): RedactionJobFormGroup {
    const redactionJobRawValue = this.convertRedactionJobToRedactionJobRawValue({
      ...this.getFormDefaults(),
      ...redactionJob,
    });
    return new FormGroup<RedactionJobFormGroupContent>({
      id: new FormControl(
        { value: redactionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(redactionJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      redactionAreas: new FormControl(redactionJobRawValue.redactionAreas, {
        validators: [Validators.required],
      }),
      redactionType: new FormControl(redactionJobRawValue.redactionType, {
        validators: [Validators.required],
      }),
      redactionColor: new FormControl(redactionJobRawValue.redactionColor, {
        validators: [Validators.maxLength(7)],
      }),
      replaceWith: new FormControl(redactionJobRawValue.replaceWith, {
        validators: [Validators.maxLength(500)],
      }),
      outputS3Key: new FormControl(redactionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentId: new FormControl(redactionJobRawValue.outputDocumentId),
      status: new FormControl(redactionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(redactionJobRawValue.startDate),
      endDate: new FormControl(redactionJobRawValue.endDate),
      errorMessage: new FormControl(redactionJobRawValue.errorMessage),
      createdBy: new FormControl(redactionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(redactionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getRedactionJob(form: RedactionJobFormGroup): IRedactionJob | NewRedactionJob {
    return this.convertRedactionJobRawValueToRedactionJob(form.getRawValue() as RedactionJobFormRawValue | NewRedactionJobFormRawValue);
  }

  resetForm(form: RedactionJobFormGroup, redactionJob: RedactionJobFormGroupInput): void {
    const redactionJobRawValue = this.convertRedactionJobToRedactionJobRawValue({ ...this.getFormDefaults(), ...redactionJob });
    form.reset(
      {
        ...redactionJobRawValue,
        id: { value: redactionJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RedactionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertRedactionJobRawValueToRedactionJob(
    rawRedactionJob: RedactionJobFormRawValue | NewRedactionJobFormRawValue,
  ): IRedactionJob | NewRedactionJob {
    return {
      ...rawRedactionJob,
      startDate: dayjs(rawRedactionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawRedactionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawRedactionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertRedactionJobToRedactionJobRawValue(
    redactionJob: IRedactionJob | (Partial<NewRedactionJob> & RedactionJobFormDefaults),
  ): RedactionJobFormRawValue | PartialWithRequiredKeyOf<NewRedactionJobFormRawValue> {
    return {
      ...redactionJob,
      startDate: redactionJob.startDate ? redactionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: redactionJob.endDate ? redactionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: redactionJob.createdDate ? redactionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
