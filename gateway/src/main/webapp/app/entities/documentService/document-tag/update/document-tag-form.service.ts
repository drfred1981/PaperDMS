import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentTag, NewDocumentTag } from '../document-tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentTag for edit and NewDocumentTagFormGroupInput for create.
 */
type DocumentTagFormGroupInput = IDocumentTag | PartialWithRequiredKeyOf<NewDocumentTag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentTag | NewDocumentTag> = Omit<T, 'assignedDate'> & {
  assignedDate?: string | null;
};

type DocumentTagFormRawValue = FormValueOf<IDocumentTag>;

type NewDocumentTagFormRawValue = FormValueOf<NewDocumentTag>;

type DocumentTagFormDefaults = Pick<NewDocumentTag, 'id' | 'assignedDate' | 'isAutoTagged'>;

type DocumentTagFormGroupContent = {
  id: FormControl<DocumentTagFormRawValue['id'] | NewDocumentTag['id']>;
  assignedDate: FormControl<DocumentTagFormRawValue['assignedDate']>;
  assignedBy: FormControl<DocumentTagFormRawValue['assignedBy']>;
  confidence: FormControl<DocumentTagFormRawValue['confidence']>;
  isAutoTagged: FormControl<DocumentTagFormRawValue['isAutoTagged']>;
  source: FormControl<DocumentTagFormRawValue['source']>;
  document: FormControl<DocumentTagFormRawValue['document']>;
  tag: FormControl<DocumentTagFormRawValue['tag']>;
};

export type DocumentTagFormGroup = FormGroup<DocumentTagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentTagFormService {
  createDocumentTagFormGroup(documentTag?: DocumentTagFormGroupInput): DocumentTagFormGroup {
    const documentTagRawValue = this.convertDocumentTagToDocumentTagRawValue({
      ...this.getFormDefaults(),
      ...(documentTag ?? { id: null }),
    });
    return new FormGroup<DocumentTagFormGroupContent>({
      id: new FormControl(
        { value: documentTagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      assignedDate: new FormControl(documentTagRawValue.assignedDate, {
        validators: [Validators.required],
      }),
      assignedBy: new FormControl(documentTagRawValue.assignedBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      confidence: new FormControl(documentTagRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      isAutoTagged: new FormControl(documentTagRawValue.isAutoTagged, {
        validators: [Validators.required],
      }),
      source: new FormControl(documentTagRawValue.source),
      document: new FormControl(documentTagRawValue.document, {
        validators: [Validators.required],
      }),
      tag: new FormControl(documentTagRawValue.tag, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentTag(form: DocumentTagFormGroup): IDocumentTag | NewDocumentTag {
    return this.convertDocumentTagRawValueToDocumentTag(form.getRawValue() as DocumentTagFormRawValue | NewDocumentTagFormRawValue);
  }

  resetForm(form: DocumentTagFormGroup, documentTag: DocumentTagFormGroupInput): void {
    const documentTagRawValue = this.convertDocumentTagToDocumentTagRawValue({ ...this.getFormDefaults(), ...documentTag });
    form.reset({
      ...documentTagRawValue,
      id: { value: documentTagRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentTagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      assignedDate: currentTime,
      isAutoTagged: false,
    };
  }

  private convertDocumentTagRawValueToDocumentTag(
    rawDocumentTag: DocumentTagFormRawValue | NewDocumentTagFormRawValue,
  ): IDocumentTag | NewDocumentTag {
    return {
      ...rawDocumentTag,
      assignedDate: dayjs(rawDocumentTag.assignedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentTagToDocumentTagRawValue(
    documentTag: IDocumentTag | (Partial<NewDocumentTag> & DocumentTagFormDefaults),
  ): DocumentTagFormRawValue | PartialWithRequiredKeyOf<NewDocumentTagFormRawValue> {
    return {
      ...documentTag,
      assignedDate: documentTag.assignedDate ? documentTag.assignedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
