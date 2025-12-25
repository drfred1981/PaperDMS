import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentTemplate, NewDocumentTemplate } from '../document-template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentTemplate for edit and NewDocumentTemplateFormGroupInput for create.
 */
type DocumentTemplateFormGroupInput = IDocumentTemplate | PartialWithRequiredKeyOf<NewDocumentTemplate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentTemplate | NewDocumentTemplate> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentTemplateFormRawValue = FormValueOf<IDocumentTemplate>;

type NewDocumentTemplateFormRawValue = FormValueOf<NewDocumentTemplate>;

type DocumentTemplateFormDefaults = Pick<NewDocumentTemplate, 'id' | 'isActive' | 'createdDate'>;

type DocumentTemplateFormGroupContent = {
  id: FormControl<DocumentTemplateFormRawValue['id'] | NewDocumentTemplate['id']>;
  name: FormControl<DocumentTemplateFormRawValue['name']>;
  templateSha256: FormControl<DocumentTemplateFormRawValue['templateSha256']>;
  templateS3Key: FormControl<DocumentTemplateFormRawValue['templateS3Key']>;
  isActive: FormControl<DocumentTemplateFormRawValue['isActive']>;
  createdBy: FormControl<DocumentTemplateFormRawValue['createdBy']>;
  createdDate: FormControl<DocumentTemplateFormRawValue['createdDate']>;
  documentType: FormControl<DocumentTemplateFormRawValue['documentType']>;
};

export type DocumentTemplateFormGroup = FormGroup<DocumentTemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentTemplateFormService {
  createDocumentTemplateFormGroup(documentTemplate: DocumentTemplateFormGroupInput = { id: null }): DocumentTemplateFormGroup {
    const documentTemplateRawValue = this.convertDocumentTemplateToDocumentTemplateRawValue({
      ...this.getFormDefaults(),
      ...documentTemplate,
    });
    return new FormGroup<DocumentTemplateFormGroupContent>({
      id: new FormControl(
        { value: documentTemplateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(documentTemplateRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      templateSha256: new FormControl(documentTemplateRawValue.templateSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      templateS3Key: new FormControl(documentTemplateRawValue.templateS3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      isActive: new FormControl(documentTemplateRawValue.isActive, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(documentTemplateRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(documentTemplateRawValue.createdDate, {
        validators: [Validators.required],
      }),
      documentType: new FormControl(documentTemplateRawValue.documentType, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentTemplate(form: DocumentTemplateFormGroup): IDocumentTemplate | NewDocumentTemplate {
    return this.convertDocumentTemplateRawValueToDocumentTemplate(
      form.getRawValue() as DocumentTemplateFormRawValue | NewDocumentTemplateFormRawValue,
    );
  }

  resetForm(form: DocumentTemplateFormGroup, documentTemplate: DocumentTemplateFormGroupInput): void {
    const documentTemplateRawValue = this.convertDocumentTemplateToDocumentTemplateRawValue({
      ...this.getFormDefaults(),
      ...documentTemplate,
    });
    form.reset(
      {
        ...documentTemplateRawValue,
        id: { value: documentTemplateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentTemplateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentTemplateRawValueToDocumentTemplate(
    rawDocumentTemplate: DocumentTemplateFormRawValue | NewDocumentTemplateFormRawValue,
  ): IDocumentTemplate | NewDocumentTemplate {
    return {
      ...rawDocumentTemplate,
      createdDate: dayjs(rawDocumentTemplate.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentTemplateToDocumentTemplateRawValue(
    documentTemplate: IDocumentTemplate | (Partial<NewDocumentTemplate> & DocumentTemplateFormDefaults),
  ): DocumentTemplateFormRawValue | PartialWithRequiredKeyOf<NewDocumentTemplateFormRawValue> {
    return {
      ...documentTemplate,
      createdDate: documentTemplate.createdDate ? documentTemplate.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
