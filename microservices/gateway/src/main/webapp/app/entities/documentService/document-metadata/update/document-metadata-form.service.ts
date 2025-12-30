import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentMetadata, NewDocumentMetadata } from '../document-metadata.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentMetadata for edit and NewDocumentMetadataFormGroupInput for create.
 */
type DocumentMetadataFormGroupInput = IDocumentMetadata | PartialWithRequiredKeyOf<NewDocumentMetadata>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentMetadata | NewDocumentMetadata> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentMetadataFormRawValue = FormValueOf<IDocumentMetadata>;

type NewDocumentMetadataFormRawValue = FormValueOf<NewDocumentMetadata>;

type DocumentMetadataFormDefaults = Pick<NewDocumentMetadata, 'id' | 'isSearchable' | 'createdDate'>;

type DocumentMetadataFormGroupContent = {
  id: FormControl<DocumentMetadataFormRawValue['id'] | NewDocumentMetadata['id']>;
  key: FormControl<DocumentMetadataFormRawValue['key']>;
  value: FormControl<DocumentMetadataFormRawValue['value']>;
  dataType: FormControl<DocumentMetadataFormRawValue['dataType']>;
  isSearchable: FormControl<DocumentMetadataFormRawValue['isSearchable']>;
  createdDate: FormControl<DocumentMetadataFormRawValue['createdDate']>;
  document: FormControl<DocumentMetadataFormRawValue['document']>;
};

export type DocumentMetadataFormGroup = FormGroup<DocumentMetadataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentMetadataFormService {
  createDocumentMetadataFormGroup(documentMetadata: DocumentMetadataFormGroupInput = { id: null }): DocumentMetadataFormGroup {
    const documentMetadataRawValue = this.convertDocumentMetadataToDocumentMetadataRawValue({
      ...this.getFormDefaults(),
      ...documentMetadata,
    });
    return new FormGroup<DocumentMetadataFormGroupContent>({
      id: new FormControl(
        { value: documentMetadataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      key: new FormControl(documentMetadataRawValue.key, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      value: new FormControl(documentMetadataRawValue.value, {
        validators: [Validators.required],
      }),
      dataType: new FormControl(documentMetadataRawValue.dataType),
      isSearchable: new FormControl(documentMetadataRawValue.isSearchable, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(documentMetadataRawValue.createdDate, {
        validators: [Validators.required],
      }),
      document: new FormControl(documentMetadataRawValue.document),
    });
  }

  getDocumentMetadata(form: DocumentMetadataFormGroup): IDocumentMetadata | NewDocumentMetadata {
    return this.convertDocumentMetadataRawValueToDocumentMetadata(
      form.getRawValue() as DocumentMetadataFormRawValue | NewDocumentMetadataFormRawValue,
    );
  }

  resetForm(form: DocumentMetadataFormGroup, documentMetadata: DocumentMetadataFormGroupInput): void {
    const documentMetadataRawValue = this.convertDocumentMetadataToDocumentMetadataRawValue({
      ...this.getFormDefaults(),
      ...documentMetadata,
    });
    form.reset(
      {
        ...documentMetadataRawValue,
        id: { value: documentMetadataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentMetadataFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSearchable: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentMetadataRawValueToDocumentMetadata(
    rawDocumentMetadata: DocumentMetadataFormRawValue | NewDocumentMetadataFormRawValue,
  ): IDocumentMetadata | NewDocumentMetadata {
    return {
      ...rawDocumentMetadata,
      createdDate: dayjs(rawDocumentMetadata.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentMetadataToDocumentMetadataRawValue(
    documentMetadata: IDocumentMetadata | (Partial<NewDocumentMetadata> & DocumentMetadataFormDefaults),
  ): DocumentMetadataFormRawValue | PartialWithRequiredKeyOf<NewDocumentMetadataFormRawValue> {
    return {
      ...documentMetadata,
      createdDate: documentMetadata.createdDate ? documentMetadata.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
