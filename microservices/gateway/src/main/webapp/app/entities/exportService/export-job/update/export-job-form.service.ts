import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExportJob, NewExportJob } from '../export-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExportJob for edit and NewExportJobFormGroupInput for create.
 */
type ExportJobFormGroupInput = IExportJob | PartialWithRequiredKeyOf<NewExportJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExportJob | NewExportJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type ExportJobFormRawValue = FormValueOf<IExportJob>;

type NewExportJobFormRawValue = FormValueOf<NewExportJob>;

type ExportJobFormDefaults = Pick<
  NewExportJob,
  'id' | 'includeMetadata' | 'includeVersions' | 'includeComments' | 'includeAuditTrail' | 'startDate' | 'endDate' | 'createdDate'
>;

type ExportJobFormGroupContent = {
  id: FormControl<ExportJobFormRawValue['id'] | NewExportJob['id']>;
  name: FormControl<ExportJobFormRawValue['name']>;
  description: FormControl<ExportJobFormRawValue['description']>;
  documentQuery: FormControl<ExportJobFormRawValue['documentQuery']>;
  exportFormat: FormControl<ExportJobFormRawValue['exportFormat']>;
  includeMetadata: FormControl<ExportJobFormRawValue['includeMetadata']>;
  includeVersions: FormControl<ExportJobFormRawValue['includeVersions']>;
  includeComments: FormControl<ExportJobFormRawValue['includeComments']>;
  includeAuditTrail: FormControl<ExportJobFormRawValue['includeAuditTrail']>;
  s3ExportKey: FormControl<ExportJobFormRawValue['s3ExportKey']>;
  exportSize: FormControl<ExportJobFormRawValue['exportSize']>;
  documentCount: FormControl<ExportJobFormRawValue['documentCount']>;
  filesGenerated: FormControl<ExportJobFormRawValue['filesGenerated']>;
  status: FormControl<ExportJobFormRawValue['status']>;
  startDate: FormControl<ExportJobFormRawValue['startDate']>;
  endDate: FormControl<ExportJobFormRawValue['endDate']>;
  errorMessage: FormControl<ExportJobFormRawValue['errorMessage']>;
  createdBy: FormControl<ExportJobFormRawValue['createdBy']>;
  createdDate: FormControl<ExportJobFormRawValue['createdDate']>;
  exportPattern: FormControl<ExportJobFormRawValue['exportPattern']>;
};

export type ExportJobFormGroup = FormGroup<ExportJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExportJobFormService {
  createExportJobFormGroup(exportJob: ExportJobFormGroupInput = { id: null }): ExportJobFormGroup {
    const exportJobRawValue = this.convertExportJobToExportJobRawValue({
      ...this.getFormDefaults(),
      ...exportJob,
    });
    return new FormGroup<ExportJobFormGroupContent>({
      id: new FormControl(
        { value: exportJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(exportJobRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(exportJobRawValue.description),
      documentQuery: new FormControl(exportJobRawValue.documentQuery, {
        validators: [Validators.required],
      }),
      exportFormat: new FormControl(exportJobRawValue.exportFormat, {
        validators: [Validators.required],
      }),
      includeMetadata: new FormControl(exportJobRawValue.includeMetadata, {
        validators: [Validators.required],
      }),
      includeVersions: new FormControl(exportJobRawValue.includeVersions, {
        validators: [Validators.required],
      }),
      includeComments: new FormControl(exportJobRawValue.includeComments),
      includeAuditTrail: new FormControl(exportJobRawValue.includeAuditTrail),
      s3ExportKey: new FormControl(exportJobRawValue.s3ExportKey, {
        validators: [Validators.maxLength(1000)],
      }),
      exportSize: new FormControl(exportJobRawValue.exportSize),
      documentCount: new FormControl(exportJobRawValue.documentCount),
      filesGenerated: new FormControl(exportJobRawValue.filesGenerated),
      status: new FormControl(exportJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(exportJobRawValue.startDate),
      endDate: new FormControl(exportJobRawValue.endDate),
      errorMessage: new FormControl(exportJobRawValue.errorMessage),
      createdBy: new FormControl(exportJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(exportJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
      exportPattern: new FormControl(exportJobRawValue.exportPattern),
    });
  }

  getExportJob(form: ExportJobFormGroup): IExportJob | NewExportJob {
    return this.convertExportJobRawValueToExportJob(form.getRawValue() as ExportJobFormRawValue | NewExportJobFormRawValue);
  }

  resetForm(form: ExportJobFormGroup, exportJob: ExportJobFormGroupInput): void {
    const exportJobRawValue = this.convertExportJobToExportJobRawValue({ ...this.getFormDefaults(), ...exportJob });
    form.reset(
      {
        ...exportJobRawValue,
        id: { value: exportJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExportJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      includeMetadata: false,
      includeVersions: false,
      includeComments: false,
      includeAuditTrail: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertExportJobRawValueToExportJob(rawExportJob: ExportJobFormRawValue | NewExportJobFormRawValue): IExportJob | NewExportJob {
    return {
      ...rawExportJob,
      startDate: dayjs(rawExportJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawExportJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawExportJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertExportJobToExportJobRawValue(
    exportJob: IExportJob | (Partial<NewExportJob> & ExportJobFormDefaults),
  ): ExportJobFormRawValue | PartialWithRequiredKeyOf<NewExportJobFormRawValue> {
    return {
      ...exportJob,
      startDate: exportJob.startDate ? exportJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: exportJob.endDate ? exportJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: exportJob.createdDate ? exportJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
