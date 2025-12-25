import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImportRule, NewImportRule } from '../import-rule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImportRule for edit and NewImportRuleFormGroupInput for create.
 */
type ImportRuleFormGroupInput = IImportRule | PartialWithRequiredKeyOf<NewImportRule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImportRule | NewImportRule> = Omit<T, 'lastMatchDate' | 'createdDate' | 'lastModifiedDate'> & {
  lastMatchDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ImportRuleFormRawValue = FormValueOf<IImportRule>;

type NewImportRuleFormRawValue = FormValueOf<NewImportRule>;

type ImportRuleFormDefaults = Pick<NewImportRule, 'id' | 'isActive' | 'lastMatchDate' | 'createdDate' | 'lastModifiedDate'>;

type ImportRuleFormGroupContent = {
  id: FormControl<ImportRuleFormRawValue['id'] | NewImportRule['id']>;
  name: FormControl<ImportRuleFormRawValue['name']>;
  description: FormControl<ImportRuleFormRawValue['description']>;
  priority: FormControl<ImportRuleFormRawValue['priority']>;
  isActive: FormControl<ImportRuleFormRawValue['isActive']>;
  conditions: FormControl<ImportRuleFormRawValue['conditions']>;
  actions: FormControl<ImportRuleFormRawValue['actions']>;
  folderId: FormControl<ImportRuleFormRawValue['folderId']>;
  documentTypeId: FormControl<ImportRuleFormRawValue['documentTypeId']>;
  applyTags: FormControl<ImportRuleFormRawValue['applyTags']>;
  notifyUsers: FormControl<ImportRuleFormRawValue['notifyUsers']>;
  matchCount: FormControl<ImportRuleFormRawValue['matchCount']>;
  lastMatchDate: FormControl<ImportRuleFormRawValue['lastMatchDate']>;
  createdBy: FormControl<ImportRuleFormRawValue['createdBy']>;
  createdDate: FormControl<ImportRuleFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<ImportRuleFormRawValue['lastModifiedDate']>;
};

export type ImportRuleFormGroup = FormGroup<ImportRuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImportRuleFormService {
  createImportRuleFormGroup(importRule: ImportRuleFormGroupInput = { id: null }): ImportRuleFormGroup {
    const importRuleRawValue = this.convertImportRuleToImportRuleRawValue({
      ...this.getFormDefaults(),
      ...importRule,
    });
    return new FormGroup<ImportRuleFormGroupContent>({
      id: new FormControl(
        { value: importRuleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(importRuleRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(importRuleRawValue.description),
      priority: new FormControl(importRuleRawValue.priority, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(importRuleRawValue.isActive, {
        validators: [Validators.required],
      }),
      conditions: new FormControl(importRuleRawValue.conditions, {
        validators: [Validators.required],
      }),
      actions: new FormControl(importRuleRawValue.actions, {
        validators: [Validators.required],
      }),
      folderId: new FormControl(importRuleRawValue.folderId),
      documentTypeId: new FormControl(importRuleRawValue.documentTypeId),
      applyTags: new FormControl(importRuleRawValue.applyTags),
      notifyUsers: new FormControl(importRuleRawValue.notifyUsers),
      matchCount: new FormControl(importRuleRawValue.matchCount),
      lastMatchDate: new FormControl(importRuleRawValue.lastMatchDate),
      createdBy: new FormControl(importRuleRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(importRuleRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(importRuleRawValue.lastModifiedDate),
    });
  }

  getImportRule(form: ImportRuleFormGroup): IImportRule | NewImportRule {
    return this.convertImportRuleRawValueToImportRule(form.getRawValue() as ImportRuleFormRawValue | NewImportRuleFormRawValue);
  }

  resetForm(form: ImportRuleFormGroup, importRule: ImportRuleFormGroupInput): void {
    const importRuleRawValue = this.convertImportRuleToImportRuleRawValue({ ...this.getFormDefaults(), ...importRule });
    form.reset(
      {
        ...importRuleRawValue,
        id: { value: importRuleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImportRuleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastMatchDate: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertImportRuleRawValueToImportRule(
    rawImportRule: ImportRuleFormRawValue | NewImportRuleFormRawValue,
  ): IImportRule | NewImportRule {
    return {
      ...rawImportRule,
      lastMatchDate: dayjs(rawImportRule.lastMatchDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawImportRule.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawImportRule.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertImportRuleToImportRuleRawValue(
    importRule: IImportRule | (Partial<NewImportRule> & ImportRuleFormDefaults),
  ): ImportRuleFormRawValue | PartialWithRequiredKeyOf<NewImportRuleFormRawValue> {
    return {
      ...importRule,
      lastMatchDate: importRule.lastMatchDate ? importRule.lastMatchDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: importRule.createdDate ? importRule.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: importRule.lastModifiedDate ? importRule.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
