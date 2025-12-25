import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentFingerprint, NewDocumentFingerprint } from '../document-fingerprint.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentFingerprint for edit and NewDocumentFingerprintFormGroupInput for create.
 */
type DocumentFingerprintFormGroupInput = IDocumentFingerprint | PartialWithRequiredKeyOf<NewDocumentFingerprint>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentFingerprint | NewDocumentFingerprint> = Omit<T, 'computedDate' | 'lastUpdated'> & {
  computedDate?: string | null;
  lastUpdated?: string | null;
};

type DocumentFingerprintFormRawValue = FormValueOf<IDocumentFingerprint>;

type NewDocumentFingerprintFormRawValue = FormValueOf<NewDocumentFingerprint>;

type DocumentFingerprintFormDefaults = Pick<NewDocumentFingerprint, 'id' | 'computedDate' | 'lastUpdated'>;

type DocumentFingerprintFormGroupContent = {
  id: FormControl<DocumentFingerprintFormRawValue['id'] | NewDocumentFingerprint['id']>;
  documentId: FormControl<DocumentFingerprintFormRawValue['documentId']>;
  fingerprintType: FormControl<DocumentFingerprintFormRawValue['fingerprintType']>;
  fingerprint: FormControl<DocumentFingerprintFormRawValue['fingerprint']>;
  vectorEmbedding: FormControl<DocumentFingerprintFormRawValue['vectorEmbedding']>;
  metadata: FormControl<DocumentFingerprintFormRawValue['metadata']>;
  computedDate: FormControl<DocumentFingerprintFormRawValue['computedDate']>;
  lastUpdated: FormControl<DocumentFingerprintFormRawValue['lastUpdated']>;
};

export type DocumentFingerprintFormGroup = FormGroup<DocumentFingerprintFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentFingerprintFormService {
  createDocumentFingerprintFormGroup(documentFingerprint: DocumentFingerprintFormGroupInput = { id: null }): DocumentFingerprintFormGroup {
    const documentFingerprintRawValue = this.convertDocumentFingerprintToDocumentFingerprintRawValue({
      ...this.getFormDefaults(),
      ...documentFingerprint,
    });
    return new FormGroup<DocumentFingerprintFormGroupContent>({
      id: new FormControl(
        { value: documentFingerprintRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentFingerprintRawValue.documentId, {
        validators: [Validators.required],
      }),
      fingerprintType: new FormControl(documentFingerprintRawValue.fingerprintType),
      fingerprint: new FormControl(documentFingerprintRawValue.fingerprint, {
        validators: [Validators.required],
      }),
      vectorEmbedding: new FormControl(documentFingerprintRawValue.vectorEmbedding),
      metadata: new FormControl(documentFingerprintRawValue.metadata),
      computedDate: new FormControl(documentFingerprintRawValue.computedDate, {
        validators: [Validators.required],
      }),
      lastUpdated: new FormControl(documentFingerprintRawValue.lastUpdated),
    });
  }

  getDocumentFingerprint(form: DocumentFingerprintFormGroup): IDocumentFingerprint | NewDocumentFingerprint {
    return this.convertDocumentFingerprintRawValueToDocumentFingerprint(
      form.getRawValue() as DocumentFingerprintFormRawValue | NewDocumentFingerprintFormRawValue,
    );
  }

  resetForm(form: DocumentFingerprintFormGroup, documentFingerprint: DocumentFingerprintFormGroupInput): void {
    const documentFingerprintRawValue = this.convertDocumentFingerprintToDocumentFingerprintRawValue({
      ...this.getFormDefaults(),
      ...documentFingerprint,
    });
    form.reset(
      {
        ...documentFingerprintRawValue,
        id: { value: documentFingerprintRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentFingerprintFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      computedDate: currentTime,
      lastUpdated: currentTime,
    };
  }

  private convertDocumentFingerprintRawValueToDocumentFingerprint(
    rawDocumentFingerprint: DocumentFingerprintFormRawValue | NewDocumentFingerprintFormRawValue,
  ): IDocumentFingerprint | NewDocumentFingerprint {
    return {
      ...rawDocumentFingerprint,
      computedDate: dayjs(rawDocumentFingerprint.computedDate, DATE_TIME_FORMAT),
      lastUpdated: dayjs(rawDocumentFingerprint.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentFingerprintToDocumentFingerprintRawValue(
    documentFingerprint: IDocumentFingerprint | (Partial<NewDocumentFingerprint> & DocumentFingerprintFormDefaults),
  ): DocumentFingerprintFormRawValue | PartialWithRequiredKeyOf<NewDocumentFingerprintFormRawValue> {
    return {
      ...documentFingerprint,
      computedDate: documentFingerprint.computedDate ? documentFingerprint.computedDate.format(DATE_TIME_FORMAT) : undefined,
      lastUpdated: documentFingerprint.lastUpdated ? documentFingerprint.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
