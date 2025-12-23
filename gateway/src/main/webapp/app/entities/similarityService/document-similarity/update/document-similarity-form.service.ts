import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentSimilarity, NewDocumentSimilarity } from '../document-similarity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentSimilarity for edit and NewDocumentSimilarityFormGroupInput for create.
 */
type DocumentSimilarityFormGroupInput = IDocumentSimilarity | PartialWithRequiredKeyOf<NewDocumentSimilarity>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentSimilarity | NewDocumentSimilarity> = Omit<T, 'computedDate' | 'reviewedDate'> & {
  computedDate?: string | null;
  reviewedDate?: string | null;
};

type DocumentSimilarityFormRawValue = FormValueOf<IDocumentSimilarity>;

type NewDocumentSimilarityFormRawValue = FormValueOf<NewDocumentSimilarity>;

type DocumentSimilarityFormDefaults = Pick<NewDocumentSimilarity, 'id' | 'computedDate' | 'isRelevant' | 'reviewedDate'>;

type DocumentSimilarityFormGroupContent = {
  id: FormControl<DocumentSimilarityFormRawValue['id'] | NewDocumentSimilarity['id']>;
  documentId1: FormControl<DocumentSimilarityFormRawValue['documentId1']>;
  documentId2: FormControl<DocumentSimilarityFormRawValue['documentId2']>;
  similarityScore: FormControl<DocumentSimilarityFormRawValue['similarityScore']>;
  algorithm: FormControl<DocumentSimilarityFormRawValue['algorithm']>;
  features: FormControl<DocumentSimilarityFormRawValue['features']>;
  computedDate: FormControl<DocumentSimilarityFormRawValue['computedDate']>;
  isRelevant: FormControl<DocumentSimilarityFormRawValue['isRelevant']>;
  reviewedBy: FormControl<DocumentSimilarityFormRawValue['reviewedBy']>;
  reviewedDate: FormControl<DocumentSimilarityFormRawValue['reviewedDate']>;
  job: FormControl<DocumentSimilarityFormRawValue['job']>;
};

export type DocumentSimilarityFormGroup = FormGroup<DocumentSimilarityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentSimilarityFormService {
  createDocumentSimilarityFormGroup(documentSimilarity?: DocumentSimilarityFormGroupInput): DocumentSimilarityFormGroup {
    const documentSimilarityRawValue = this.convertDocumentSimilarityToDocumentSimilarityRawValue({
      ...this.getFormDefaults(),
      ...(documentSimilarity ?? { id: null }),
    });
    return new FormGroup<DocumentSimilarityFormGroupContent>({
      id: new FormControl(
        { value: documentSimilarityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId1: new FormControl(documentSimilarityRawValue.documentId1, {
        validators: [Validators.required],
      }),
      documentId2: new FormControl(documentSimilarityRawValue.documentId2, {
        validators: [Validators.required],
      }),
      similarityScore: new FormControl(documentSimilarityRawValue.similarityScore, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      algorithm: new FormControl(documentSimilarityRawValue.algorithm),
      features: new FormControl(documentSimilarityRawValue.features),
      computedDate: new FormControl(documentSimilarityRawValue.computedDate, {
        validators: [Validators.required],
      }),
      isRelevant: new FormControl(documentSimilarityRawValue.isRelevant),
      reviewedBy: new FormControl(documentSimilarityRawValue.reviewedBy, {
        validators: [Validators.maxLength(50)],
      }),
      reviewedDate: new FormControl(documentSimilarityRawValue.reviewedDate),
      job: new FormControl(documentSimilarityRawValue.job),
    });
  }

  getDocumentSimilarity(form: DocumentSimilarityFormGroup): IDocumentSimilarity | NewDocumentSimilarity {
    return this.convertDocumentSimilarityRawValueToDocumentSimilarity(
      form.getRawValue() as DocumentSimilarityFormRawValue | NewDocumentSimilarityFormRawValue,
    );
  }

  resetForm(form: DocumentSimilarityFormGroup, documentSimilarity: DocumentSimilarityFormGroupInput): void {
    const documentSimilarityRawValue = this.convertDocumentSimilarityToDocumentSimilarityRawValue({
      ...this.getFormDefaults(),
      ...documentSimilarity,
    });
    form.reset({
      ...documentSimilarityRawValue,
      id: { value: documentSimilarityRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentSimilarityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      computedDate: currentTime,
      isRelevant: false,
      reviewedDate: currentTime,
    };
  }

  private convertDocumentSimilarityRawValueToDocumentSimilarity(
    rawDocumentSimilarity: DocumentSimilarityFormRawValue | NewDocumentSimilarityFormRawValue,
  ): IDocumentSimilarity | NewDocumentSimilarity {
    return {
      ...rawDocumentSimilarity,
      computedDate: dayjs(rawDocumentSimilarity.computedDate, DATE_TIME_FORMAT),
      reviewedDate: dayjs(rawDocumentSimilarity.reviewedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentSimilarityToDocumentSimilarityRawValue(
    documentSimilarity: IDocumentSimilarity | (Partial<NewDocumentSimilarity> & DocumentSimilarityFormDefaults),
  ): DocumentSimilarityFormRawValue | PartialWithRequiredKeyOf<NewDocumentSimilarityFormRawValue> {
    return {
      ...documentSimilarity,
      computedDate: documentSimilarity.computedDate ? documentSimilarity.computedDate.format(DATE_TIME_FORMAT) : undefined,
      reviewedDate: documentSimilarity.reviewedDate ? documentSimilarity.reviewedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
