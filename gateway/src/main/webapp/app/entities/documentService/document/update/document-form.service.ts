import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocument, NewDocument } from '../document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocument for edit and NewDocumentFormGroupInput for create.
 */
type DocumentFormGroupInput = IDocument | PartialWithRequiredKeyOf<NewDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocument | NewDocument> = Omit<T, 'uploadDate' | 'createdDate'> & {
  uploadDate?: string | null;
  createdDate?: string | null;
};

type DocumentFormRawValue = FormValueOf<IDocument>;

type NewDocumentFormRawValue = FormValueOf<NewDocument>;

type DocumentFormDefaults = Pick<NewDocument, 'id' | 'uploadDate' | 'isPublic' | 'createdDate'>;

type DocumentFormGroupContent = {
  id: FormControl<DocumentFormRawValue['id'] | NewDocument['id']>;
  title: FormControl<DocumentFormRawValue['title']>;
  fileName: FormControl<DocumentFormRawValue['fileName']>;
  fileSize: FormControl<DocumentFormRawValue['fileSize']>;
  mimeType: FormControl<DocumentFormRawValue['mimeType']>;
  sha256: FormControl<DocumentFormRawValue['sha256']>;
  s3Key: FormControl<DocumentFormRawValue['s3Key']>;
  s3Bucket: FormControl<DocumentFormRawValue['s3Bucket']>;
  s3Region: FormControl<DocumentFormRawValue['s3Region']>;
  s3Etag: FormControl<DocumentFormRawValue['s3Etag']>;
  thumbnailS3Key: FormControl<DocumentFormRawValue['thumbnailS3Key']>;
  thumbnailSha256: FormControl<DocumentFormRawValue['thumbnailSha256']>;
  webpPreviewS3Key: FormControl<DocumentFormRawValue['webpPreviewS3Key']>;
  webpPreviewSha256: FormControl<DocumentFormRawValue['webpPreviewSha256']>;
  status: FormControl<DocumentFormRawValue['status']>;
  uploadDate: FormControl<DocumentFormRawValue['uploadDate']>;
  isPublic: FormControl<DocumentFormRawValue['isPublic']>;
  downloadCount: FormControl<DocumentFormRawValue['downloadCount']>;
  viewCount: FormControl<DocumentFormRawValue['viewCount']>;
  detectedLanguage: FormControl<DocumentFormRawValue['detectedLanguage']>;
  manualLanguage: FormControl<DocumentFormRawValue['manualLanguage']>;
  languageConfidence: FormControl<DocumentFormRawValue['languageConfidence']>;
  pageCount: FormControl<DocumentFormRawValue['pageCount']>;
  createdDate: FormControl<DocumentFormRawValue['createdDate']>;
  createdBy: FormControl<DocumentFormRawValue['createdBy']>;
  folder: FormControl<DocumentFormRawValue['folder']>;
  documentType: FormControl<DocumentFormRawValue['documentType']>;
};

export type DocumentFormGroup = FormGroup<DocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentFormService {
  createDocumentFormGroup(document?: DocumentFormGroupInput): DocumentFormGroup {
    const documentRawValue = this.convertDocumentToDocumentRawValue({
      ...this.getFormDefaults(),
      ...(document ?? { id: null }),
    });
    return new FormGroup<DocumentFormGroupContent>({
      id: new FormControl(
        { value: documentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(documentRawValue.title, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      fileName: new FormControl(documentRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      fileSize: new FormControl(documentRawValue.fileSize, {
        validators: [Validators.required],
      }),
      mimeType: new FormControl(documentRawValue.mimeType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      sha256: new FormControl(documentRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(documentRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(documentRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      s3Region: new FormControl(documentRawValue.s3Region, {
        validators: [Validators.maxLength(50)],
      }),
      s3Etag: new FormControl(documentRawValue.s3Etag, {
        validators: [Validators.maxLength(100)],
      }),
      thumbnailS3Key: new FormControl(documentRawValue.thumbnailS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      thumbnailSha256: new FormControl(documentRawValue.thumbnailSha256, {
        validators: [Validators.maxLength(64)],
      }),
      webpPreviewS3Key: new FormControl(documentRawValue.webpPreviewS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      webpPreviewSha256: new FormControl(documentRawValue.webpPreviewSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(documentRawValue.status, {
        validators: [Validators.required],
      }),
      uploadDate: new FormControl(documentRawValue.uploadDate, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(documentRawValue.isPublic, {
        validators: [Validators.required],
      }),
      downloadCount: new FormControl(documentRawValue.downloadCount),
      viewCount: new FormControl(documentRawValue.viewCount),
      detectedLanguage: new FormControl(documentRawValue.detectedLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      manualLanguage: new FormControl(documentRawValue.manualLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      languageConfidence: new FormControl(documentRawValue.languageConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      pageCount: new FormControl(documentRawValue.pageCount),
      createdDate: new FormControl(documentRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(documentRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      folder: new FormControl(documentRawValue.folder),
      documentType: new FormControl(documentRawValue.documentType, {
        validators: [Validators.required],
      }),
    });
  }

  getDocument(form: DocumentFormGroup): IDocument | NewDocument {
    return this.convertDocumentRawValueToDocument(form.getRawValue() as DocumentFormRawValue | NewDocumentFormRawValue);
  }

  resetForm(form: DocumentFormGroup, document: DocumentFormGroupInput): void {
    const documentRawValue = this.convertDocumentToDocumentRawValue({ ...this.getFormDefaults(), ...document });
    form.reset({
      ...documentRawValue,
      id: { value: documentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadDate: currentTime,
      isPublic: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentRawValueToDocument(rawDocument: DocumentFormRawValue | NewDocumentFormRawValue): IDocument | NewDocument {
    return {
      ...rawDocument,
      uploadDate: dayjs(rawDocument.uploadDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawDocument.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentToDocumentRawValue(
    document: IDocument | (Partial<NewDocument> & DocumentFormDefaults),
  ): DocumentFormRawValue | PartialWithRequiredKeyOf<NewDocumentFormRawValue> {
    return {
      ...document,
      uploadDate: document.uploadDate ? document.uploadDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: document.createdDate ? document.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
