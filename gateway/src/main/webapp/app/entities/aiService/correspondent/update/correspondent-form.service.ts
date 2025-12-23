import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICorrespondent, NewCorrespondent } from '../correspondent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorrespondent for edit and NewCorrespondentFormGroupInput for create.
 */
type CorrespondentFormGroupInput = ICorrespondent | PartialWithRequiredKeyOf<NewCorrespondent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICorrespondent | NewCorrespondent> = Omit<T, 'verifiedDate' | 'extractedDate'> & {
  verifiedDate?: string | null;
  extractedDate?: string | null;
};

type CorrespondentFormRawValue = FormValueOf<ICorrespondent>;

type NewCorrespondentFormRawValue = FormValueOf<NewCorrespondent>;

type CorrespondentFormDefaults = Pick<NewCorrespondent, 'id' | 'isVerified' | 'verifiedDate' | 'extractedDate'>;

type CorrespondentFormGroupContent = {
  id: FormControl<CorrespondentFormRawValue['id'] | NewCorrespondent['id']>;
  name: FormControl<CorrespondentFormRawValue['name']>;
  email: FormControl<CorrespondentFormRawValue['email']>;
  phone: FormControl<CorrespondentFormRawValue['phone']>;
  address: FormControl<CorrespondentFormRawValue['address']>;
  company: FormControl<CorrespondentFormRawValue['company']>;
  type: FormControl<CorrespondentFormRawValue['type']>;
  role: FormControl<CorrespondentFormRawValue['role']>;
  confidence: FormControl<CorrespondentFormRawValue['confidence']>;
  isVerified: FormControl<CorrespondentFormRawValue['isVerified']>;
  verifiedBy: FormControl<CorrespondentFormRawValue['verifiedBy']>;
  verifiedDate: FormControl<CorrespondentFormRawValue['verifiedDate']>;
  metadata: FormControl<CorrespondentFormRawValue['metadata']>;
  extractedDate: FormControl<CorrespondentFormRawValue['extractedDate']>;
  extraction: FormControl<CorrespondentFormRawValue['extraction']>;
};

export type CorrespondentFormGroup = FormGroup<CorrespondentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorrespondentFormService {
  createCorrespondentFormGroup(correspondent?: CorrespondentFormGroupInput): CorrespondentFormGroup {
    const correspondentRawValue = this.convertCorrespondentToCorrespondentRawValue({
      ...this.getFormDefaults(),
      ...(correspondent ?? { id: null }),
    });
    return new FormGroup<CorrespondentFormGroupContent>({
      id: new FormControl(
        { value: correspondentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(correspondentRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      email: new FormControl(correspondentRawValue.email, {
        validators: [Validators.maxLength(255)],
      }),
      phone: new FormControl(correspondentRawValue.phone, {
        validators: [Validators.maxLength(50)],
      }),
      address: new FormControl(correspondentRawValue.address),
      company: new FormControl(correspondentRawValue.company, {
        validators: [Validators.maxLength(255)],
      }),
      type: new FormControl(correspondentRawValue.type),
      role: new FormControl(correspondentRawValue.role),
      confidence: new FormControl(correspondentRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      isVerified: new FormControl(correspondentRawValue.isVerified, {
        validators: [Validators.required],
      }),
      verifiedBy: new FormControl(correspondentRawValue.verifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      verifiedDate: new FormControl(correspondentRawValue.verifiedDate),
      metadata: new FormControl(correspondentRawValue.metadata),
      extractedDate: new FormControl(correspondentRawValue.extractedDate, {
        validators: [Validators.required],
      }),
      extraction: new FormControl(correspondentRawValue.extraction, {
        validators: [Validators.required],
      }),
    });
  }

  getCorrespondent(form: CorrespondentFormGroup): ICorrespondent | NewCorrespondent {
    return this.convertCorrespondentRawValueToCorrespondent(form.getRawValue() as CorrespondentFormRawValue | NewCorrespondentFormRawValue);
  }

  resetForm(form: CorrespondentFormGroup, correspondent: CorrespondentFormGroupInput): void {
    const correspondentRawValue = this.convertCorrespondentToCorrespondentRawValue({ ...this.getFormDefaults(), ...correspondent });
    form.reset({
      ...correspondentRawValue,
      id: { value: correspondentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CorrespondentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isVerified: false,
      verifiedDate: currentTime,
      extractedDate: currentTime,
    };
  }

  private convertCorrespondentRawValueToCorrespondent(
    rawCorrespondent: CorrespondentFormRawValue | NewCorrespondentFormRawValue,
  ): ICorrespondent | NewCorrespondent {
    return {
      ...rawCorrespondent,
      verifiedDate: dayjs(rawCorrespondent.verifiedDate, DATE_TIME_FORMAT),
      extractedDate: dayjs(rawCorrespondent.extractedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCorrespondentToCorrespondentRawValue(
    correspondent: ICorrespondent | (Partial<NewCorrespondent> & CorrespondentFormDefaults),
  ): CorrespondentFormRawValue | PartialWithRequiredKeyOf<NewCorrespondentFormRawValue> {
    return {
      ...correspondent,
      verifiedDate: correspondent.verifiedDate ? correspondent.verifiedDate.format(DATE_TIME_FORMAT) : undefined,
      extractedDate: correspondent.extractedDate ? correspondent.extractedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
