import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentVersion, NewDocumentVersion } from '../document-version.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentVersion for edit and NewDocumentVersionFormGroupInput for create.
 */
type DocumentVersionFormGroupInput = IDocumentVersion | PartialWithRequiredKeyOf<NewDocumentVersion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentVersion | NewDocumentVersion> = Omit<T, 'uploadDate'> & {
  uploadDate?: string | null;
};

type DocumentVersionFormRawValue = FormValueOf<IDocumentVersion>;

type NewDocumentVersionFormRawValue = FormValueOf<NewDocumentVersion>;

type DocumentVersionFormDefaults = Pick<NewDocumentVersion, 'id' | 'uploadDate' | 'isActive'>;

type DocumentVersionFormGroupContent = {
  id: FormControl<DocumentVersionFormRawValue['id'] | NewDocumentVersion['id']>;
  versionNumber: FormControl<DocumentVersionFormRawValue['versionNumber']>;
  sha256: FormControl<DocumentVersionFormRawValue['sha256']>;
  s3Key: FormControl<DocumentVersionFormRawValue['s3Key']>;
  fileSize: FormControl<DocumentVersionFormRawValue['fileSize']>;
  uploadDate: FormControl<DocumentVersionFormRawValue['uploadDate']>;
  isActive: FormControl<DocumentVersionFormRawValue['isActive']>;
  createdBy: FormControl<DocumentVersionFormRawValue['createdBy']>;
  document: FormControl<DocumentVersionFormRawValue['document']>;
};

export type DocumentVersionFormGroup = FormGroup<DocumentVersionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentVersionFormService {
  createDocumentVersionFormGroup(documentVersion: DocumentVersionFormGroupInput = { id: null }): DocumentVersionFormGroup {
    const documentVersionRawValue = this.convertDocumentVersionToDocumentVersionRawValue({
      ...this.getFormDefaults(),
      ...documentVersion,
    });
    return new FormGroup<DocumentVersionFormGroupContent>({
      id: new FormControl(
        { value: documentVersionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      versionNumber: new FormControl(documentVersionRawValue.versionNumber, {
        validators: [Validators.required],
      }),
      sha256: new FormControl(documentVersionRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(documentVersionRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      fileSize: new FormControl(documentVersionRawValue.fileSize, {
        validators: [Validators.required],
      }),
      uploadDate: new FormControl(documentVersionRawValue.uploadDate, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(documentVersionRawValue.isActive, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(documentVersionRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      document: new FormControl(documentVersionRawValue.document),
    });
  }

  getDocumentVersion(form: DocumentVersionFormGroup): IDocumentVersion | NewDocumentVersion {
    return this.convertDocumentVersionRawValueToDocumentVersion(
      form.getRawValue() as DocumentVersionFormRawValue | NewDocumentVersionFormRawValue,
    );
  }

  resetForm(form: DocumentVersionFormGroup, documentVersion: DocumentVersionFormGroupInput): void {
    const documentVersionRawValue = this.convertDocumentVersionToDocumentVersionRawValue({ ...this.getFormDefaults(), ...documentVersion });
    form.reset(
      {
        ...documentVersionRawValue,
        id: { value: documentVersionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentVersionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadDate: currentTime,
      isActive: false,
    };
  }

  private convertDocumentVersionRawValueToDocumentVersion(
    rawDocumentVersion: DocumentVersionFormRawValue | NewDocumentVersionFormRawValue,
  ): IDocumentVersion | NewDocumentVersion {
    return {
      ...rawDocumentVersion,
      uploadDate: dayjs(rawDocumentVersion.uploadDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentVersionToDocumentVersionRawValue(
    documentVersion: IDocumentVersion | (Partial<NewDocumentVersion> & DocumentVersionFormDefaults),
  ): DocumentVersionFormRawValue | PartialWithRequiredKeyOf<NewDocumentVersionFormRawValue> {
    return {
      ...documentVersion,
      uploadDate: documentVersion.uploadDate ? documentVersion.uploadDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
