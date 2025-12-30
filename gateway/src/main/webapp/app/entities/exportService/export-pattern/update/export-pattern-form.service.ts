import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExportPattern, NewExportPattern } from '../export-pattern.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExportPattern for edit and NewExportPatternFormGroupInput for create.
 */
type ExportPatternFormGroupInput = IExportPattern | PartialWithRequiredKeyOf<NewExportPattern>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExportPattern | NewExportPattern> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ExportPatternFormRawValue = FormValueOf<IExportPattern>;

type NewExportPatternFormRawValue = FormValueOf<NewExportPattern>;

type ExportPatternFormDefaults = Pick<NewExportPattern, 'id' | 'isSystem' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type ExportPatternFormGroupContent = {
  id: FormControl<ExportPatternFormRawValue['id'] | NewExportPattern['id']>;
  name: FormControl<ExportPatternFormRawValue['name']>;
  description: FormControl<ExportPatternFormRawValue['description']>;
  pathTemplate: FormControl<ExportPatternFormRawValue['pathTemplate']>;
  fileNameTemplate: FormControl<ExportPatternFormRawValue['fileNameTemplate']>;
  variables: FormControl<ExportPatternFormRawValue['variables']>;
  examples: FormControl<ExportPatternFormRawValue['examples']>;
  isSystem: FormControl<ExportPatternFormRawValue['isSystem']>;
  isActive: FormControl<ExportPatternFormRawValue['isActive']>;
  usageCount: FormControl<ExportPatternFormRawValue['usageCount']>;
  createdBy: FormControl<ExportPatternFormRawValue['createdBy']>;
  createdDate: FormControl<ExportPatternFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<ExportPatternFormRawValue['lastModifiedDate']>;
};

export type ExportPatternFormGroup = FormGroup<ExportPatternFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExportPatternFormService {
  createExportPatternFormGroup(exportPattern: ExportPatternFormGroupInput = { id: null }): ExportPatternFormGroup {
    const exportPatternRawValue = this.convertExportPatternToExportPatternRawValue({
      ...this.getFormDefaults(),
      ...exportPattern,
    });
    return new FormGroup<ExportPatternFormGroupContent>({
      id: new FormControl(
        { value: exportPatternRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(exportPatternRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(exportPatternRawValue.description),
      pathTemplate: new FormControl(exportPatternRawValue.pathTemplate, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      fileNameTemplate: new FormControl(exportPatternRawValue.fileNameTemplate, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      variables: new FormControl(exportPatternRawValue.variables),
      examples: new FormControl(exportPatternRawValue.examples),
      isSystem: new FormControl(exportPatternRawValue.isSystem, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(exportPatternRawValue.isActive, {
        validators: [Validators.required],
      }),
      usageCount: new FormControl(exportPatternRawValue.usageCount),
      createdBy: new FormControl(exportPatternRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(exportPatternRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(exportPatternRawValue.lastModifiedDate),
    });
  }

  getExportPattern(form: ExportPatternFormGroup): IExportPattern | NewExportPattern {
    return this.convertExportPatternRawValueToExportPattern(form.getRawValue() as ExportPatternFormRawValue | NewExportPatternFormRawValue);
  }

  resetForm(form: ExportPatternFormGroup, exportPattern: ExportPatternFormGroupInput): void {
    const exportPatternRawValue = this.convertExportPatternToExportPatternRawValue({ ...this.getFormDefaults(), ...exportPattern });
    form.reset(
      {
        ...exportPatternRawValue,
        id: { value: exportPatternRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExportPatternFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertExportPatternRawValueToExportPattern(
    rawExportPattern: ExportPatternFormRawValue | NewExportPatternFormRawValue,
  ): IExportPattern | NewExportPattern {
    return {
      ...rawExportPattern,
      createdDate: dayjs(rawExportPattern.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawExportPattern.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExportPatternToExportPatternRawValue(
    exportPattern: IExportPattern | (Partial<NewExportPattern> & ExportPatternFormDefaults),
  ): ExportPatternFormRawValue | PartialWithRequiredKeyOf<NewExportPatternFormRawValue> {
    return {
      ...exportPattern,
      createdDate: exportPattern.createdDate ? exportPattern.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: exportPattern.lastModifiedDate ? exportPattern.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
