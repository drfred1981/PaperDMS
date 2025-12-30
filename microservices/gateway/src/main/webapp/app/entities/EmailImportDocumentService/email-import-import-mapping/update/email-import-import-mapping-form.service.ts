import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmailImportImportMapping, NewEmailImportImportMapping } from '../email-import-import-mapping.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailImportImportMapping for edit and NewEmailImportImportMappingFormGroupInput for create.
 */
type EmailImportImportMappingFormGroupInput = IEmailImportImportMapping | PartialWithRequiredKeyOf<NewEmailImportImportMapping>;

type EmailImportImportMappingFormDefaults = Pick<NewEmailImportImportMapping, 'id' | 'isRequired'>;

type EmailImportImportMappingFormGroupContent = {
  id: FormControl<IEmailImportImportMapping['id'] | NewEmailImportImportMapping['id']>;
  emailField: FormControl<IEmailImportImportMapping['emailField']>;
  documentField: FormControl<IEmailImportImportMapping['documentField']>;
  transformation: FormControl<IEmailImportImportMapping['transformation']>;
  transformationConfig: FormControl<IEmailImportImportMapping['transformationConfig']>;
  isRequired: FormControl<IEmailImportImportMapping['isRequired']>;
  defaultValue: FormControl<IEmailImportImportMapping['defaultValue']>;
  validationRegex: FormControl<IEmailImportImportMapping['validationRegex']>;
  rule: FormControl<IEmailImportImportMapping['rule']>;
};

export type EmailImportImportMappingFormGroup = FormGroup<EmailImportImportMappingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailImportImportMappingFormService {
  createEmailImportImportMappingFormGroup(
    emailImportImportMapping: EmailImportImportMappingFormGroupInput = { id: null },
  ): EmailImportImportMappingFormGroup {
    const emailImportImportMappingRawValue = {
      ...this.getFormDefaults(),
      ...emailImportImportMapping,
    };
    return new FormGroup<EmailImportImportMappingFormGroupContent>({
      id: new FormControl(
        { value: emailImportImportMappingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      emailField: new FormControl(emailImportImportMappingRawValue.emailField, {
        validators: [Validators.required],
      }),
      documentField: new FormControl(emailImportImportMappingRawValue.documentField, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      transformation: new FormControl(emailImportImportMappingRawValue.transformation),
      transformationConfig: new FormControl(emailImportImportMappingRawValue.transformationConfig),
      isRequired: new FormControl(emailImportImportMappingRawValue.isRequired, {
        validators: [Validators.required],
      }),
      defaultValue: new FormControl(emailImportImportMappingRawValue.defaultValue, {
        validators: [Validators.maxLength(500)],
      }),
      validationRegex: new FormControl(emailImportImportMappingRawValue.validationRegex, {
        validators: [Validators.maxLength(500)],
      }),
      rule: new FormControl(emailImportImportMappingRawValue.rule),
    });
  }

  getEmailImportImportMapping(form: EmailImportImportMappingFormGroup): IEmailImportImportMapping | NewEmailImportImportMapping {
    return form.getRawValue() as IEmailImportImportMapping | NewEmailImportImportMapping;
  }

  resetForm(form: EmailImportImportMappingFormGroup, emailImportImportMapping: EmailImportImportMappingFormGroupInput): void {
    const emailImportImportMappingRawValue = { ...this.getFormDefaults(), ...emailImportImportMapping };
    form.reset(
      {
        ...emailImportImportMappingRawValue,
        id: { value: emailImportImportMappingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmailImportImportMappingFormDefaults {
    return {
      id: null,
      isRequired: false,
    };
  }
}
