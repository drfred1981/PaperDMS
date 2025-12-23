import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentRelation, NewDocumentRelation } from '../document-relation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentRelation for edit and NewDocumentRelationFormGroupInput for create.
 */
type DocumentRelationFormGroupInput = IDocumentRelation | PartialWithRequiredKeyOf<NewDocumentRelation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentRelation | NewDocumentRelation> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentRelationFormRawValue = FormValueOf<IDocumentRelation>;

type NewDocumentRelationFormRawValue = FormValueOf<NewDocumentRelation>;

type DocumentRelationFormDefaults = Pick<NewDocumentRelation, 'id' | 'createdDate'>;

type DocumentRelationFormGroupContent = {
  id: FormControl<DocumentRelationFormRawValue['id'] | NewDocumentRelation['id']>;
  sourceDocumentId: FormControl<DocumentRelationFormRawValue['sourceDocumentId']>;
  targetDocumentId: FormControl<DocumentRelationFormRawValue['targetDocumentId']>;
  relationType: FormControl<DocumentRelationFormRawValue['relationType']>;
  createdBy: FormControl<DocumentRelationFormRawValue['createdBy']>;
  createdDate: FormControl<DocumentRelationFormRawValue['createdDate']>;
};

export type DocumentRelationFormGroup = FormGroup<DocumentRelationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentRelationFormService {
  createDocumentRelationFormGroup(documentRelation?: DocumentRelationFormGroupInput): DocumentRelationFormGroup {
    const documentRelationRawValue = this.convertDocumentRelationToDocumentRelationRawValue({
      ...this.getFormDefaults(),
      ...(documentRelation ?? { id: null }),
    });
    return new FormGroup<DocumentRelationFormGroupContent>({
      id: new FormControl(
        { value: documentRelationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceDocumentId: new FormControl(documentRelationRawValue.sourceDocumentId, {
        validators: [Validators.required],
      }),
      targetDocumentId: new FormControl(documentRelationRawValue.targetDocumentId, {
        validators: [Validators.required],
      }),
      relationType: new FormControl(documentRelationRawValue.relationType, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(documentRelationRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(documentRelationRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentRelation(form: DocumentRelationFormGroup): IDocumentRelation | NewDocumentRelation {
    return this.convertDocumentRelationRawValueToDocumentRelation(
      form.getRawValue() as DocumentRelationFormRawValue | NewDocumentRelationFormRawValue,
    );
  }

  resetForm(form: DocumentRelationFormGroup, documentRelation: DocumentRelationFormGroupInput): void {
    const documentRelationRawValue = this.convertDocumentRelationToDocumentRelationRawValue({
      ...this.getFormDefaults(),
      ...documentRelation,
    });
    form.reset({
      ...documentRelationRawValue,
      id: { value: documentRelationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentRelationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertDocumentRelationRawValueToDocumentRelation(
    rawDocumentRelation: DocumentRelationFormRawValue | NewDocumentRelationFormRawValue,
  ): IDocumentRelation | NewDocumentRelation {
    return {
      ...rawDocumentRelation,
      createdDate: dayjs(rawDocumentRelation.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentRelationToDocumentRelationRawValue(
    documentRelation: IDocumentRelation | (Partial<NewDocumentRelation> & DocumentRelationFormDefaults),
  ): DocumentRelationFormRawValue | PartialWithRequiredKeyOf<NewDocumentRelationFormRawValue> {
    return {
      ...documentRelation,
      createdDate: documentRelation.createdDate ? documentRelation.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
