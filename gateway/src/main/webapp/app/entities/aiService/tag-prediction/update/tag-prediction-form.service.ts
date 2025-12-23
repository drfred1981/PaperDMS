import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITagPrediction, NewTagPrediction } from '../tag-prediction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITagPrediction for edit and NewTagPredictionFormGroupInput for create.
 */
type TagPredictionFormGroupInput = ITagPrediction | PartialWithRequiredKeyOf<NewTagPrediction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITagPrediction | NewTagPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

type TagPredictionFormRawValue = FormValueOf<ITagPrediction>;

type NewTagPredictionFormRawValue = FormValueOf<NewTagPrediction>;

type TagPredictionFormDefaults = Pick<NewTagPrediction, 'id' | 'isAccepted' | 'acceptedDate' | 'predictionDate'>;

type TagPredictionFormGroupContent = {
  id: FormControl<TagPredictionFormRawValue['id'] | NewTagPrediction['id']>;
  tagName: FormControl<TagPredictionFormRawValue['tagName']>;
  confidence: FormControl<TagPredictionFormRawValue['confidence']>;
  reason: FormControl<TagPredictionFormRawValue['reason']>;
  modelVersion: FormControl<TagPredictionFormRawValue['modelVersion']>;
  predictionS3Key: FormControl<TagPredictionFormRawValue['predictionS3Key']>;
  isAccepted: FormControl<TagPredictionFormRawValue['isAccepted']>;
  acceptedBy: FormControl<TagPredictionFormRawValue['acceptedBy']>;
  acceptedDate: FormControl<TagPredictionFormRawValue['acceptedDate']>;
  predictionDate: FormControl<TagPredictionFormRawValue['predictionDate']>;
  job: FormControl<TagPredictionFormRawValue['job']>;
};

export type TagPredictionFormGroup = FormGroup<TagPredictionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagPredictionFormService {
  createTagPredictionFormGroup(tagPrediction?: TagPredictionFormGroupInput): TagPredictionFormGroup {
    const tagPredictionRawValue = this.convertTagPredictionToTagPredictionRawValue({
      ...this.getFormDefaults(),
      ...(tagPrediction ?? { id: null }),
    });
    return new FormGroup<TagPredictionFormGroupContent>({
      id: new FormControl(
        { value: tagPredictionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tagName: new FormControl(tagPredictionRawValue.tagName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      confidence: new FormControl(tagPredictionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      reason: new FormControl(tagPredictionRawValue.reason, {
        validators: [Validators.maxLength(500)],
      }),
      modelVersion: new FormControl(tagPredictionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      predictionS3Key: new FormControl(tagPredictionRawValue.predictionS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      isAccepted: new FormControl(tagPredictionRawValue.isAccepted),
      acceptedBy: new FormControl(tagPredictionRawValue.acceptedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acceptedDate: new FormControl(tagPredictionRawValue.acceptedDate),
      predictionDate: new FormControl(tagPredictionRawValue.predictionDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(tagPredictionRawValue.job, {
        validators: [Validators.required],
      }),
    });
  }

  getTagPrediction(form: TagPredictionFormGroup): ITagPrediction | NewTagPrediction {
    return this.convertTagPredictionRawValueToTagPrediction(form.getRawValue() as TagPredictionFormRawValue | NewTagPredictionFormRawValue);
  }

  resetForm(form: TagPredictionFormGroup, tagPrediction: TagPredictionFormGroupInput): void {
    const tagPredictionRawValue = this.convertTagPredictionToTagPredictionRawValue({ ...this.getFormDefaults(), ...tagPrediction });
    form.reset({
      ...tagPredictionRawValue,
      id: { value: tagPredictionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TagPredictionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAccepted: false,
      acceptedDate: currentTime,
      predictionDate: currentTime,
    };
  }

  private convertTagPredictionRawValueToTagPrediction(
    rawTagPrediction: TagPredictionFormRawValue | NewTagPredictionFormRawValue,
  ): ITagPrediction | NewTagPrediction {
    return {
      ...rawTagPrediction,
      acceptedDate: dayjs(rawTagPrediction.acceptedDate, DATE_TIME_FORMAT),
      predictionDate: dayjs(rawTagPrediction.predictionDate, DATE_TIME_FORMAT),
    };
  }

  private convertTagPredictionToTagPredictionRawValue(
    tagPrediction: ITagPrediction | (Partial<NewTagPrediction> & TagPredictionFormDefaults),
  ): TagPredictionFormRawValue | PartialWithRequiredKeyOf<NewTagPredictionFormRawValue> {
    return {
      ...tagPrediction,
      acceptedDate: tagPrediction.acceptedDate ? tagPrediction.acceptedDate.format(DATE_TIME_FORMAT) : undefined,
      predictionDate: tagPrediction.predictionDate ? tagPrediction.predictionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
