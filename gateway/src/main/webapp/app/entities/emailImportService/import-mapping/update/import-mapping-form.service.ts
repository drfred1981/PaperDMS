import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IImportMapping, NewImportMapping } from '../import-mapping.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImportMapping for edit and NewImportMappingFormGroupInput for create.
 */
type ImportMappingFormGroupInput = IImportMapping | PartialWithRequiredKeyOf<NewImportMapping>;

type ImportMappingFormDefaults = Pick<NewImportMapping, 'id' | 'isRequired'>;

type ImportMappingFormGroupContent = {
  id: FormControl<IImportMapping['id'] | NewImportMapping['id']>;
  ruleId: FormControl<IImportMapping['ruleId']>;
  emailField: FormControl<IImportMapping['emailField']>;
  documentField: FormControl<IImportMapping['documentField']>;
  transformation: FormControl<IImportMapping['transformation']>;
  transformationConfig: FormControl<IImportMapping['transformationConfig']>;
  isRequired: FormControl<IImportMapping['isRequired']>;
  defaultValue: FormControl<IImportMapping['defaultValue']>;
  validationRegex: FormControl<IImportMapping['validationRegex']>;
  rule: FormControl<IImportMapping['rule']>;
};

export type ImportMappingFormGroup = FormGroup<ImportMappingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImportMappingFormService {
  createImportMappingFormGroup(importMapping?: ImportMappingFormGroupInput): ImportMappingFormGroup {
    const importMappingRawValue = {
      ...this.getFormDefaults(),
      ...(importMapping ?? { id: null }),
    };
    return new FormGroup<ImportMappingFormGroupContent>({
      id: new FormControl(
        { value: importMappingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ruleId: new FormControl(importMappingRawValue.ruleId, {
        validators: [Validators.required],
      }),
      emailField: new FormControl(importMappingRawValue.emailField, {
        validators: [Validators.required],
      }),
      documentField: new FormControl(importMappingRawValue.documentField, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      transformation: new FormControl(importMappingRawValue.transformation),
      transformationConfig: new FormControl(importMappingRawValue.transformationConfig),
      isRequired: new FormControl(importMappingRawValue.isRequired, {
        validators: [Validators.required],
      }),
      defaultValue: new FormControl(importMappingRawValue.defaultValue, {
        validators: [Validators.maxLength(500)],
      }),
      validationRegex: new FormControl(importMappingRawValue.validationRegex, {
        validators: [Validators.maxLength(500)],
      }),
      rule: new FormControl(importMappingRawValue.rule, {
        validators: [Validators.required],
      }),
    });
  }

  getImportMapping(form: ImportMappingFormGroup): IImportMapping | NewImportMapping {
    return form.getRawValue() as IImportMapping | NewImportMapping;
  }

  resetForm(form: ImportMappingFormGroup, importMapping: ImportMappingFormGroupInput): void {
    const importMappingRawValue = { ...this.getFormDefaults(), ...importMapping };
    form.reset({
      ...importMappingRawValue,
      id: { value: importMappingRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ImportMappingFormDefaults {
    return {
      id: null,
      isRequired: false,
    };
  }
}
